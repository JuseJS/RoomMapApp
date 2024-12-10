package org.iesharia.roommapapp.presentation.model

import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.util.Constants

data class MapUiState(
    val markers: List<MarkerModel> = emptyList(),
    val selectedMarker: MarkerModel? = null,
    val currentMap: CustomMapModel? = null,
    val availableMaps: List<CustomMapModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLatitude: Double = Constants.Map.DEFAULT_LATITUDE,
    val currentLongitude: Double = Constants.Map.DEFAULT_LONGITUDE,
    val currentZoom: Double = Constants.Map.DEFAULT_ZOOM
)

sealed interface MapEvent {
    data class OnMarkerSelected(val marker: MarkerModel?) : MapEvent
    data class OnMapSelected(val mapId: Long) : MapEvent
    object OnErrorDismissed : MapEvent
}