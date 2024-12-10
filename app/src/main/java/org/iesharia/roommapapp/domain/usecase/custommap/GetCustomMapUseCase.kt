package org.iesharia.roommapapp.domain.usecase.custommap

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class GetCustomMapUseCase @Inject constructor(
    private val repository: CustomMapRepository,
    private val addDefaultMapUseCase: AddDefaultMapUseCase
) {
    suspend fun getDefaultMap(): CustomMapModel {
        return repository.getDefaultMap() ?: run {
            // Si no hay mapa por defecto, creamos uno
            when (val result = addDefaultMapUseCase()) {
                is Result.Success -> result.data
                is Result.Error -> throw result.exception
                else -> throw IllegalStateException("No se pudo crear el mapa por defecto")
            }
        }
    }

    suspend fun getCustomMap(mapId: Long): CustomMapModel? {
        return repository.getCustomMapById(mapId)
    }

    fun getAllMaps(): Flow<List<CustomMapModel>> {
        return repository.getAllCustomMaps()
    }
}