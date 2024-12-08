package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.data.database.dao.MarkerDao
import org.iesharia.roommapapp.data.database.entity.MarkerEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerRepository @Inject constructor(
    private val markerDao: MarkerDao
) {
    fun getAllMarkers(): Flow<List<MarkerEntity>> = markerDao.getAllMarkers()

    fun getMarkersByMapId(mapId: Long): Flow<List<MarkerEntity>> =
        markerDao.getMarkersByMapId(mapId)

    fun getMarkersByTypeId(typeId: Long): Flow<List<MarkerEntity>> =
        markerDao.getMarkersByTypeId(typeId)

    fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<List<MarkerEntity>> = markerDao.getMarkersInBoundingBox(minLat, maxLat, minLon, maxLon)

    suspend fun insertMarker(marker: MarkerEntity): Long = markerDao.insertMarker(marker)

    suspend fun updateMarker(marker: MarkerEntity) = markerDao.updateMarker(marker)

    suspend fun deleteMarker(marker: MarkerEntity) = markerDao.deleteMarker(marker)
}