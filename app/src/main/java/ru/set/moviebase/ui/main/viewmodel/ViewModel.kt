package ru.set.moviebase.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.set.moviebase.ui.main.model.Model
import ru.set.moviebase.ui.main.model.Movies
import ru.set.moviebase.ui.main.view.MoviesAdapter


class ViewModel(
    private val liveDataToObserve: MutableLiveData<Movies> = MutableLiveData(),
    private val model: Model = Model()
) : ViewModel(), MoviesAdapter.OnItemClickListener {

    fun getData(): LiveData<Movies> {
        return liveDataToObserve
    }

    fun loadData() {
        model.loadData(object : OnMoviesChangedListener {
            override fun onLoad(movies: Movies?) { //асинхронная загрузка
                liveDataToObserve.postValue(movies)
            }
        })
    }

    fun loadDataLocal() {
         liveDataToObserve.value = model.loadDataLocal()
    }

    interface OnMoviesChangedListener {
        fun onLoad(movies: Movies?)
    }

    override fun changeMovieFavoritesStatus(movieGUID: String) {
        var movies : Movies? = liveDataToObserve.value
        movies?.filter { it.GUID == movieGUID }?.get(0)?.let { it.IsFavorite = !it.IsFavorite }
        liveDataToObserve.value = movies
    }
    override fun setMovieRating(movieGUID: String, rating: Float) {
        var movies : Movies? = liveDataToObserve.value
        movies?.filter { it.GUID == movieGUID }?.get(0)?.let { it.Rating = rating }
        liveDataToObserve.value = movies
    }
}