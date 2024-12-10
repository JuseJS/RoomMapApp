package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.data.database.dao.CustomMapDao
import org.iesharia.roommapapp.data.mapper.CustomMapMapper
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomMapRepositoryImpl @Inject constructor(
    private val customMapDao: CustomMapDao,
    private val customMapMapper: CustomMapMapper
) : CustomMapRepository {

    override fun getAllCustomMaps(): Flow<List<CustomMapModel>> {
        return customMapDao.getAllCustomMaps().map { maps ->
            maps.map { customMapMapper.toModel(it) }
        }
    }

    override suspend fun getDefaultMap(): CustomMapModel? {
        return customMapDao.getDefaultMap()?.let { customMapMapper.toModel(it) }
    }

    override suspend fun getCustomMapById(mapId: Long): CustomMapModel? {
        return customMapDao.getCustomMapById(mapId)?.let { customMapMapper.toModel(it) }
    }

    override suspend fun addCustomMap(customMap: CustomMapModel) {
        customMapDao.insertCustomMap(customMapMapper.toEntity(customMap))
    }

    override suspend fun updateCustomMap(customMap: CustomMapModel) {
        customMapDao.updateCustomMap(customMapMapper.toEntity(customMap))
    }

    override suspend fun deleteCustomMap(mapId: Long) {
        customMapDao.getCustomMapById(mapId)?.let { map ->
            customMapDao.deleteCustomMap(map)
        }
    }

    override suspend fun setDefaultMap(mapId: Long) {
        customMapDao.setDefaultMap(mapId)
    }
}