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
import org.iesharia.roommapapp.domain.model.MarkerData
import org.iesharia.roommapapp.domain.model.MarkerType
import org.iesharia.roommapapp.presentation.ui.components.map.MapComponent
import org.iesharia.roommapapp.presentation.ui.components.map.MapConfig
import org.iesharia.roommapapp.presentation.ui.components.MapStyleDrawer
import org.iesharia.roommapapp.presentation.ui.components.MapTopAppBar
import org.iesharia.roommapapp.presentation.viewmodel.CustomMapViewModel
import org.iesharia.roommapapp.presentation.viewmodel.MarkerViewModel

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    customMapViewModel: CustomMapViewModel = hiltViewModel(),
    markerViewModel: MarkerViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentMap by customMapViewModel.currentMap.collectAsState()
    val availableMaps by customMapViewModel.availableMaps.collectAsState()
    val markers by markerViewModel.markers.collectAsState()

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
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MapComponent(
                    modifier = Modifier.fillMaxSize(),
                    config = MapConfig(
                        initialLatitude = currentMap?.initialLatitude ?: 40.416775,
                        initialLongitude = currentMap?.initialLongitude ?: -3.703790,
                        initialZoom = currentMap?.initialZoom?.toDouble() ?: 16.0
                    ),
                    markers = markers.map {
                        MarkerData(
                            id = it.id.toString(),
                            latitude = it.latitude,
                            longitude = it.longitude,
                            title = it.title,
                            description = it.description,
                            type = MarkerType.fromTypeId(it.typeId)
                        )
                    },
                    onMarkerClick = { markerData ->
                        markerViewModel.selectMarker(
                            markers.find { it.id.toString() == markerData.id }
                        )
                    }
                )
            }
        }
    }
}