package ru.set.moviebase.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.set.moviebase.ui.main.model.Model
import ru.set.moviebase.ui.main.model.MovieEntity
import ru.set.moviebase.ui.main.model.Movies
import ru.set.moviebase.ui.main.view.MoviesAdapter

class ViewModel(
    private val moviesListToObserve: MutableLiveData<Movies> = MutableLiveData(),
    private val chosenMovieToObserve: MutableLiveData<MovieEntity> = MutableLiveData(),
    private val model: Model = Model()
) : ViewModel(), MoviesAdapter.OnItemClickListener {

    fun getMoviesList(): LiveData<Movies> {
        return moviesListToObserve
    }

    fun getChosenMovie(): LiveData<MovieEntity> {
        return chosenMovieToObserve
    }

    fun loadData() {
        model.loadData(object : OnMoviesChangedListener {
            override fun onLoad(movies: Movies?) { //асинхронная загрузка
                moviesListToObserve.postValue(movies)
            }
        })
    }

    fun loadDataLocal() {
        moviesListToObserve.value = model.loadDataLocal()
    }

    override fun chooseMovie(movieGUID: String) {
        chosenMovieToObserve.value =
            moviesListToObserve.value?.filter { it.GUID == movieGUID }?.get(0)
    }

    fun unchooseMovie() {
        chosenMovieToObserve.value = null
    }

    interface OnMoviesChangedListener {
        fun onLoad(movies: Movies?)
    }

    override fun changeMovieFavoritesStatus(movieGUID: String) {
        changeMovie(movieGUID, changeFavoritesStatus = true)
    }

    override fun setMovieRating(movieGUID: String, rating: Float) {
        changeMovie(movieGUID, rating, changeRating = true)
    }

    private fun changeMovie(
        movieGUID: String,
        rating: Float = 0.0f,
        changeRating: Boolean = false,
        changeFavoritesStatus: Boolean = false
    ) {
        var movies: Movies = listOf()
        moviesListToObserve.value?.forEach {
            movies = when {
                it.GUID != movieGUID -> movies.plus(it)
                changeRating -> movies.plus(it.copy(rating = rating))
                changeFavoritesStatus -> movies.plus(it.copy(isFavorite = !it.isFavorite))
                else -> movies.plus(it)
            }
        }
        moviesListToObserve.value = movies
    }
}