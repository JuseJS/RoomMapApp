package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.data.database.dao.CustomMapDao
import org.iesharia.roommapapp.data.database.entity.CustomMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomMapRepository @Inject constructor(
    private val customMapDao: CustomMapDao
) {
    fun getAllCustomMaps(): Flow<List<CustomMap>> = customMapDao.getAllCustomMaps()

    suspend fun getDefaultMap(): CustomMap? = customMapDao.getDefaultMap()

    suspend fun insertCustomMap(customMap: CustomMap) =
        customMapDao.insertCustomMap(customMap)

    suspend fun updateCustomMap(customMap: CustomMap) =
        customMapDao.updateCustomMap(customMap)

    suspend fun deleteCustomMap(customMap: CustomMap) =
        customMapDao.deleteCustomMap(customMap)

    suspend fun setDefaultMap(mapId: Long) = customMapDao.setDefaultMap(mapId)
}