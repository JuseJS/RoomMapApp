package org.iesharia.roommapapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.util.Result

interface CustomMapRepository {
    fun getAllCustomMaps(): Flow<Result<List<CustomMapModel>>>
    suspend fun getDefaultMap(): Result<CustomMapModel>
    suspend fun getCustomMapById(mapId: Long): Result<CustomMapModel>
    suspend fun addCustomMap(customMap: CustomMapModel): Result<Unit>
    suspend fun updateCustomMap(customMap: CustomMapModel): Result<Unit>
    suspend fun deleteCustomMap(mapId: Long): Result<Unit>
    suspend fun setDefaultMap(mapId: Long): Result<Unit>
}