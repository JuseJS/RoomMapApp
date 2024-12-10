package org.iesharia.roommapapp.domain.usecase.markertype

import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import org.iesharia.roommapapp.domain.util.Result
import javax.inject.Inject

class GetMarkerTypesUseCase @Inject constructor(
    private val repository: MarkerTypeRepository
) {
    operator fun invoke(): Flow<Result<List<MarkerTypeModel>>> {
        return repository.getAllMarkerTypes()
    }
}