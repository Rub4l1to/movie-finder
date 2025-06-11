package com.example.moviefinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.moviefinder.ui.navigation.AppNavigation
import com.example.moviefinder.ui.theme.MovieFinderTheme
import com.example.moviefinder.ui.viewmodels.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appViewModel = ViewModelProvider(this)[AppViewModel::class.java]

        setContent {
            MovieFinderTheme {
                AppNavigation(appViewModel, applicationContext)
            }
        }
    }
}
