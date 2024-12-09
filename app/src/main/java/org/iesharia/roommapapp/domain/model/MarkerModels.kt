package org.iesharia.roommapapp.domain.model

import org.iesharia.roommapapp.data.database.entity.MarkerEntity

/**
 * Modelo de dominio para representar un marcador en el mapa
 */
data class MarkerData(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String?,
    val type: MarkerType
)

/**
 * Tipos de marcadores disponibles en la aplicación
 */
enum class MarkerType {
    DEFAULT,
    RESTAURANT,
    HOTEL,
    MONUMENT,
    PARK;

    companion object {
        fun fromTypeId(typeId: Long): MarkerType {
            return when (typeId) {
                1L -> RESTAURANT
                2L -> HOTEL
                3L -> MONUMENT
                4L -> PARK
                else -> DEFAULT
            }
        }
    }
}

/**
 * Extensiones para mapear entre entidades y modelos de dominio
 */
fun MarkerEntity.toMarkerData(): MarkerData = MarkerData(
    id = id.toString(),
    latitude = latitude,
    longitude = longitude,    title = title,
    description = description,
    type = MarkerType.fromTypeId(typeId)
)

fun List<MarkerEntity>.toMarkerDataList(): List<MarkerData> =
    map { it.toMarkerData() }