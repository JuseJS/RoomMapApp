package org.iesharia.roommapapp.presentation.ui.components.map

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.osmdroid.util.GeoPoint

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    config: MapConfig,
    markers: List<MarkerModel> = emptyList(),
    onMarkerClick: (MarkerModel) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Inicializar OSMDroid
    DisposableEffect(Unit) {
        MapConfiguration.initialize(context)
        onDispose { }
    }

    // Crear y configurar el MapView
    val mapView = remember(config.isDarkTheme) {
        MapConfiguration.createMapView(context, config)
    }

    val mapController = remember(mapView) {
        MapControllerCompose(context, mapView)
    }

    // Manejar los marcadores
    LaunchedEffect(markers) {
        mapController.updateMarkers(markers)
    }

    // Gestionar el ciclo de vida
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    mapView.controller.apply {
                        setCenter(GeoPoint(config.initialLatitude, config.initialLongitude))
                        setZoom(config.initialZoom)
                    }
                }
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    key(config.isDarkTheme) {
        AndroidView(
            factory = { mapView },
            modifier = modifier,
            update = { view ->
                view.controller.setCenter(GeoPoint(config.initialLatitude, config.initialLongitude))
                view.controller.setZoom(config.initialZoom)
            }
        )
    }

    // Mostrar ventana de información de marcador
    mapController.MarkerInfoWindow()
}