package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.data.database.dao.MarkerDao
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.data.mapper.MarkerMapper
import org.iesharia.roommapapp.data.mapper.MarkerTypeMapper
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerRepositoryImpl @Inject constructor(
    private val markerDao: MarkerDao,
    private val markerTypeDao: MarkerTypeDao,
    private val markerMapper: MarkerMapper,
    private val markerTypeMapper: MarkerTypeMapper
) : MarkerRepository {

    override fun getMarkersByMapId(mapId: Long): Flow<List<MarkerModel>> {
        return markerDao.getMarkersByMapId(mapId).map { markers ->
            markers.map { marker ->
                val markerType = markerTypeDao.getMarkerTypeById(marker.typeId)
                    ?: throw IllegalStateException("MarkerType not found for id: ${marker.typeId}")
                markerMapper.toModel(marker, markerTypeMapper.toModel(markerType))
            }
        }
    }

    override fun getMarkersByType(typeId: Long): Flow<List<MarkerModel>> {
        return markerDao.getMarkersByTypeId(typeId).map { markers ->
            markers.map { marker ->
                val markerType = markerTypeDao.getMarkerTypeById(marker.typeId)
                    ?: throw IllegalStateException("MarkerType not found for id: ${marker.typeId}")
                markerMapper.toModel(marker, markerTypeMapper.toModel(markerType))
            }
        }
    }

    override fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<List<MarkerModel>> {
        return markerDao.getMarkersInBoundingBox(minLat, maxLat, minLon, maxLon).map { markers ->
            markers.map { marker ->
                val markerType = markerTypeDao.getMarkerTypeById(marker.typeId)
                    ?: throw IllegalStateException("MarkerType not found for id: ${marker.typeId}")
                markerMapper.toModel(marker, markerTypeMapper.toModel(markerType))
            }
        }
    }

    override suspend fun addMarker(marker: MarkerModel) {
        markerDao.insertMarker(markerMapper.toEntity(marker))
    }

    override suspend fun updateMarker(marker: MarkerModel) {
        markerDao.updateMarker(markerMapper.toEntity(marker))
    }

    override suspend fun deleteMarker(markerId: String) {
        markerDao.getMarkerById(markerId.toLong())?.let { marker ->
            markerDao.deleteMarker(marker)
        }
    }
}