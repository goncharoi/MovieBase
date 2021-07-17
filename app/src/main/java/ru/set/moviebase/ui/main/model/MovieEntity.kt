package ru.set.moviebase.ui.main.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntity(
    val GUID: String,
    val title: String,
    val originTitle: String,
    val description: String,
    val year: Int,
    var rating: Float,
    var isFavorite: Boolean
) : Parcelable