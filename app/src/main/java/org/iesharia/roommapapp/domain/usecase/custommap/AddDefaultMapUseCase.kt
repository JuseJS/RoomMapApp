package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class AddDefaultMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(): Result<CustomMapModel> {
        return try {
            val defaultMap = CustomMapModel(
                id = 0L,
                name = "Haría, Lanzarote",
                styleJson = "",
                initialLatitude = 29.143461,
                initialLongitude = -13.497764,
                initialZoom = 15.0f,
                isDefault = true
            )

            when (val addResult = repository.addCustomMap(defaultMap)) {
                is Result.Success -> {
                    when (val setDefaultResult = repository.setDefaultMap(defaultMap.id)) {
                        is Result.Success -> Result.Success(defaultMap)
                        is Result.Error -> setDefaultResult
                        Result.Loading -> Result.Loading
                    }
                }
                is Result.Error -> addResult
                Result.Loading -> Result.Loading
            }
        } catch (e: Exception) {
            Result.Error(e.toAppError())
        }
    }
}