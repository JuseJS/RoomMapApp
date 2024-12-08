package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.data.database.entity.MarkerType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerTypeRepository @Inject constructor(
    private val markerTypeDao: MarkerTypeDao
) {
    fun getAllMarkerTypes(): Flow<List<MarkerType>> = markerTypeDao.getAllMarkerTypes()

    suspend fun getMarkerTypeById(typeId: Long): MarkerType? =
        markerTypeDao.getMarkerTypeById(typeId)

    suspend fun insertMarkerType(markerType: MarkerType) =
        markerTypeDao.insertMarkerType(markerType)

    suspend fun updateMarkerType(markerType: MarkerType) =
        markerTypeDao.updateMarkerType(markerType)

    suspend fun deleteMarkerType(markerType: MarkerType) =
        markerTypeDao.deleteMarkerType(markerType)
}