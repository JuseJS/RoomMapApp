package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class AddCustomMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(customMap: CustomMapModel): Result<Unit> {
        return try {
            repository.addCustomMap(customMap)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}