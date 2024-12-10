package org.iesharia.roommapapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.iesharia.roommapapp.di.IoDispatcher
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.usecase.InitializeDataUseCase
import org.iesharia.roommapapp.domain.usecase.custommap.GetCustomMapUseCase
import org.iesharia.roommapapp.domain.usecase.custommap.SetDefaultMapUseCase
import org.iesharia.roommapapp.domain.usecase.custommap.UpdateCustomMapUseCase
import org.iesharia.roommapapp.domain.usecase.marker.AddMarkerUseCase
import org.iesharia.roommapapp.domain.usecase.marker.GetMarkersUseCase
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Constants
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import org.iesharia.roommapapp.presentation.model.MapEvent
import org.iesharia.roommapapp.presentation.model.MapUiState
import org.iesharia.roommapapp.util.Logger
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMarkersUseCase: GetMarkersUseCase,
    private val addMarkerUseCase: AddMarkerUseCase,
    private val getCustomMapUseCase: GetCustomMapUseCase,
    private val setDefaultMapUseCase: SetDefaultMapUseCase,
    private val updateCustomMapUseCase: UpdateCustomMapUseCase,
    private val initializeDataUseCase: InitializeDataUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var markerUpdateJob: Job? = null
    private var boundingBoxUpdateJob: Job? = null
    private val tag = "MapViewModel"

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        val error = when (throwable) {
            is AppError -> throwable
            else -> throwable.toAppError()
        }
        handleAppError(error)
    }

    init {
        Logger.d(tag, "Initializing MapViewModel")
        initializeMap()
    }

    private fun initializeMap() {
        viewModelScope.launch(ioDispatcher + errorHandler) {
            try {
                Logger.d(tag, "Loading default map")
                _uiState.update { it.copy(isLoading = true) }

                when (val defaultMapResult = getCustomMapUseCase.getDefaultMap()) {
                    is Result.Success -> {
                        setupMap(defaultMapResult.data)
                    }
                    is Result.Error -> {
                        handleAppError(defaultMapResult.error)
                    }
                    is Result.Loading -> {
                        // Aquí no hacemos nada mientras está cargando
                        Logger.e(tag, "Default map is still loading.")
                        throw IllegalStateException("Map cannot be initialized while loading")
                    }
                }

                loadAvailableMaps()
            } catch (e: Exception) {
                when (e) {
                    is AppError -> handleAppError(e)
                    else -> handleAppError(
                        AppError.MapError(
                            Constants.ErrorMessages.MAP_LOAD_ERROR,
                            e
                        )
                    )
                }
            }
        }
    }

    private suspend fun setupMap(map: CustomMapModel) {
        try {
            // Primero actualizamos el estado UI con el mapa
            _uiState.update { state ->
                state.copy(
                    currentMap = map,
                    currentLatitude = map.initialLatitude,
                    currentLongitude = map.initialLongitude,
                    currentZoom = map.initialZoom.toDouble(),
                    error = null,
                    isLoading = true // Mantenemos loading mientras inicializamos
                )
            }

            Logger.d(tag, "Initializing map data")

            // Solo inicializamos datos si el mapa es nuevo (id = 0)
            Logger.d(tag, "Datos del mapa: $map")
            if (map.id == 0L) {
                when (val result = initializeDataUseCase(1L)) {
                    is Result.Success -> {
                        // Después de inicializar, cargamos los marcadores
                        loadMarkersForMap(1L)
                    }
                    is Result.Error -> {
                        throw result.error
                    }
                    Result.Loading -> {
                        // No necesitamos hacer nada aquí
                    }
                }
            } else {
                // Si el mapa ya existe, solo cargamos sus marcadores
                loadMarkersForMap(map.id)
            }

            _uiState.update { it.copy(isLoading = false) }

        } catch (e: Exception) {
            throw AppError.MapError("Error al configurar el mapa", e)
        }
    }

    private fun loadMarkersForMap(mapId: Long) {
        markerUpdateJob?.cancel()
        markerUpdateJob = viewModelScope.launch(ioDispatcher + errorHandler) {
            _uiState.update { it.copy(isLoading = true) }

            getMarkersUseCase(mapId)
                .debounce(Constants.Map.MARKER_UPDATE_THRESHOLD)
                .distinctUntilChanged()
                .onCompletion { _uiState.update { it.copy(isLoading = false) } }
                .catch { e ->
                    handleAppError(AppError.DatabaseError(
                        Constants.ErrorMessages.DATABASE_ERROR,
                        e
                    ))
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
                        is Result.Error -> handleAppError(result.error)
                        Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
        }
    }

    private fun loadAvailableMaps() {
        viewModelScope.launch(ioDispatcher + errorHandler) {
            getCustomMapUseCase.getAllMaps()
                .catch { e ->
                    handleAppError(
                        AppError.DatabaseError(
                            Constants.ErrorMessages.DATABASE_ERROR,
                            e
                        )
                    )
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update { it.copy(availableMaps = result.data, error = null) }
                        }
                        is Result.Error -> handleAppError(result.error)
                        Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
        }
    }

    fun updateMapBounds(minLat: Double, maxLat: Double, minLon: Double, maxLon: Double) {
        try {
            validateCoordinates(minLat, maxLat, minLon, maxLon)

            boundingBoxUpdateJob?.cancel()
            boundingBoxUpdateJob = viewModelScope.launch(ioDispatcher + errorHandler) {
                getMarkersUseCase.getMarkersInBoundingBox(minLat, maxLat, minLon, maxLon)
                    .debounce(Constants.Map.MARKER_UPDATE_THRESHOLD)
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _uiState.update { it.copy(markers = result.data, error = null) }
                            }
                            is Result.Error -> handleAppError(result.error)
                            Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                        }
                    }
            }
        } catch (e: Exception) {
            handleAppError(
                AppError.ValidationError(
                    "Coordenadas inválidas para el bounding box",
                    "coordinates"
                )
            )
        }
    }

    fun addMarker(marker: MarkerModel) {
        viewModelScope.launch(ioDispatcher + errorHandler) {
            try {
                validateMarker(marker)
                when (val result = addMarkerUseCase(marker)) {
                    is Result.Success -> {
                        _uiState.value.currentMap?.let { loadMarkersForMap(it.id) }
                    }
                    is Result.Error -> handleAppError(result.error)
                    Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                }
            } catch (e: Exception) {
                when (e) {
                    is AppError -> handleAppError(e)
                    else -> handleAppError(
                        AppError.ValidationError(
                            "Error al añadir el marcador",
                            "marker"
                        )
                    )
                }
            }
        }
    }

    fun updateMapConfiguration(latitude: Double, longitude: Double, zoom: Double) {
        viewModelScope.launch(ioDispatcher + errorHandler) {
            try {
                validateMapConfiguration(latitude, longitude, zoom)

                _uiState.value.currentMap?.let { currentMap ->
                    val updatedMap = currentMap.copy(
                        initialLatitude = latitude,
                        initialLongitude = longitude,
                        initialZoom = zoom.toFloat()
                    )

                    when (val result = updateCustomMapUseCase(updatedMap)) {
                        is Result.Success -> {
                            _uiState.update { it.copy(currentMap = updatedMap, error = null) }
                        }
                        is Result.Error -> handleAppError(result.error)
                        Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                } ?: throw AppError.MapError("No hay un mapa seleccionado")
            } catch (e: Exception) {
                when (e) {
                    is AppError -> handleAppError(e)
                    else -> handleAppError(
                        AppError.ValidationError(
                            "Error al actualizar la configuración del mapa",
                            "configuration"
                        )
                    )
                }
            }
        }
    }

    fun updateMapPosition(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            updateMapConfiguration(
                latitude = latitude,
                longitude = longitude,
                zoom = uiState.value.currentZoom
            )
        }
    }

    fun handleEvent(event: MapEvent) {
        viewModelScope.launch(errorHandler) {
            when (event) {
                is MapEvent.OnMarkerSelected -> {
                    _uiState.update { it.copy(selectedMarker = event.marker, error = null) }
                }
                is MapEvent.OnMapSelected -> {
                    withContext(ioDispatcher) {
                        try {
                            val selectedMap = getCustomMapUseCase.getCustomMap(event.mapId).getOrThrow()
                            setupMap(selectedMap)
                            setDefaultMapUseCase(event.mapId)
                        } catch (e: Exception) {
                            handleAppError(
                                AppError.MapError(
                                    "Error al seleccionar el mapa",
                                    e
                                )
                            )
                        }
                    }
                }
                MapEvent.OnErrorDismissed -> {
                    _uiState.update { it.copy(error = null) }
                }
            }
        }
    }

    private fun validateMarker(marker: MarkerModel) {
        when {
            marker.title.isBlank() ->
                throw AppError.ValidationError("El título no puede estar vacío", "title")
            marker.latitude !in -90.0..90.0 ->
                throw AppError.ValidationError("Latitud debe estar entre -90 y 90", "latitude")
            marker.longitude !in -180.0..180.0 ->
                throw AppError.ValidationError("Longitud debe estar entre -180 y 180", "longitude")
        }
    }

    private fun validateMapConfiguration(latitude: Double, longitude: Double, zoom: Double) {
        when {
            latitude !in -90.0..90.0 ->
                throw AppError.ValidationError("Latitud debe estar entre -90 y 90", "latitude")
            longitude !in -180.0..180.0 ->
                throw AppError.ValidationError("Longitud debe estar entre -180 y 180", "longitude")
            zoom !in Constants.Map.MIN_ZOOM..Constants.Map.MAX_ZOOM ->
                throw AppError.ValidationError(
                    "Zoom debe estar entre ${Constants.Map.MIN_ZOOM} y ${Constants.Map.MAX_ZOOM}",
                    "zoom"
                )
        }
    }

    private fun validateCoordinates(minLat: Double, maxLat: Double, minLon: Double, maxLon: Double) {
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

    private fun handleAppError(error: AppError) {
        Logger.e(tag, "AppError handled: ${error.message}", error)
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                error = error.getUserMessage()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        markerUpdateJob?.cancel()
        boundingBoxUpdateJob?.cancel()
    }
}