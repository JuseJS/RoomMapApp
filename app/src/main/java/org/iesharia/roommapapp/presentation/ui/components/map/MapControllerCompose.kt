package org.iesharia.roommapapp.presentation.ui.components.map

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.iesharia.roommapapp.domain.model.MarkerData
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapControllerCompose(
    private val context: Context,
    private val mapView: MapView
) {
    private var selectedMarker: MarkerData? by mutableStateOf(null)

    fun setCenter(latitude: Double, longitude: Double) {
        mapView.controller.setCenter(GeoPoint(latitude, longitude))
    }

    fun setZoom(zoom: Double) {
        mapView.controller.setZoom(zoom)
    }

    fun addMarker(marker: MarkerData) {
        Marker(mapView).apply {
            id = marker.id
            position = GeoPoint(marker.latitude, marker.longitude)
            title = marker.title
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            setOnMarkerClickListener { _, _ ->
                selectedMarker = marker
                true
            }

            mapView.overlays.add(this)
        }
        mapView.invalidate()
    }

    fun clearMarkers() {
        mapView.overlays.clear()
        mapView.invalidate()
    }

    fun updateMarkers(markers: List<MarkerData>) {
        clearMarkers()
        markers.forEach { addMarker(it) }
    }

    @Composable
    fun MarkerInfoWindow() {
        var currentMarker by remember { mutableStateOf<MarkerData?>(null) }

        selectedMarker?.let { marker ->
            currentMarker = marker
            MarkerInfoDialog(
                markerData = marker,
                onDismiss = { selectedMarker = null }
            )
        }
    }
}