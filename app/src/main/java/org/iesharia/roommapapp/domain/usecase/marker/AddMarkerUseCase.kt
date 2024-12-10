package org.iesharia.roommapapp.domain.usecase.marker

import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class AddMarkerUseCase @Inject constructor(
    private val repository: MarkerRepository
) {
    suspend operator fun invoke(marker: MarkerModel): Result<Unit> {
        return try {
            validateMarker(marker)
            repository.addMarker(marker)
        } catch (e: Exception) {
            Result.Error(e.toAppError())
        }
    }

    private fun validateMarker(marker: MarkerModel) {
        when {
            marker.title.isBlank() ->
                throw AppError.ValidationError("El título no puede estar vacío", "title")
            marker.latitude !in -90.0..90.0 ->
                throw AppError.ValidationError("Latitud debe estar entre -90 y 90", "latitude")
            marker.longitude !in -180.0..180.0 ->
                throw AppError.ValidationError("Longitud debe estar entre -180 y 180", "longitude")
        }
    }
}