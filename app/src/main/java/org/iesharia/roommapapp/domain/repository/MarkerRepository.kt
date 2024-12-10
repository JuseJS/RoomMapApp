package org.iesharia.roommapapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.util.Result

interface MarkerRepository {
    fun getMarkersByMapId(mapId: Long): Flow<Result<List<MarkerModel>>>
    fun getMarkersByType(typeId: Long): Flow<Result<List<MarkerModel>>>
    fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<Result<List<MarkerModel>>>
    suspend fun addMarker(marker: MarkerModel): Result<Unit>
    suspend fun updateMarker(marker: MarkerModel): Result<Unit>
    suspend fun deleteMarker(markerId: String): Result<Unit>
}