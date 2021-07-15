package ru.set.moviebase.ui.main.model

import ru.set.moviebase.ui.main.viewmodel.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class Model {
    fun loadData(listener: ViewModel.OnMoviesChangedListener) {
        // TODO выборка данных
        listener.onLoad(ArrayList())
    }

    fun loadDataLocal(): Movies {
        return listOf(
            MovieEntity(UUID.randomUUID().toString(),"Movie 1", 1985, 0.9f, true),
            MovieEntity(UUID.randomUUID().toString(),"Movie 2", 1984, 0.2f, false),
            MovieEntity(UUID.randomUUID().toString(),"Movie 3", 1956, 0.9f, false),
            MovieEntity(UUID.randomUUID().toString(),"Movie 4",1999, 0.5f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 1", 2001, 0.6f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 2", 2012, 0.1f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 3", 2005, 0.6f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 4", 2009, 0.5f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 9", 1985, 0.8f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 10", 2021, 0.9f, false)
        )
    }
}