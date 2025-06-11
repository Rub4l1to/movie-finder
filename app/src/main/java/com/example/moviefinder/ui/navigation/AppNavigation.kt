package com.example.moviefinder.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviefinder.ui.screens.AppScreens
import com.example.moviefinder.ui.screens.MainScreen
import com.example.moviefinder.ui.screens.MovieDetailsScreen
import com.example.moviefinder.ui.screens.WelcomeScreen
import com.example.moviefinder.ui.viewmodels.AppViewModel

@Composable
fun AppNavigation(appViewModel: AppViewModel, context: Context) {

    val navController = rememberNavController()

    val apiKey = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE).getString("api_key", "")
    val startDestination = if (apiKey != null && apiKey.isNotEmpty()) {
        appViewModel.updateApiKey(apiKey)
        AppScreens.MAIN_SCREEN
    } else {
        AppScreens.WELCOME_SCREEN
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = AppScreens.WELCOME_SCREEN) { WelcomeScreen(appViewModel, navController) }
        composable(route = AppScreens.MAIN_SCREEN) { MainScreen(appViewModel, navController) }
        composable(route = AppScreens.MOVIE_DETAILS_SCREEN) { MovieDetailsScreen(appViewModel, navController) }
    }

}
