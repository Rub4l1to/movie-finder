package com.example.moviefinder.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import com.example.moviefinder.ui.components.MovieCard
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moviefinder.R
import com.example.moviefinder.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(appViewModel: AppViewModel, navController: NavController) {

    val context = LocalContext.current

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        appViewModel.logout(context) {
                            navController.navigate(AppScreens.WELCOME_SCREEN) {
                                popUpTo(AppScreens.MAIN_SCREEN) { inclusive = true }
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            Column(modifier = Modifier.fillMaxSize()) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(if (appViewModel.requestInProgress) 1f else 0f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = searchQuery, onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    singleLine = true,
                    enabled = !appViewModel.requestInProgress,
                    label = { Text(stringResource(R.string.search_movie)) },
                    trailingIcon = {
                        IconButton(onClick = {
                            appViewModel.searchMovies(searchQuery) { error ->
                                if (error) {
                                    appViewModel.showAlert(
                                        context.getString(R.string.error),
                                        context.getString(R.string.search_error)
                                    )
                                } else {
                                    if (appViewModel.moviesList.isEmpty()) {
                                        Toast.makeText(
                                            context, context.getString(R.string.no_results),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }, enabled = searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                null
                            )
                        }
                    }
                )

                if (appViewModel.moviesList.isEmpty()) {

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Movie,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(100.dp)
                        )
                    }

                } else {

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    LazyColumn {
                        itemsIndexed(items = appViewModel.moviesList) { index, movie ->

                            MovieCard(
                                movie = movie,
                                onClick = {
                                    appViewModel.updateSelectedMoviesListIndex(index = index)
                                    navController.navigate(AppScreens.MOVIE_DETAILS_SCREEN)
                                }
                            )

                        }
                    }

                }

            }

        }
    }

    if (appViewModel.alertContent.isNotEmpty()) {
        AlertDialog(
            title = { Text(text = appViewModel.alertTitle) },
            text = { Text(text = appViewModel.alertContent) },
            onDismissRequest = {
                appViewModel.closeAlert()
            }, confirmButton = {
                TextButton(onClick = {
                    appViewModel.closeAlert()
                }) { Text(text = stringResource(R.string.ok)) }
            })
    }

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun MainScreenPreview() {
    val appViewModel = AppViewModel()

    MainScreen(appViewModel, rememberNavController())
}
