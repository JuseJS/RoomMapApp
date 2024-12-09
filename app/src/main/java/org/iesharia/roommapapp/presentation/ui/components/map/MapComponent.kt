package org.iesharia.roommapapp.presentation.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.iesharia.roommapapp.domain.model.MarkerData

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    config: MapConfig = MapConfig(),
    markers: List<MarkerData> = emptyList(),
    onMarkerClick: (MarkerData) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapConfiguration.initialize(context)
        MapConfiguration.createMapView(context, config)
    }

    val mapController = remember {
        MapControllerCompose(context, mapView)
    }

    LaunchedEffect(markers) {
        mapController.updateMarkers(markers)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
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

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )

    mapController.MarkerInfoWindow()
}