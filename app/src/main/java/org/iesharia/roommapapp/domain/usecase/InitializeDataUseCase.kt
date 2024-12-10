package org.iesharia.roommapapp.domain.usecase

import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import org.iesharia.roommapapp.domain.model.MapIconType
import javax.inject.Inject

class InitializeDataUseCase @Inject constructor(
    private val markerRepository: MarkerRepository,
    private val markerTypeRepository: MarkerTypeRepository
) {
    suspend operator fun invoke(mapId: Long) {
        // Inicializar tipos de marcadores
        val markerTypes = listOf(
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

        // Insertar tipos de marcadores
        markerTypes.forEach { markerTypeRepository.addMarkerType(it) }

        // Crear marcadores de ejemplo
        val markers = listOf(
            // Restaurantes
            createMarker("Casa Lucio", markerTypes[0], mapId, 40.4128, -3.7075, "Restaurante tradicional madrileño"),
            createMarker("Botín", markerTypes[0], mapId, 40.4156, -3.7089, "Restaurante más antiguo del mundo"),
            createMarker("La Barraca", markerTypes[0], mapId, 40.4176, -3.7048, "Especialidad en paella"),

            // Hoteles
            createMarker("Hotel Ritz", markerTypes[1], mapId, 40.4156, -3.6922, "Hotel de lujo histórico"),
            createMarker("Hotel Palace", markerTypes[1], mapId, 40.4147, -3.6947, "Hotel emblemático"),
            createMarker("Hotel Ópera", markerTypes[1], mapId, 40.4183, -3.7089, "Hotel céntrico"),

            // Monumentos
            createMarker("Palacio Real", markerTypes[2], mapId, 40.4180, -3.7144, "Residencia oficial de la Corona"),
            createMarker("Plaza Mayor", markerTypes[2], mapId, 40.4155, -3.7074, "Plaza histórica principal"),
            createMarker("Puerta del Sol", markerTypes[2], mapId, 40.4169, -3.7034, "Centro neurálgico de Madrid"),

            // Parques
            createMarker("Retiro", markerTypes[3], mapId, 40.4153, -3.6844, "Parque principal de Madrid"),
            createMarker("Casa de Campo", markerTypes[3], mapId, 40.4115, -3.7473, "Mayor pulmón verde de Madrid"),
            createMarker("Madrid Río", markerTypes[3], mapId, 40.4097, -3.7199, "Parque lineal junto al río")
        )

        // Insertar marcadores
        markers.forEach { markerRepository.addMarker(it) }
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