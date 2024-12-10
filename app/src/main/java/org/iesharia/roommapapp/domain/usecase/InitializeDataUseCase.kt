package org.iesharia.roommapapp.domain.usecase

import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import org.iesharia.roommapapp.domain.model.MapIconType
import org.iesharia.roommapapp.domain.util.AppError
import org.iesharia.roommapapp.domain.util.Result
import org.iesharia.roommapapp.domain.util.toAppError
import javax.inject.Inject

class InitializeDataUseCase @Inject constructor(
    private val markerRepository: MarkerRepository,
    private val markerTypeRepository: MarkerTypeRepository
) {
    suspend operator fun invoke(mapId: Long): Result<Unit> {
        return try {
            if (mapId <= 0) {
                throw AppError.ValidationError("ID de mapa inválido")
            }

            // Inicializar tipos de marcadores
            val markerTypes = createMarkerTypes()
            markerTypes.forEach { markerType ->
                when (val result = markerTypeRepository.addMarkerType(markerType)) {
                    is Result.Error -> throw result.error
                    else -> { /* Continuar */ }
                }
            }

            // Inicializar marcadores
            val markers = createMarkers(mapId, markerTypes)
            markers.forEach { marker ->
                when (val result = markerRepository.addMarker(marker)) {
                    is Result.Error -> throw result.error
                    else -> { /* Continuar */ }
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toAppError())
        }
    }

    private fun createMarkerTypes(): List<MarkerTypeModel> {
        return listOf(
            MarkerTypeModel(
                id = 1,
                name = "Restaurante",
                icon = MapIconType.getIconName(MapIconType.RESTAURANT),
                color = MapIconType.getDefaultColor(MapIconType.RESTAURANT),
                description = "Lugares para comer",
                isVisible = true
            ),
            MarkerTypeModel(
                id = 2,
                name = "Hotel",
                icon = MapIconType.getIconName(MapIconType.HOTEL),
                color = MapIconType.getDefaultColor(MapIconType.HOTEL),
                description = "Lugares para hospedarse",
                isVisible = true
            ),
            MarkerTypeModel(
                id = 3,
                name = "Monumento",
                icon = MapIconType.getIconName(MapIconType.MONUMENT),
                color = MapIconType.getDefaultColor(MapIconType.MONUMENT),
                description = "Lugares históricos y monumentos",
                isVisible = true
            ),
            MarkerTypeModel(
                id = 4,
                name = "Parque",
                icon = MapIconType.getIconName(MapIconType.PARK),
                color = MapIconType.getDefaultColor(MapIconType.PARK),
                description = "Áreas verdes y recreativas",
                isVisible = true
            )
        )
    }

    private fun createMarkers(mapId: Long, types: List<MarkerTypeModel>): List<MarkerModel> {
        return listOf(
            // Restaurantes
            createMarker("El Diablo", types[0], mapId, 29.0117, -13.7357, "Restaurante icónico en el Parque Nacional de Timanfaya, con platos cocinados con calor volcánico"),
            createMarker("La Casa Roja", types[0], mapId, 28.9182, -13.7086, "Restaurante frente al puerto en Marina Rubicón con especialidades locales"),
            createMarker("Casa Brigida", types[0], mapId, 28.8638, -13.8225, "Deliciosa cocina canaria en el pueblo de Yaiza"),

            // Hoteles
            createMarker("Hotel Fariones", types[1], mapId, 28.9215, -13.6650, "Lujo y confort cerca de la playa en Puerto del Carmen"),
            createMarker("Hotel Princesa Yaiza", types[1], mapId, 28.8677, -13.8225, "Hotel de lujo frente al mar en Playa Blanca"),
            createMarker("Hotel La Geria", types[1], mapId, 28.9253, -13.6587, "Hotel cerca de la playa con excelente gastronomía"),

            // Monumentos
            createMarker("Monumento al Campesino", types[2], mapId, 29.0005, -13.6169, "Obra icónica de César Manrique en homenaje a los campesinos de Lanzarote"),
            createMarker("Castillo de San José", types[2], mapId, 28.9659, -13.5359, "Museo Internacional de Arte Contemporáneo"),
            createMarker("Jardín de Cactus", types[2], mapId, 29.0915, -13.4810, "Espectacular jardín diseñado por César Manrique"),

            // Parques
            createMarker("Parque Nacional de Timanfaya", types[3], mapId, 28.9983, -13.7922, "Parque volcánico único en el mundo"),
            createMarker("Cueva de los Verdes", types[3], mapId, 29.1570, -13.4316, "Sistema de túneles volcánicos impresionante"),
            createMarker("Los Jameos del Agua", types[3], mapId, 29.1574, -13.4320, "Espacio natural transformado por César Manrique con cuevas y piscinas"),
        )
    }

    private fun createMarker(
        title: String,
        type: MarkerTypeModel,
        mapId: Long,
        latitude: Double,
        longitude: Double,
        description: String
    ): MarkerModel {
        return MarkerModel(
            id = "0",
            title = title,
            type = type,
            mapId = mapId,
            latitude = latitude,
            longitude = longitude,
            description = description
        )
    }
}