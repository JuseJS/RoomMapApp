package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class AddCustomMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(customMap: CustomMapModel): Result<Unit> {
        return try {
            if (customMap.name.isBlank()) {
                throw AppError.ValidationError("El nombre del mapa no puede estar vacío")
            }

            if (customMap.id <= 0) {
                throw AppError.ValidationError("ID de mapa inválido")
            }

            repository.addCustomMap(customMap)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toAppError())
        }
    }
}