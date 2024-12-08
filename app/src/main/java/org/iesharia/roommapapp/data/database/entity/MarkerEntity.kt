package org.iesharia.roommapapp.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "markers",
    foreignKeys = [
        ForeignKey(
            entity = MarkerType::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CustomMap::class,
            parentColumns = ["id"],
            childColumns = ["mapId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("typeId"),
        Index("mapId")
    ]
)
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val typeId: Long,
    val mapId: Long,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)