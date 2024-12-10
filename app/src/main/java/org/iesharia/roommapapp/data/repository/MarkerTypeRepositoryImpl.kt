package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.data.mapper.Mappers
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerTypeRepositoryImpl @Inject constructor(
    private val markerTypeDao: MarkerTypeDao,
    private val mappers: Mappers
) : MarkerTypeRepository {
    override fun getAllMarkerTypes(): Flow<Result<List<MarkerTypeModel>>> = flow {
        emit(Result.Loading)
        try {
            markerTypeDao.getAllMarkerTypes()
                .map { types -> types.map { mappers.run { it.toModel() } } }
                .collect { emit(Result.Success(it)) }
        } catch (e: Exception) {
            emit(Result.Error(e.toAppError()))
        }
    }

    override suspend fun getMarkerTypeById(typeId: Long): Result<MarkerTypeModel> = try {
        val markerType = markerTypeDao.getMarkerTypeById(typeId)
            ?: throw AppError.DatabaseError("MarkerType not found for id: $typeId")
        Result.Success(mappers.run { markerType.toModel() })
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun addMarkerType(markerType: MarkerTypeModel): Result<Unit> = try {
        markerTypeDao.insertMarkerType(mappers.run { markerType.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun updateMarkerType(markerType: MarkerTypeModel): Result<Unit> = try {
        markerTypeDao.updateMarkerType(mappers.run { markerType.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun deleteMarkerType(typeId: Long): Result<Unit> = try {
        val markerType = markerTypeDao.getMarkerTypeById(typeId)
            ?: throw AppError.DatabaseError("MarkerType not found for id: $typeId")
        markerTypeDao.deleteMarkerType(markerType)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }
}