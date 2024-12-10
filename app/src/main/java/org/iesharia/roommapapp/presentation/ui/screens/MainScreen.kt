package org.iesharia.roommapapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.iesharia.roommapapp.domain.util.Constants
import org.iesharia.roommapapp.presentation.model.MapEvent
import org.iesharia.roommapapp.presentation.ui.components.common.EmptyStateView
import org.iesharia.roommapapp.presentation.ui.components.common.ErrorDialog
import org.iesharia.roommapapp.presentation.ui.components.common.LoadingIndicator
import org.iesharia.roommapapp.presentation.ui.components.map.MapComponent
import org.iesharia.roommapapp.presentation.ui.components.map.MapConfig
import org.iesharia.roommapapp.presentation.ui.components.MapStyleDrawer
import org.iesharia.roommapapp.presentation.ui.components.MapTopAppBar
import org.iesharia.roommapapp.presentation.ui.components.map.MarkerInfoDialog
import org.iesharia.roommapapp.presentation.viewmodel.MapViewModel

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by mapViewModel.uiState.collectAsStateWithLifecycle()

    // Extraer contenido del drawer como función @Composable
    @Composable
    fun DrawerContent() {
        MapStyleDrawer(
            drawerState = drawerState,
            availableMaps = uiState.availableMaps,
            currentMapId = uiState.currentMap?.id,
            onMapSelected = { mapId ->
                scope.launch {
                    mapViewModel.handleEvent(MapEvent.OnMapSelected(mapId))
                    drawerState.close()
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        drawerContent = { DrawerContent() }
    ) {
        Scaffold(
            topBar = {
                MapTopAppBar(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.markers.isEmpty() && !uiState.isLoading) {
                    EmptyStateView(
                        icon = Icons.Default.LocationOff,
                        title = "No hay marcadores",
                        message = "No se encontraron marcadores en este mapa"
                    )
                }

                MapComponent(
                    modifier = Modifier.fillMaxSize(),
                    config = MapConfig(
                        initialLatitude = uiState.currentMap?.initialLatitude
                            ?: Constants.Map.DEFAULT_LATITUDE,
                        initialLongitude = uiState.currentMap?.initialLongitude
                            ?: Constants.Map.DEFAULT_LONGITUDE,
                        initialZoom = uiState.currentMap?.initialZoom?.toDouble()
                            ?: Constants.Map.DEFAULT_ZOOM
                    ),
                    markers = uiState.markers,
                    onMarkerClick = { marker ->
                        mapViewModel.handleEvent(MapEvent.OnMarkerSelected(marker))
                    }
                )

                if (uiState.isLoading) {
                    LoadingIndicator()
                }

                uiState.error?.let { error ->
                    ErrorDialog(
                        message = error,
                        onDismiss = {
                            mapViewModel.handleEvent(MapEvent.OnErrorDismissed)
                        }
                    )
                }

                uiState.selectedMarker?.let { marker ->
                    MarkerInfoDialog(
                        marker = marker,
                        onDismiss = {
                            mapViewModel.handleEvent(MapEvent.OnMarkerSelected(null))
                        }
                    )
                }
            }
        }
    }
}
