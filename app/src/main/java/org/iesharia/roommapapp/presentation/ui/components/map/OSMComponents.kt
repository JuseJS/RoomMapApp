package org.iesharia.roommapapp.presentation.ui.components.map

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.iesharia.roommapapp.domain.model.MarkerData
import org.iesharia.roommapapp.domain.model.MarkerType

class OSMController(private val mapView: MapView) : MapController {
    override fun setCenter(latitude: Double, longitude: Double) {
        mapView.controller.setCenter(GeoPoint(latitude, longitude))
    }

    override fun setZoom(zoom: Double) {
        mapView.controller.setZoom(zoom)
    }

    override fun addMarker(marker: MarkerData) {
        Marker(mapView).apply {
            id = marker.id
            position = GeoPoint(marker.latitude, marker.longitude)
            title = marker.title
            snippet = marker.description
            icon = getMarkerIcon(marker.type)
            mapView.overlays.add(this)
        }
        mapView.invalidate()
    }

    override fun clearMarkers() {
        mapView.overlays.clear()
        mapView.invalidate()
    }

    override fun updateMarkers(markers: List<MarkerData>) {
        clearMarkers()
        markers.forEach { addMarker(it) }
    }

    private fun getMarkerIcon(type: MarkerType): org.osmdroid.views.overlay.Marker.PLACEMARKS_MARKERS {
        return when (type) {
            MarkerType.RESTAURANT -> org.osmdroid.views.overlay.Marker.PLACEMARKS_MARKERS.RESTAURANT
            MarkerType.HOTEL -> org.osmdroid.views.overlay.Marker.PLACEMARKS_MARKERS.HOTEL
            MarkerType.MONUMENT -> org.osmdroid.views.overlay.Marker.PLACEMARKS_MARKERS.MONUMENT
            MarkerType.PARK -> org.osmdroid.views.overlay.Marker.PLACEMARKS_MARKERS.PARK
            MarkerType.DEFAULT -> org.osmdroid.views.overlay.Marker.PLACEMARKS_MARKERS.DEFAULT
        }
    }
}

interface MapController {
    fun setCenter(latitude: Double, longitude: Double)
    fun setZoom(zoom: Double)
    fun addMarker(marker: MarkerData)
    fun clearMarkers()
    fun updateMarkers(markers: List<MarkerData>)
}