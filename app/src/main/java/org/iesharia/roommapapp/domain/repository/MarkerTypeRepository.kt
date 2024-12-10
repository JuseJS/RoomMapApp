package org.iesharia.roommapapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.util.Result

interface MarkerTypeRepository {
    fun getAllMarkerTypes(): Flow<Result<List<MarkerTypeModel>>>
    suspend fun getMarkerTypeById(typeId: Long): Result<MarkerTypeModel>
    suspend fun addMarkerType(markerType: MarkerTypeModel): Result<Unit>
    suspend fun updateMarkerType(markerType: MarkerTypeModel): Result<Unit>
    suspend fun deleteMarkerType(typeId: Long): Result<Unit>
}