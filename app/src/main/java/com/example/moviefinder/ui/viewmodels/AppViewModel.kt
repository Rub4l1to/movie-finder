package com.example.moviefinder.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviefinder.data.Movie
import com.example.moviefinder.data.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import android.content.Context
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class AppViewModel : ViewModel() {

    // API Key to test: 9fe3a4a6ffd4d7ed3a22e7d13b29dc9a
    var apiKey by mutableStateOf("")
        private set

    var alertTitle by mutableStateOf("")
        private set

    var alertContent by mutableStateOf("")
        private set

    var requestInProgress by mutableStateOf(false)
        private set

    var moviesList = mutableStateListOf<Movie>()
        private set

    var selectedMoviesListIndex by mutableIntStateOf(0)
        private set

    fun updateApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    fun showAlert(title: String, content: String) {
        alertTitle = title
        alertContent = content
    }

    fun closeAlert() {
        alertTitle = ""
        alertContent = ""
    }

    fun updateSelectedMoviesListIndex(index: Int) {
        selectedMoviesListIndex = index
    }

    fun validateApiKey(onResult: (isValid: Boolean?) -> Unit) {

        requestInProgress = true

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val encodedQuery = URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString())

                val url = URL("https://api.themoviedb.org/3/authentication?api_key=$encodedQuery")

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) { onResult(true) }
                } else {
                    withContext(Dispatchers.Main) { onResult(false) }
                }

            } catch (_: Exception) {
                onResult(null)
            } finally {
                withContext(Dispatchers.Main) { requestInProgress = false }
            }
        }
    }

    fun searchMovies(searchQuery: String, onResult: (error: Boolean) -> Unit) {
        requestInProgress = true

        viewModelScope.launch(Dispatchers.IO) {

            try {

                moviesList.clear()

                val encodedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8.toString())

                val result =
                    URL("https://api.themoviedb.org/3/search/movie?api_key=$apiKey&language=es-ES&query=$encodedQuery").readText()

                val jsonFormat = Json { ignoreUnknownKeys = true }

                val movieResponse = jsonFormat.decodeFromString<MovieListResponse>(result)

                if (movieResponse.results.isNotEmpty()) {
                    moviesList.addAll(movieResponse.results)
                }

                withContext(Dispatchers.Main) { onResult(false) }

            } catch (_: Exception) {
                withContext(Dispatchers.Main) { onResult(true) }
            } finally {
                withContext(Dispatchers.Main) { requestInProgress = false }
            }
        }
    }
    fun logout(context: Context, onLoggedOut: () -> Unit) {
        context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
            .edit().remove("api_key").apply()
        updateApiKey("")
        onLoggedOut()
    }

}
