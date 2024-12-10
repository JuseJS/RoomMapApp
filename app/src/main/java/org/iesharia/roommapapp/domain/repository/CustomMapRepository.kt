package org.iesharia.roommapapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.CustomMapModel

interface CustomMapRepository {
    fun getAllCustomMaps(): Flow<List<CustomMapModel>>
    suspend fun getDefaultMap(): CustomMapModel?
    suspend fun getCustomMapById(mapId: Long): CustomMapModel?
    suspend fun addCustomMap(customMap: CustomMapModel)
    suspend fun updateCustomMap(customMap: CustomMapModel)
    suspend fun deleteCustomMap(mapId: Long)
    suspend fun setDefaultMap(mapId: Long)
}