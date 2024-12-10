package org.iesharia.roommapapp.presentation.model

import org.iesharia.roommapapp.domain.model.MarkerTypeModel

data class MarkerTypeUiState(
    val markerTypes: List<MarkerTypeModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface MarkerTypeEvent {
    data class OnMarkerTypeSelected(val markerType: MarkerTypeModel) : MarkerTypeEvent
    object OnErrorDismissed : MarkerTypeEvent
}