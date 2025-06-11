package com.example.moviefinder.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moviefinder.R
import com.example.moviefinder.ui.viewmodels.AppViewModel
import androidx.core.content.edit

@Composable
fun WelcomeScreen(appViewModel: AppViewModel, navController: NavController) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(id = R.drawable.background_1),
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box {
            Card {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.welcome),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = appViewModel.apiKey, onValueChange = {
                            appViewModel.updateApiKey(it)
                        },
                        label = { Text(text = stringResource(R.string.api_key)) },
                        trailingIcon = {
                            IconButton(onClick = {
                                appViewModel.showAlert(
                                    context.getString(R.string.api_key),
                                    context.getString(R.string.enter_api_key)
                                )
                            }) {
                                Icon(imageVector = Icons.Outlined.Info, null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !appViewModel.requestInProgress,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {
                        if (appViewModel.apiKey.isEmpty()) {
                            appViewModel.showAlert(
                                context.getString(R.string.api_key),
                                context.getString(R.string.enter_api_key)
                            )
                            return@Button
                        }
                        appViewModel.validateApiKey(onResult = { isValid ->
                            if (isValid == null) {
                                appViewModel.showAlert(
                                    context.getString(R.string.error),
                                    context.getString(R.string.api_key_validation_error)
                                )
                            } else {
                                if (isValid) {
                                    context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                                        .edit {
                                            putString("api_key", appViewModel.apiKey)
                                        }
                                    navController.navigate(AppScreens.MAIN_SCREEN) {
                                        popUpTo(AppScreens.WELCOME_SCREEN) { inclusive = true }
                                    }
                                } else {
                                    appViewModel.showAlert(
                                        context.getString(R.string.error),
                                        context.getString(R.string.invalid_api_key)
                                    )
                                }
                            }
                        })
                    }, enabled = !appViewModel.requestInProgress) {
                        Text(text = stringResource(R.string.continue_text))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(modifier = Modifier.alpha(if (appViewModel.requestInProgress) 1f else 0f))
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
fun WelcomeScreenPreview() {
    val appViewModel = AppViewModel()

    WelcomeScreen(appViewModel, rememberNavController())
}
