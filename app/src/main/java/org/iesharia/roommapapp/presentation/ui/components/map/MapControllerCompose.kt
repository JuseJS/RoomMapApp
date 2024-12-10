package org.iesharia.roommapapp.presentation.ui.components.map

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import org.iesharia.roommapapp.R
import org.iesharia.roommapapp.domain.model.MapIconType
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay

class MapControllerCompose(
    private val context: Context,
    private val mapView: MapView
) {
    private var selectedMarker: MarkerModel? by mutableStateOf(null)
    private val tileProvider = MapTileProviderBasic(context)

    init {
        // Configurar el tile provider
        mapView.tileProvider = tileProvider
        mapView.overlayManager.tilesOverlay = TilesOverlay(tileProvider, context)
    }

    private fun getMarkerDrawable(typeId: Long): Drawable? {
        val iconType = MapIconType.fromTypeId(typeId)
        val resourceId = when (iconType) {
            MapIconType.RESTAURANT -> R.drawable.ic_marker_restaurant
            MapIconType.HOTEL -> R.drawable.ic_marker_hotel
            MapIconType.MONUMENT -> R.drawable.ic_marker_monument
            MapIconType.PARK -> R.drawable.ic_marker_park
            MapIconType.DEFAULT -> R.drawable.ic_marker_default
        }
        return ContextCompat.getDrawable(context, resourceId)
    }

    fun setCenter(latitude: Double, longitude: Double) {
        mapView.controller.setCenter(GeoPoint(latitude, longitude))
    }

    fun setZoom(zoom: Double) {
        mapView.controller.setZoom(zoom)
    }

    fun addMarker(marker: MarkerModel) {
        Marker(mapView).apply {
            id = marker.id
            position = GeoPoint(marker.latitude, marker.longitude)
            title = marker.title
            snippet = marker.description
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            // Establecer el icono correspondiente al tipo de marcador
            icon = getMarkerDrawable(marker.type.id)?.apply {
                val scaleFactor = 1.5f
                setBounds(
                    0,
                    0,
                    (intrinsicWidth * scaleFactor).toInt(),
                    (intrinsicHeight * scaleFactor).toInt()
                )
            }

            // Configurar comportamiento al seleccionar el marcador
            setOnMarkerClickListener { _, _ ->
                selectedMarker = marker
                true
            }

            mapView.overlays.add(this)
        }
        mapView.invalidate()
    }

    fun clearMarkers() {
        // Obtener el tiles overlay original
        val tilesOverlay = mapView.overlayManager.tilesOverlay

        // Limpiar los overlays pero mantener el mapa base
        mapView.overlays.clear()
        mapView.overlays.add(tilesOverlay)

        // Forzar el redibujado
        mapView.invalidate()
    }

    fun updateMarkers(markers: List<MarkerModel>) {
        try {
            clearMarkers()
            markers.forEach { addMarker(it) }
            Log.d("MapController", "Marcadores obtenidos: ${markers.size}")
        } catch (e: Exception) {
            Log.e("MapController", "Error al actualizar los marcadores: ", e)
        }
    }

    @Composable
    fun MarkerInfoWindow() {
        var currentMarker by remember { mutableStateOf<MarkerModel?>(null) }

        selectedMarker?.let { marker ->
            currentMarker = marker
            MarkerInfoDialog(
                marker = marker,
                onDismiss = { selectedMarker = null }
            )
        }
    }
}