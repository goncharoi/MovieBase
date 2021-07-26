package ru.set.moviebase.ui.main.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntity(
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val release_date: String,
    var vote_average: Float,
    var isFavorite: Boolean
) : Parcelable