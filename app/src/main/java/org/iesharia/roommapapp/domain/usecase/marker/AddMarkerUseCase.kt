package org.iesharia.roommapapp.domain.usecase.marker

import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class AddMarkerUseCase @Inject constructor(
    private val repository: MarkerRepository
) {
    suspend operator fun invoke(marker: MarkerModel): Result<Unit> {
        return try {
            repository.addMarker(marker)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}