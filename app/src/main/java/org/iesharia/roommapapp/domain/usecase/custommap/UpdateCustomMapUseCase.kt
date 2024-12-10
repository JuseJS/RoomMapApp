package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class UpdateCustomMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(customMap: CustomMapModel): Result<Unit> {
        return try {
            validateCustomMap(customMap)
            repository.updateCustomMap(customMap)
        } catch (e: Exception) {
            Result.Error(e.toAppError())
        }
    }

    private fun validateCustomMap(map: CustomMapModel) {
        when {
            map.id <= 0 ->
                throw AppError.ValidationError("ID de mapa inválido")
            map.name.isBlank() ->
                throw AppError.ValidationError("El nombre no puede estar vacío")
            map.initialLatitude !in -90.0..90.0 ->
                throw AppError.ValidationError("Latitud inicial inválida")
            map.initialLongitude !in -180.0..180.0 ->
                throw AppError.ValidationError("Longitud inicial inválida")
            map.initialZoom <= 0 ->
                throw AppError.ValidationError("Zoom inicial debe ser mayor que 0")
        }
    }
}