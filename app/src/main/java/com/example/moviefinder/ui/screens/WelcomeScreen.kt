package com.example.moviefinder.ui.screens

import androidx.core.content.edit
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviefinder.R
import com.example.moviefinder.ui.components.AppTopBar
import com.example.moviefinder.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavController, appViewModel: AppViewModel) {
    val context = LocalContext.current
    var apiKey by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppTopBar(title = stringResource(R.string.app_name)) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.background_1),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.5f
            )

            // Contenido encima
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.welcome_message),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("API Key") },
                    singleLine = true,
                    enabled = !loading
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        loading = true
                        appViewModel.updateApiKey(apiKey)
                        appViewModel.validateApiKey { isValid ->
                            loading = false
                            if (isValid == true) {
                                context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                                    .edit { putString("api_key", apiKey) }
                                navController.navigate(AppScreens.MAIN_SCREEN) {
                                    popUpTo(AppScreens.WELCOME_SCREEN) { inclusive = true }
                                }
                            } else {
                                error = "API Key inválida"
                            }
                        }
                    },
                    enabled = apiKey.isNotBlank() && !loading
                ) {
                    Text(text = stringResource(R.string.start))
                }
                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}