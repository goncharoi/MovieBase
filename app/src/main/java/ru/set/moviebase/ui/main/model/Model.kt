package ru.set.moviebase.ui.main.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.set.moviebase.R
import ru.set.moviebase.ui.main.viewmodel.NOW_PLAYING_REQUEST_TYPE
import ru.set.moviebase.ui.main.viewmodel.UPCOMING_REQUEST_TYPE
import ru.set.moviebase.ui.main.viewmodel.ViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val API_KEY = "665b3b4cab72e973bf2c0bd2c0311051"

class Model {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadData(listener: ViewModel.OnMoviesChangedListener) {
        loadDataOfType(NOW_PLAYING_REQUEST_TYPE, listener, listener::onNowPlayingMoviesLoad)
        loadDataOfType(UPCOMING_REQUEST_TYPE, listener, listener::onUpcomingMoviesLoad)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadDataOfType(
        type: String,
        listener: ViewModel.OnMoviesChangedListener,
        onLoad: (movies: Movies?) -> Unit
    ) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${type}?api_key=${API_KEY}")
            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    try {
                        onLoad(parseJsonToMovies(bufferedReader))
                    } catch (e: JsonSyntaxException) {
                        listener.onError(R.string.fail_parsing_message)
                    }
                } catch (e: Exception) {
                    listener.onError(R.string.fail_connection_message)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            listener.onError(R.string.fail_URI_message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun parseJsonToMovies(reader: BufferedReader): Movies {
        return Gson().fromJson(
            reader.lines().collect(Collectors.joining("\n")),
            NowPlayingJson::class.java
        ).results
    }
}