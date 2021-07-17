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

    override fun chooseMovie(movieGUID: String){
        chosenMovieToObserve.value = moviesListToObserve.value?.filter { it.GUID == movieGUID }?.get(0)
    }

    fun unchooseMovie() {
        chosenMovieToObserve.value = null
    }

    interface OnMoviesChangedListener {
        fun onLoad(movies: Movies?)
    }

    override fun changeMovieFavoritesStatus(movieGUID: String) {
        val movies : Movies? = moviesListToObserve.value
        movies?.filter { it.GUID == movieGUID }?.get(0)?.let { it.isFavorite = !it.isFavorite }
        moviesListToObserve.value = movies
    }
    override fun setMovieRating(movieGUID: String, rating: Float) {
        val movies : Movies? = moviesListToObserve.value
        movies?.filter { it.GUID == movieGUID }?.get(0)?.let { it.rating = rating }
        moviesListToObserve.value = movies
    }
}