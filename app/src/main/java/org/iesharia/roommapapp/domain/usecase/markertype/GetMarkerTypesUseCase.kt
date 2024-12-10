package org.iesharia.roommapapp.domain.usecase.markertype

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class GetMarkerTypesUseCase @Inject constructor(
    private val repository: MarkerTypeRepository
) {
    operator fun invoke(): Flow<Result<List<MarkerTypeModel>>> {
        return repository.getAllMarkerTypes()
            .map<List<MarkerTypeModel>, Result<List<MarkerTypeModel>>> { Result.Success(it) }
            .catch { emit(Result.Error(Exception(it.message))) }
    }
}