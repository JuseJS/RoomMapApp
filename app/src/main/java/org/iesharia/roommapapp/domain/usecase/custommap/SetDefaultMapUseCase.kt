package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class SetDefaultMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(mapId: Long): Result<Unit> {
        return try {
            if (mapId <= 0) {
                throw AppError.ValidationError("ID de mapa inválido")
            }

            when (val mapExists = repository.getCustomMapById(mapId)) {
                is Result.Success -> repository.setDefaultMap(mapId)
                is Result.Error -> Result.Error(AppError.DatabaseError("El mapa no existe"))
                Result.Loading -> Result.Loading
            }
        } catch (e: Exception) {
            Result.Error(e.toAppError())
        }
    }
}