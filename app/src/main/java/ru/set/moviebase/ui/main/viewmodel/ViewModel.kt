package ru.set.moviebase.ui.main.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.set.moviebase.R
import ru.set.moviebase.ui.main.model.*
import ru.set.moviebase.ui.main.view.MoviesAdapter

const val NOW_PLAYING_REQUEST_TYPE = "now_playing"
const val UPCOMING_REQUEST_TYPE = "upcoming"

class ViewModel(
    private val NowPlayingMoviesToObserve: MutableLiveData<Movies> = MutableLiveData(),
    private val UpcomingMoviesToObserve: MutableLiveData<Movies> = MutableLiveData(),
    private val errorMessageToObserve: MutableLiveData<Int> = MutableLiveData(),
    private val chosenMovieToObserve: MutableLiveData<MovieEntity> = MutableLiveData(),
    private val model: Model = Model()
) : ViewModel(), MoviesAdapter.OnItemClickListener {

    fun getNowPlayingMovies(): LiveData<Movies> {
        return NowPlayingMoviesToObserve
    }

    fun getUpcomingMovies(): LiveData<Movies> {
        return UpcomingMoviesToObserve
    }

    fun getChosenMovie(): LiveData<MovieEntity> {
        return chosenMovieToObserve
    }

    fun getErrorMessage(): LiveData<Int> {
        return errorMessageToObserve
    }

    fun loadDataWithService(context: Context?) {
        context?.let {
            it.startService(Intent(it, TheMovieDBService::class.java).apply {
                putExtra(DATA_TYPE_EXTRA, NOW_PLAYING_REQUEST_TYPE)
            })
        }
        context?.let {
            it.startService(Intent(it, TheMovieDBService::class.java).apply {
                putExtra(DATA_TYPE_EXTRA, UPCOMING_REQUEST_TYPE)
            })
        }
    }

    fun registerReceiver(context: Context?) {
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                getLoadResultReceiver(),
                IntentFilter(DETAILS_INTENT_FILTER)
            )
        }
    }

    fun unregisterReceiver(context: Context?) {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(getLoadResultReceiver())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadData() {
        model.loadData(object : OnMoviesChangedListener {
            override fun onNowPlayingMoviesLoad(movies: Movies?) { //асинхронная загрузка
                NowPlayingMoviesToObserve.postValue(movies)
            }

            override fun onUpcomingMoviesLoad(movies: Movies?) { //асинхронная загрузка
                UpcomingMoviesToObserve.postValue(movies)
            }

            override fun onError(messageId: Int) {
                errorMessageToObserve.postValue(messageId)
            }
        })
    }

    override fun chooseMovie(movieGUID: Int) {
        chosenMovieToObserve.value =
            NowPlayingMoviesToObserve.value?.filter { it.id == movieGUID }?.get(0)
    }

    fun unchooseMovie() {
        chosenMovieToObserve.value = null
    }

    interface OnMoviesChangedListener {
        fun onNowPlayingMoviesLoad(movies: Movies?)
        fun onUpcomingMoviesLoad(movies: Movies?)
        fun onError(messageId: Int)
    }

    override fun changeMovieFavoritesStatus(movieGUID: Int) {
        changeMovie(NowPlayingMoviesToObserve, movieGUID, changeFavoritesStatus = true)
        changeMovie(UpcomingMoviesToObserve, movieGUID, changeFavoritesStatus = true)
    }

    override fun setMovieRating(movieGUID: Int, rating: Float) {
        changeMovie(NowPlayingMoviesToObserve, movieGUID, rating, changeRating = true)
        changeMovie(UpcomingMoviesToObserve, movieGUID, changeFavoritesStatus = true)
    }

    private fun changeMovie(
        MoviesToObserve: MutableLiveData<Movies>,
        movieGUID: Int,
        rating: Float = 0.0f,
        changeRating: Boolean = false,
        changeFavoritesStatus: Boolean = false
    ) {
        var movies: Movies = listOf()
        MoviesToObserve.value?.forEach {
            movies = when {
                it.id != movieGUID -> movies.plus(it)
                changeRating -> movies.plus(it.copy(vote_average = rating))
                changeFavoritesStatus -> movies.plus(it.copy(isFavorite = !it.isFavorite))
                else -> movies.plus(it)
            }
        }
        MoviesToObserve.value = movies
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_ERROR_MESSAGE_EXTRA -> errorMessageToObserve.postValue(
                    intent.getIntExtra(DETAILS_ERROR_MESSAGE_EXTRA, R.string.unknown_error)
                )
                DETAILS_JSON_STRING_EXTRA -> onDataReceive(intent)
                else -> errorMessageToObserve.postValue(R.string.unknown_error)
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun onDataReceive(intent: Intent) {
            when (intent.getStringExtra(DETAILS_REQUEST_TYPE_STRING_EXTRA)) {
                NOW_PLAYING_REQUEST_TYPE -> NowPlayingMoviesToObserve.postValue(
                    parseJsonToMovies(intent)
                )
                UPCOMING_REQUEST_TYPE -> UpcomingMoviesToObserve.postValue(
                    parseJsonToMovies(intent)
                )
                else -> errorMessageToObserve.postValue(R.string.unknown_error)
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun parseJsonToMovies(intent: Intent): Movies {
            return Gson().fromJson(
                intent.getStringExtra(DETAILS_JSON_STRING_EXTRA),
                NowPlayingJson::class.java
            ).results
        }
    }

    fun getLoadResultReceiver() = loadResultsReceiver

}