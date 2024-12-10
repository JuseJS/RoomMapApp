package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.data.mapper.MarkerTypeMapper
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerTypeRepositoryImpl @Inject constructor(
    private val markerTypeDao: MarkerTypeDao,
    private val markerTypeMapper: MarkerTypeMapper
) : MarkerTypeRepository {

    override fun getAllMarkerTypes(): Flow<List<MarkerTypeModel>> {
        return markerTypeDao.getAllMarkerTypes().map { types ->
            types.map { markerTypeMapper.toModel(it) }
        }
    }

    override suspend fun getMarkerTypeById(typeId: Long): MarkerTypeModel? {
        return markerTypeDao.getMarkerTypeById(typeId)?.let { markerTypeMapper.toModel(it) }
    }

    override suspend fun addMarkerType(markerType: MarkerTypeModel) {
        markerTypeDao.insertMarkerType(markerTypeMapper.toEntity(markerType))
    }

    override suspend fun updateMarkerType(markerType: MarkerTypeModel) {
        markerTypeDao.updateMarkerType(markerTypeMapper.toEntity(markerType))
    }

    override suspend fun deleteMarkerType(typeId: Long) {
        markerTypeDao.getMarkerTypeById(typeId)?.let { type ->
            markerTypeDao.deleteMarkerType(type)
        }
    }
}