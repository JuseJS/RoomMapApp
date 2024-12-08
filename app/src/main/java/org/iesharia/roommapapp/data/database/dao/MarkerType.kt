package org.iesharia.roommapapp.data.database.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marker_types")
data class MarkerType(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    val color: String,
    val description: String? = null,
    val isVisible: Boolean = true
)