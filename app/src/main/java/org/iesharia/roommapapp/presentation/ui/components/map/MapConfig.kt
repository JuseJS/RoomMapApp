package org.iesharia.roommapapp.presentation.ui.components.map

import android.content.Context
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

data class MapConfig(
    val initialLatitude: Double = 40.416775,
    val initialLongitude: Double = -3.703790,
    val initialZoom: Double = 16.0,
    val isMultiTouchEnabled: Boolean = true
)

object MapConfiguration {
    fun initialize(context: Context) {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
            load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        }
    }

    fun createMapView(context: Context, config: MapConfig): MapView {
        return MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            isTilesScaledToDpi = true
            setMultiTouchControls(config.isMultiTouchEnabled)
            controller.setZoom(config.initialZoom)
            controller.setCenter(org.osmdroid.util.GeoPoint(
                config.initialLatitude,
                config.initialLongitude
            ))
        }
    }
}