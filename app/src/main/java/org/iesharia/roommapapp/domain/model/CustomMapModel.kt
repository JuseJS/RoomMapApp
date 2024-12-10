package org.iesharia.roommapapp.domain.model

data class CustomMapModel(
    val id: Long,
    val name: String,
    val styleJson: String,
    val initialLatitude: Double,
    val initialLongitude: Double,
    val initialZoom: Float,
    val isDefault: Boolean
)