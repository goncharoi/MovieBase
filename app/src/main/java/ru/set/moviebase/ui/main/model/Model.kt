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
            MovieEntity(UUID.randomUUID().toString(),"Фильм 1", "Movie 1","Какое-то очень длинное описание очень скучного фильма", 1985, 0.9f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 2","Movie 2","Какое-то очень длинное описание очень скучного фильма", 1984, 0.2f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 3","Movie 3","Какое-то очень длинное описание очень скучного фильма", 1956, 0.9f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 4","Movie 4","Какое-то очень длинное описание очень скучного фильма",1999, 0.5f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 5","Movie 1","Какое-то очень длинное описание очень скучного фильма", 2001, 0.6f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 6","Movie 2","Какое-то очень длинное описание очень скучного фильма", 2012, 0.1f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 7","Movie 3","Какое-то очень длинное описание очень скучного фильма", 2005, 0.6f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 8","Movie 4","Какое-то очень длинное описание очень скучного фильма", 2009, 0.5f, true),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 9","Movie 9","Какое-то очень длинное описание очень скучного фильма", 1985, 0.8f, false),
            MovieEntity(UUID.randomUUID().toString(),"Фильм 10","Movie 10","Какое-то очень длинное описание очень скучного фильма", 2021, 0.9f, false)
        )
    }
}