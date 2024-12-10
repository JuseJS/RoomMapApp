package org.iesharia.roommapapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.util.Result

interface MarkerRepository {
    fun getMarkersByMapId(mapId: Long): Flow<List<MarkerModel>>
    fun getMarkersByType(typeId: Long): Flow<List<MarkerModel>>
    fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<List<MarkerModel>>
    suspend fun addMarker(marker: MarkerModel)
    suspend fun updateMarker(marker: MarkerModel)
    suspend fun deleteMarker(markerId: String)
}