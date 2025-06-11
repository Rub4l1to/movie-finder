package com.example.moviefinder.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.moviefinder.R
import com.example.moviefinder.data.Movie
import com.example.moviefinder.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(appViewModel: AppViewModel, navController: NavController) {

    val demoMovie = Movie(
        adult = false,
        backdrop_path = "https://image.tmdb.org/t/p/original/vL5LR6WdxWPjLPFRLe133jXWsh5.jpg",
        genre_ids = listOf(28, 12, 14, 878),
        id = 19995,
        original_language = "en",
        original_title = "Avatar",
        overview = "Año 2154. Jake Sully, un exmarine en silla de ruedas, es enviado al planeta Pandora, donde se ha creado el programa Avatar, gracias al cual los seres humanos pueden controlar de forma remota un cuerpo biológico con apariencia y genética de la especie nativa. Pronto se encontrará con la encrucijada entre seguir las órdenes de sus superiores o defender al mundo que le ha acogido y siente como suyo.",
        popularity = 24.5763,
        poster_path = "https://image.tmdb.org/t/p/original/tXmTHdrZgNsULqCbThK2Dt2X9Wt.jpg",
        release_date = "2009-12-15",
        title = "Avatar",
        video = false,
        vote_average = 7.59,
        vote_count = 32265
    )

    //val movie = demoMovie

    val movie = appViewModel.moviesList[appViewModel.selectedMoviesListIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = movie.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            AsyncImage(
                model = "https://image.tmdb.org/t/p/original/" + movie.poster_path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .alpha(0.2f)
            )

            Column(Modifier.verticalScroll(rememberScrollState())) {
                if (movie.backdrop_path != null) {
                    SubcomposeAsyncImage(
                        model = "https://image.tmdb.org/t/p/original/" + movie.backdrop_path,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                        loading = {
                            LinearProgressIndicator(
                                modifier = Modifier.padding(vertical = 85.dp)
                            )
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Movie,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxSize()
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.release_date_label, movie.release_date),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/original/" + movie.poster_path,
                            contentDescription = "Poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = movie.overview,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(
                                R.string.rating,
                                movie.vote_average,
                                movie.vote_count
                            ),
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = stringResource(R.string.popularity, movie.popularity),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
        }
    }

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun MovieDetailsScreenPreview() {
    val appViewModel = AppViewModel()

    MovieDetailsScreen(appViewModel, rememberNavController())
}