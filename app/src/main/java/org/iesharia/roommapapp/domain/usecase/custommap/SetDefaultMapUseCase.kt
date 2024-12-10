package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class SetDefaultMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(mapId: Long): Result<Unit> {
        return try {
            repository.setDefaultMap(mapId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}