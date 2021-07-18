package ru.set.moviebase.ui.main.view

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showTextById(id: Int, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, this.resources.getText(id), length).show()
}