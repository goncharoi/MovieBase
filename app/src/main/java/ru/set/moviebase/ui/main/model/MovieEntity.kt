package ru.set.moviebase.ui.main.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntity(
    val GUID: String,
    val Title: String,
    val Year: Int,
    var Rating: Float,
    var IsFavorite: Boolean
) : Parcelable