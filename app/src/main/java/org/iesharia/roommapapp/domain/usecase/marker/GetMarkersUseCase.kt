package org.iesharia.roommapapp.domain.usecase.marker

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class GetMarkersUseCase @Inject constructor(
    private val repository: MarkerRepository
) {
    operator fun invoke(mapId: Long): Flow<Result<List<MarkerModel>>> {
        if (mapId <= 0) {
            return kotlinx.coroutines.flow.flow {
                emit(Result.Error(AppError.ValidationError("ID de mapa inválido", "mapId")))
            }
        }
        return repository.getMarkersByMapId(mapId)
    }

    suspend fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<Result<List<MarkerModel>>> {
        return try {
            validateBoundingBox(minLat, maxLat, minLon, maxLon)
            repository.getMarkersInBoundingBox(minLat, maxLat, minLon, maxLon)
        } catch (e: Exception) {
            kotlinx.coroutines.flow.flow { emit(Result.Error(e.toAppError())) }
        }
    }

    private fun validateBoundingBox(minLat: Double, maxLat: Double, minLon: Double, maxLon: Double) {
        when {
            minLat > maxLat ->
                throw AppError.ValidationError("Latitud mínima no puede ser mayor que la máxima")
            minLon > maxLon ->
                throw AppError.ValidationError("Longitud mínima no puede ser mayor que la máxima")
            !isValidLatitude(minLat) || !isValidLatitude(maxLat) ->
                throw AppError.ValidationError("Latitud debe estar entre -90 y 90")
            !isValidLongitude(minLon) || !isValidLongitude(maxLon) ->
                throw AppError.ValidationError("Longitud debe estar entre -180 y 180")
        }
    }

    private fun isValidLatitude(lat: Double) = lat in -90.0..90.0
    private fun isValidLongitude(lon: Double) = lon in -180.0..180.0
}