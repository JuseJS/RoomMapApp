package org.iesharia.roommapapp.presentation.ui.components.map

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
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
            tileFileSystemCacheMaxBytes = 100L * 1024 * 1024
            tileFileSystemCacheTrimBytes = 80L * 1024 * 1024
            cacheMapTileCount = 12
        }
    }

    fun createMapView(context: Context, config: MapConfig): MapView {
        return MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            isTilesScaledToDpi = true
            setMultiTouchControls(config.isMultiTouchEnabled)

            // Deshabilitar POIs y etiquetas usando un filtro de color
            overlayManager.tilesOverlay.setColorFilter(
                PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC)
            )

            // Configuraciones básicas
            isHorizontalMapRepetitionEnabled = false
            isVerticalMapRepetitionEnabled = false

            // Establecer la posición inicial
            controller.setZoom(config.initialZoom)
            controller.setCenter(GeoPoint(
                config.initialLatitude,
                config.initialLongitude
            ))
        }
    }
}
