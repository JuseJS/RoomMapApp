package org.iesharia.roommapapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.iesharia.roommapapp.data.database.entity.MarkerEntity
import org.iesharia.roommapapp.data.repository.MarkerRepository
import javax.inject.Inject

@HiltViewModel
class MarkerViewModel @Inject constructor(
    private val markerRepository: MarkerRepository
) : ViewModel() {

    private val _markers = MutableStateFlow<List<MarkerEntity>>(emptyList())
    val markers: StateFlow<List<MarkerEntity>> = _markers

    private val _selectedMarker = MutableStateFlow<MarkerEntity?>(null)
    val selectedMarker: StateFlow<MarkerEntity?> = _selectedMarker

    fun loadMarkersForMap(mapId: Long) {
        viewModelScope.launch {
            markerRepository.getMarkersByMapId(mapId).collect { mapMarkers ->
                _markers.value = mapMarkers
            }
        }
    }

    suspend fun addMarker(
        title: String,
        typeId: Long,
        mapId: Long,
        latitude: Double,
        longitude: Double,
        description: String? = null
    ): Long {
        val marker = MarkerEntity(
            title = title,
            typeId = typeId,
            mapId = mapId,
            latitude = latitude,
            longitude = longitude,
            description = description
        )
        return markerRepository.insertMarker(marker)
    }

    fun updateMarker(marker: MarkerEntity) {
        viewModelScope.launch {
            markerRepository.updateMarker(marker)
        }
    }

    fun deleteMarker(marker: MarkerEntity) {
        viewModelScope.launch {
            markerRepository.deleteMarker(marker)
        }
    }

    fun selectMarker(marker: MarkerEntity?) {
        _selectedMarker.value = marker
    }
}