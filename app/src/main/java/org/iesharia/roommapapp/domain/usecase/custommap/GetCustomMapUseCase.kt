package org.iesharia.roommapapp.domain.usecase.custommap

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.util.Logger
import javax.inject.Inject

class GetCustomMapUseCase @Inject constructor(
    private val repository: CustomMapRepository,
    private val addDefaultMapUseCase: AddDefaultMapUseCase
) {
    suspend fun getDefaultMap(): Result<CustomMapModel> {
        return when (val defaultMapResult = repository.getDefaultMap()) {
            is Result.Success -> {
                defaultMapResult
            }
            is Result.Error -> {
                // Si no hay un mapa predeterminado, verificamos si existen otros mapas
                // Filtramos los estados Loading antes de tomar el primero.
                when (val allMapsResult = repository.getAllCustomMaps()
                    .filterNot { it is Result.Loading }
                    .first()) {
                    is Result.Success -> {
                        Logger.d("GetCustomMapUseCase", "allMapsResult: $allMapsResult")
                        if (allMapsResult.data.isNotEmpty()) {
                            // Si hay mapas existentes, usamos el primero
                            Result.Success(allMapsResult.data.first())
                        } else {
                            // Si no hay mapas existentes, creamos uno nuevo
                            addDefaultMapUseCase()
                        }
                    }
                    is Result.Error -> {
                        Logger.e("GetCustomMapUseCase", "Error al obtener mapas: ${allMapsResult.error}")
                        // Si falla la obtención de mapas, devolvemos el error
                        allMapsResult
                    }
                    is Result.Loading -> {
                        Logger.d("GetCustomMapUseCase", "Result.Loading inesperado tras filtrado")
                        Result.Loading
                    }
                }
            }
            is Result.Loading -> {
                Result.Loading
            }
        }
    }

    suspend fun getCustomMap(mapId: Long): Result<CustomMapModel> {
        return repository.getCustomMapById(mapId)
    }

    fun getAllMaps(): Flow<Result<List<CustomMapModel>>> {
        return repository.getAllCustomMaps()
    }
}
