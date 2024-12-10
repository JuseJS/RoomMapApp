package org.iesharia.roommapapp.presentation.ui.components.map

import android.content.Context
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay

data class MapConfig(
    val initialLatitude: Double = 40.416775,
    val initialLongitude: Double = -3.703790,
    val initialZoom: Double = 16.0,
    val isMultiTouchEnabled: Boolean = true,
    val isDarkTheme: Boolean = false
)

object MapConfiguration {
    fun initialize(context: Context) {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
            load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
            tileFileSystemCacheMaxBytes = 100L * 1024 * 1024
            tileFileSystemCacheTrimBytes = 80L * 1024 * 1024
            cacheMapTileCount = 12
            expirationOverrideDuration = 1000L * 60 * 60 * 24 * 7
        }
    }

    fun createMapView(context: Context, config: MapConfig): MapView {
        // Limpiar la caché al crear el mapa con un nuevo tema
        Configuration.getInstance().osmdroidTileCache.delete()

        return MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            isTilesScaledToDpi = true
            setMultiTouchControls(config.isMultiTouchEnabled)
            setUseDataConnection(true)

            // Configurar el overlay de tiles con el tema apropiado
            overlayManager.tilesOverlay.apply {
                if (config.isDarkTheme) {
                    // Modo nocturno
                    setColorFilter(TilesOverlay.INVERT_COLORS)
                } else {
                    // Modo normal
                    setColorFilter(null)
                }
            }

            // Forzar la recarga de tiles al cambiar el tema
            tileProvider.clearTileCache()

            // Configuraciones básicas
            maxZoomLevel = 19.0
            minZoomLevel = 4.0
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