package org.iesharia.roommapapp.domain.model

enum class MapIconType {
    DEFAULT,
    RESTAURANT,
    HOTEL,
    MONUMENT,
    PARK;

    companion object {
        fun fromTypeId(typeId: Long): MapIconType {
            return when (typeId) {
                1L -> RESTAURANT
                2L -> HOTEL
                3L -> MONUMENT
                4L -> PARK
                else -> DEFAULT
            }
        }

        fun getIconName(type: MapIconType): String {
            return when (type) {
                DEFAULT -> "ic_marker_default"
                RESTAURANT -> "ic_marker_restaurant"
                HOTEL -> "ic_marker_hotel"
                MONUMENT -> "ic_marker_monument"
                PARK -> "ic_marker_park"
            }
        }

        fun getDefaultColor(type: MapIconType): String {
            return when (type) {
                DEFAULT -> "#FF0000"     // Rojo
                RESTAURANT -> "#4CAF50"  // Verde
                HOTEL -> "#2196F3"       // Azul
                MONUMENT -> "#9C27B0"    // Morado
                PARK -> "#8BC34A"        // Verde claro
            }
        }
    }
}