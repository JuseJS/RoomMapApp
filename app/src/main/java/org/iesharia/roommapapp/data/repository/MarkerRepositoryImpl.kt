package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.data.database.dao.MarkerDao
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.data.mapper.Mappers
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerRepositoryImpl @Inject constructor(
    private val markerDao: MarkerDao,
    private val markerTypeDao: MarkerTypeDao,
    private val mappers: Mappers
) : MarkerRepository {
    override fun getMarkersByMapId(mapId: Long): Flow<Result<List<MarkerModel>>> = flow {
        emit(Result.Loading)
        try {
            markerDao.getMarkersByMapId(mapId)
                .map { markers ->
                    markers.map { marker ->
                        val markerType = markerTypeDao.getMarkerTypeById(marker.typeId)
                            ?: throw AppError.DatabaseError("MarkerType not found for id: ${marker.typeId}")
                        mappers.run { marker.toModel(mappers.run { markerType.toModel() }) }
                    }
                }
                .collect { emit(Result.Success(it)) }
        } catch (e: Exception) {
            emit(Result.Error(e.toAppError()))
        }
    }

    override fun getMarkersByType(typeId: Long): Flow<Result<List<MarkerModel>>> = flow {
        emit(Result.Loading)
        try {
            markerDao.getMarkersByTypeId(typeId)
                .map { markers ->
                    markers.map { marker ->
                        val markerType = markerTypeDao.getMarkerTypeById(marker.typeId)
                            ?: throw AppError.DatabaseError("MarkerType not found for id: ${marker.typeId}")
                        mappers.run { marker.toModel(mappers.run { markerType.toModel() }) }
                    }
                }
                .collect { emit(Result.Success(it)) }
        } catch (e: Exception) {
            emit(Result.Error(e.toAppError()))
        }
    }

    override fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<Result<List<MarkerModel>>> = flow {
        emit(Result.Loading)
        try {
            if (minLat > maxLat || minLon > maxLon) {
                throw AppError.ValidationError("Invalid bounding box coordinates")
            }
            markerDao.getMarkersInBoundingBox(minLat, maxLat, minLon, maxLon)
                .map { markers ->
                    markers.map { marker ->
                        val markerType = markerTypeDao.getMarkerTypeById(marker.typeId)
                            ?: throw AppError.DatabaseError("MarkerType not found for id: ${marker.typeId}")
                        mappers.run { marker.toModel(mappers.run { markerType.toModel() }) }
                    }
                }
                .collect { emit(Result.Success(it)) }
        } catch (e: Exception) {
            emit(Result.Error(e.toAppError()))
        }
    }

    override suspend fun addMarker(marker: MarkerModel): Result<Unit> = try {
        markerDao.insertMarker(mappers.run { marker.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun updateMarker(marker: MarkerModel): Result<Unit> = try {
        markerDao.updateMarker(mappers.run { marker.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun deleteMarker(markerId: String): Result<Unit> = try {
        val markerId = markerId.toLongOrNull()
            ?: throw AppError.ValidationError("Invalid marker ID format")

        val marker = markerDao.getMarkerById(markerId)
            ?: throw AppError.DatabaseError("Marker not found with id: $markerId")

        markerDao.deleteMarker(marker)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }
}