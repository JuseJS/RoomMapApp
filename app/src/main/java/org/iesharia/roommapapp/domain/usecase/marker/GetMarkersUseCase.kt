package org.iesharia.roommapapp.domain.usecase.marker

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class GetMarkersUseCase @Inject constructor(
    private val repository: MarkerRepository
) {
    operator fun invoke(mapId: Long): Flow<Result<List<MarkerModel>>> {
        return repository.getMarkersByMapId(mapId)
            .map<List<MarkerModel>, Result<List<MarkerModel>>> { Result.Success(it) }
            .catch { emit(Result.Error(Exception(it.message))) }
    }
}