package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class UpdateCustomMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(customMap: CustomMapModel): Result<Unit> {
        return try {
            repository.updateCustomMap(customMap)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}