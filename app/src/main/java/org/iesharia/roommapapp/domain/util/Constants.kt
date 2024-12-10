package org.iesharia.roommapapp.domain.util

object Constants {
    // Map Configuration
    object Map {
        const val DEFAULT_ZOOM = 15.0
        const val MIN_ZOOM = 5.0
        const val MAX_ZOOM = 20.0
        const val DEFAULT_LATITUDE = 29.143461  // Haría, Lanzarote
        const val DEFAULT_LONGITUDE = -13.497764 // Haría, Lanzarote

        // Tile Cache
        const val CACHE_SIZE_MB = 100  // Tamaño máximo de la caché en MB
        const val CACHE_TRIM_SIZE_MB = 80  // Tamaño al que se reduce la caché cuando se excede el máximo

        // Map Update Intervals
        const val LOCATION_UPDATE_INTERVAL = 5000L // 5 segundos
        const val MARKER_UPDATE_THRESHOLD = 1000L // 1 segundo
    }

    // Database
    object Database {
        const val DATABASE_NAME = "app_database"
        const val DATABASE_VERSION = 1
    }

    // UI Related
    object UI {
        const val ANIMATION_DURATION = 300L
        const val MIN_SPACING_DP = 8
        const val DEFAULT_SPACING_DP = 16
        const val LARGE_SPACING_DP = 24
    }

    // Permissions
    object Permissions {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val STORAGE_PERMISSION_REQUEST_CODE = 1002
    }

    // Error Messages
    object ErrorMessages {
        const val NETWORK_ERROR = "Error de conexión. Por favor, verifica tu conexión a internet."
        const val LOCATION_ERROR = "No se pudo obtener la ubicación actual."
        const val DATABASE_ERROR = "Error al acceder a los datos."
        const val PERMISSION_DENIED = "Permisos necesarios no concedidos."
        const val MARKER_NOT_FOUND = "Marcador no encontrado."
        const val MAP_LOAD_ERROR = "Error al cargar el mapa."
        const val STORAGE_PERMISSION_ERROR = "No se pudo acceder al almacenamiento. Verifica los permisos de la aplicación."
        const val TILE_CACHE_ERROR = "Error al cargar los tiles del mapa. Verifica tu conexión a internet."
    }
}