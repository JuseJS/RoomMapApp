package org.iesharia.roommapapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.iesharia.roommapapp.di.IoDispatcher
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.usecase.InitializeDataUseCase
import org.iesharia.roommapapp.domain.usecase.custommap.GetCustomMapUseCase
import org.iesharia.roommapapp.domain.usecase.marker.GetMarkersUseCase
import org.iesharia.roommapapp.domain.util.Constants
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.presentation.model.MapEvent
import org.iesharia.roommapapp.presentation.model.MapUiState
import org.iesharia.roommapapp.util.Logger
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMarkersUseCase: GetMarkersUseCase,
    private val getCustomMapUseCase: GetCustomMapUseCase,
    private val initializeDataUseCase: InitializeDataUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var markerUpdateJob: Job? = null
    private val tag = "MapViewModel"

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Logger.e(tag, "Coroutine exception handled", exception)
        handleError(exception.message ?: "Error desconocido")
    }

    init {
        Logger.d(tag, "Initializing MapViewModel")
        initializeMap()
    }

    private fun initializeMap() {
        viewModelScope.launch(ioDispatcher) {
            try {
                Logger.d(tag, "Loading default map")
                _uiState.update { it.copy(isLoading = true) }

                when (val mapResult = getCustomMapUseCase.getDefaultMap()) {
                    is Result.Success<CustomMapModel> -> {
                        Logger.d(tag, "Default map loaded successfully")
                        val defaultMap = mapResult.data

                        _uiState.update { state ->
                            state.copy(
                                currentMap = defaultMap,
                                currentLatitude = defaultMap.initialLatitude,
                                currentLongitude = defaultMap.initialLongitude,
                                currentZoom = defaultMap.initialZoom.toDouble(),
                                error = null,
                                isLoading = false
                            )
                        }
                        Logger.d(tag, "No llega hasta aqui")

                        try {
                            Logger.d(tag, "Initializing map data")
                            initializeDataUseCase(defaultMap.id)
                            loadMarkersForMap(defaultMap.id)
                        } catch (e: Exception) {
                            Logger.e(tag, "Error initializing data", e)
                            handleError("Error al inicializar los datos: ${e.message}")
                        }
                    }
                    is Result.Error -> {
                        Logger.e(tag, "Error loading default map", mapResult.exception)
                        handleError(mapResult.exception.message ?: "Error al cargar el mapa")
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }

                loadAvailableMaps()

            } catch (e: Exception) {
                Logger.e(tag, "Error in initializeMap", e)
                handleError(e.message ?: Constants.ErrorMessages.MAP_LOAD_ERROR)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadAvailableMaps() {
        viewModelScope.launch(ioDispatcher) {
            try {
                getCustomMapUseCase.getAllMaps()
                    .catch { e -> handleError(e.message ?: Constants.ErrorMessages.DATABASE_ERROR) }
                    .collect { maps ->
                        _uiState.update {
                            it.copy(
                                availableMaps = maps,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                handleError(e.message ?: Constants.ErrorMessages.DATABASE_ERROR)
            }
        }
    }

    private fun loadMarkersForMap(mapId: Long) {
        markerUpdateJob?.cancel()
        markerUpdateJob = viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getMarkersUseCase(mapId)
                    .debounce(Constants.Map.MARKER_UPDATE_THRESHOLD)
                    .distinctUntilChanged()
                    .onCompletion { _uiState.update { it.copy(isLoading = false) } }
                    .catch { e ->
                        handleError(e.message ?: Constants.ErrorMessages.DATABASE_ERROR)
                    }
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _uiState.update { state ->
                                    state.copy(
                                        markers = result.data,
                                        isLoading = false,
                                        error = null
                                    )
                                }
                            }
                            is Result.Error -> {
                                handleError(result.exception.message
                                    ?: Constants.ErrorMessages.DATABASE_ERROR)
                            }
                            Result.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }
                        }
                    }
            } catch (e: Exception) {
                handleError(e.message ?: Constants.ErrorMessages.DATABASE_ERROR)
            }
        }
    }

    fun handleEvent(event: MapEvent) {
        viewModelScope.launch(errorHandler) {
            when (event) {
                is MapEvent.OnMarkerSelected -> {
                    _uiState.update {
                        it.copy(
                            selectedMarker = event.marker,
                            error = null
                        )
                    }
                }
                is MapEvent.OnMapSelected -> {
                    withContext(ioDispatcher) {
                        try {
                            val selectedMap = getCustomMapUseCase.getCustomMap(event.mapId)
                            selectedMap?.let { map ->
                                _uiState.update {
                                    it.copy(
                                        currentMap = map,
                                        error = null
                                    )
                                }
                                loadMarkersForMap(map.id)
                            } ?: handleError(Constants.ErrorMessages.MAP_LOAD_ERROR)
                        } catch (e: Exception) {
                            handleError(e.message ?: Constants.ErrorMessages.MAP_LOAD_ERROR)
                        }
                    }
                }
                MapEvent.OnErrorDismissed -> {
                    _uiState.update { it.copy(error = null) }
                }
            }
        }
    }

    private fun handleError(errorMessage: String) {
        Logger.e(tag, "Error handled: $errorMessage")
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                error = errorMessage
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        markerUpdateJob?.cancel()
    }
}
