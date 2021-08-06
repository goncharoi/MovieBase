package ru.set.moviebase.ui.main.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.JsonSyntaxException
import ru.set.moviebase.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val DATA_TYPE_EXTRA = "DataType"

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "DETAILS LOAD RESULT"
const val DETAILS_JSON_STRING_EXTRA = "DETAILS JSON STRING"
const val DETAILS_REQUEST_TYPE_STRING_EXTRA = "DETAILS TYPE REQUEST STRING"
const val DETAILS_ERROR_MESSAGE_EXTRA = "DETAILS ERROR MESSAGE"

private const val API_KEY = "665b3b4cab72e973bf2c0bd2c0311051"

class TheMovieDBService() : IntentService("TheMovieDBService") {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.getStringExtra(DATA_TYPE_EXTRA)?.let { loadDataOfType(it) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadDataOfType(type: String) {
        try {
            val uri = URL("https://api.themoviedb.org/3/movie/${type}?api_key=${API_KEY}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))
                try {
                    onLoad(bufferedReader.lines().collect(Collectors.joining("\n")), type)
                } catch (e: JsonSyntaxException) {
                    onError(R.string.fail_parsing_message)
                }
            } catch (e: Exception) {
                onError(R.string.fail_connection_message)
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onError(R.string.fail_URI_message)
        }
    }

    private fun onError(error: Int) {
        putLoadResult(DETAILS_ERROR_MESSAGE_EXTRA)
        broadcastIntent.putExtra(DETAILS_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onLoad(result: String, type: String) {
        putLoadResult(DETAILS_JSON_STRING_EXTRA)
        broadcastIntent.putExtra(DETAILS_JSON_STRING_EXTRA, result)
        broadcastIntent.putExtra(DETAILS_REQUEST_TYPE_STRING_EXTRA, type)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

}