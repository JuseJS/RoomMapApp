package org.iesharia.roommapapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.iesharia.roommapapp.data.database.entity.MarkerType
import org.iesharia.roommapapp.data.repository.MarkerTypeRepository
import javax.inject.Inject

@HiltViewModel
class MarkerTypeViewModel @Inject constructor(
    private val markerTypeRepository: MarkerTypeRepository
) : ViewModel() {

    private val _markerTypes = MutableStateFlow<List<MarkerType>>(emptyList())
    val markerTypes: StateFlow<List<MarkerType>> = _markerTypes

    init {
        loadMarkerTypes()
    }

    private fun loadMarkerTypes() {
        viewModelScope.launch {
            markerTypeRepository.getAllMarkerTypes().collect { types ->
                _markerTypes.value = types
            }
        }
    }

    suspend fun addMarkerType(
        name: String,
        icon: String,
        color: String,
        description: String? = null
    ) {
        val markerType = MarkerType(
            name = name,
            icon = icon,
            color = color,
            description = description
        )
        markerTypeRepository.insertMarkerType(markerType)
    }

    suspend fun updateMarkerType(markerType: MarkerType) {
        markerTypeRepository.updateMarkerType(markerType)
    }

    suspend fun deleteMarkerType(markerType: MarkerType) {
        markerTypeRepository.deleteMarkerType(markerType)
    }
}