package ru.set.moviebase.ui.main.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY
import android.widget.Toast
import ru.set.moviebase.R

class MainBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false)) {
            Toast.makeText(context, context.getText(R.string.no_connection), Toast.LENGTH_LONG)
                .show()
        }
    }
}
