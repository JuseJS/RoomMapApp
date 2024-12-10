package org.iesharia.roommapapp.domain.model

data class MarkerModel(
    val id: String,
    val title: String,
    val type: MarkerTypeModel,
    val mapId: Long,
    val latitude: Double,
    val longitude: Double,
    val description: String?
)