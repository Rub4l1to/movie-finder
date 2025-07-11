package com.example.moviefinder.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.moviefinder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, showBack: Boolean = false, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = if (showBack && onBack != null) {
            {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        } else {
            {}
        }
    )
}