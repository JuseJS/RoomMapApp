package org.iesharia.roommapapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.iesharia.roommapapp.data.database.dao.CustomMapDao
import org.iesharia.roommapapp.data.mapper.Mappers
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomMapRepositoryImpl @Inject constructor(
    private val customMapDao: CustomMapDao,
    private val mappers: Mappers
) : CustomMapRepository {
    override fun getAllCustomMaps(): Flow<Result<List<CustomMapModel>>> {
        return customMapDao.getAllCustomMaps()
            .map { maps -> maps.map { mappers.run { it.toModel() } } }
            .map<List<CustomMapModel>, Result<List<CustomMapModel>>> { Result.Success(it) }
            .onStart { emit(Result.Loading) }
            .catch { e -> emit(Result.Error(e.toAppError())) }
    }

    override suspend fun getDefaultMap(): Result<CustomMapModel> = try {
        val defaultMap = customMapDao.getDefaultMap()
            ?: throw AppError.DatabaseError("No default map found")
        Result.Success(mappers.run { defaultMap.toModel() })
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun getCustomMapById(mapId: Long): Result<CustomMapModel> = try {
        val map = customMapDao.getCustomMapById(mapId)
            ?: throw AppError.DatabaseError("Map not found for id: $mapId")
        Result.Success(mappers.run { map.toModel() })
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun addCustomMap(customMap: CustomMapModel): Result<Unit> = try {
        customMapDao.insertCustomMap(mappers.run { customMap.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun updateCustomMap(customMap: CustomMapModel): Result<Unit> = try {
        customMapDao.updateCustomMap(mappers.run { customMap.toEntity() })
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun deleteCustomMap(mapId: Long): Result<Unit> = try {
        val map = customMapDao.getCustomMapById(mapId)
            ?: throw AppError.DatabaseError("Map not found for id: $mapId")
        customMapDao.deleteCustomMap(map)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override suspend fun setDefaultMap(mapId: Long): Result<Unit> = try {
        customMapDao.setDefaultMap(mapId)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }
}