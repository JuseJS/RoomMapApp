package org.iesharia.roommapapp.domain.usecase.custommap

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class AddDefaultMapUseCase @Inject constructor(
    private val repository: CustomMapRepository
) {
    suspend operator fun invoke(): Result<CustomMapModel> {
        return try {
            // Coordenadas de Haría, Lanzarote
            val defaultMap = CustomMapModel(
                id = 0L,
                name = "Haría, Lanzarote",
                styleJson = "",
                initialLatitude = 29.143461,
                initialLongitude = -13.497764,
                initialZoom = 15.0f,
                isDefault = true
            )

            repository.addCustomMap(defaultMap)
            repository.setDefaultMap(defaultMap.id)
            Result.Success(defaultMap)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}