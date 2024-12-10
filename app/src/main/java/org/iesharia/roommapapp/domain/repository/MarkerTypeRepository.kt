package org.iesharia.roommapapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.MarkerTypeModel

interface MarkerTypeRepository {
    fun getAllMarkerTypes(): Flow<List<MarkerTypeModel>>
    suspend fun getMarkerTypeById(typeId: Long): MarkerTypeModel?
    suspend fun addMarkerType(markerType: MarkerTypeModel)
    suspend fun updateMarkerType(markerType: MarkerTypeModel)
    suspend fun deleteMarkerType(typeId: Long)
}