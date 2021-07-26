package ru.set.moviebase.ui.main.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.set.moviebase.ui.main.model.Model
import ru.set.moviebase.ui.main.model.MovieEntity
import ru.set.moviebase.ui.main.model.Movies
import ru.set.moviebase.ui.main.view.MoviesAdapter

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
}