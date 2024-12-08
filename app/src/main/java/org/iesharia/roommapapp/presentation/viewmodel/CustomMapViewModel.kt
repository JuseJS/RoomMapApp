package org.iesharia.roommapapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.iesharia.roommapapp.data.database.entity.CustomMap
import org.iesharia.roommapapp.data.repository.CustomMapRepository
import javax.inject.Inject

@HiltViewModel
class CustomMapViewModel @Inject constructor(
    private val customMapRepository: CustomMapRepository
) : ViewModel() {

    private val _currentMap = MutableStateFlow<CustomMap?>(null)
    val currentMap: StateFlow<CustomMap?> = _currentMap

    private val _availableMaps = MutableStateFlow<List<CustomMap>>(emptyList())
    val availableMaps: StateFlow<List<CustomMap>> = _availableMaps

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Cargar mapa por defecto
            customMapRepository.getDefaultMap()?.let { map ->
                _currentMap.value = map
            }

            // Observar todos los mapas disponibles
            customMapRepository.getAllCustomMaps().collect { maps ->
                _availableMaps.value = maps
            }
        }
    }

    suspend fun createCustomMap(
        name: String,
        styleJson: String,
        initialLatitude: Double,
        initialLongitude: Double,
        initialZoom: Float,
        isDefault: Boolean = false
    ) {
        val customMap = CustomMap(
            name = name,
            styleJson = styleJson,
            initialLatitude = initialLatitude,
            initialLongitude = initialLongitude,
            initialZoom = initialZoom,
            isDefault = isDefault
        )
        customMapRepository.insertCustomMap(customMap)
    }

    suspend fun setCurrentMap(mapId: Long) {
        customMapRepository.getCustomMapById(mapId)?.let { map ->
            _currentMap.value = map
        }
    }

    suspend fun setDefaultMap(mapId: Long) {
        customMapRepository.setDefaultMap(mapId)
    }

    suspend fun deleteCustomMap(customMap: CustomMap) {
        customMapRepository.deleteCustomMap(customMap)
    }
}