package org.iesharia.roommapapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.iesharia.roommapapp.presentation.ui.components.MapStyleDrawer
import org.iesharia.roommapapp.presentation.ui.components.MapTopAppBar
import org.iesharia.roommapapp.presentation.viewmodel.CustomMapViewModel

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    customMapViewModel: CustomMapViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentMap by customMapViewModel.currentMap.collectAsState()
    val availableMaps by customMapViewModel.availableMaps.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MapStyleDrawer(
                drawerState = drawerState,
                availableMaps = availableMaps,
                currentMapId = currentMap?.id,
                onMapSelected = { map ->
                    scope.launch {
                        customMapViewModel.setCurrentMap(map.id)
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                MapTopAppBar(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Aquí irá el componente del mapa
            }
        }
    }
}