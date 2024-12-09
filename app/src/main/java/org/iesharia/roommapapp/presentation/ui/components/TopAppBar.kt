package org.iesharia.roommapapp.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopAppBar(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text("Mapas") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = onThemeChange) {
                Icon(
                    imageVector = if (isDarkTheme) {
                        Icons.Default.LightMode
                    } else {
                        Icons.Default.DarkMode
                    },
                    contentDescription = if (isDarkTheme) {
                        "Cambiar a modo claro"
                    } else {
                        "Cambiar a modo noche"
                    }
                )
            }
        }
    )
}