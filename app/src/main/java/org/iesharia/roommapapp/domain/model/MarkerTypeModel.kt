package org.iesharia.roommapapp.domain.model

data class MarkerTypeModel(
    val id: Long,
    val name: String,
    val icon: String,
    val color: String,
    val description: String?,
    val isVisible: Boolean
)