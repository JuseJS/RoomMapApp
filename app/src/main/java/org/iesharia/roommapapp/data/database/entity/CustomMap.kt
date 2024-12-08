package org.iesharia.roommapapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_maps")
data class CustomMap(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val styleJson: String,
    val initialLatitude: Double,
    val initialLongitude: Double,
    val initialZoom: Float,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)