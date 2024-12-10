package org.iesharia.roommapapp.data.mapper

import org.iesharia.roommapapp.data.database.entity.CustomMap
import org.iesharia.roommapapp.data.database.entity.MarkerEntity
import org.iesharia.roommapapp.data.database.entity.MarkerType
import org.iesharia.roommapapp.domain.model.CustomMapModel
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mappers @Inject constructor() {
    // CustomMap Mappings
    fun CustomMap.toModel() = CustomMapModel(
        id = id,
        name = name,
        styleJson = styleJson,
        initialLatitude = initialLatitude,
        initialLongitude = initialLongitude,
        initialZoom = initialZoom,
        isDefault = isDefault
    )

    fun CustomMapModel.toEntity() = CustomMap(
        id = id,
        name = name,
        styleJson = styleJson,
        initialLatitude = initialLatitude,
        initialLongitude = initialLongitude,
        initialZoom = initialZoom,
        isDefault = isDefault
    )

    // MarkerType Mappings
    fun MarkerType.toModel() = MarkerTypeModel(
        id = id,
        name = name,
        icon = icon,
        color = color,
        description = description,
        isVisible = isVisible
    )

    fun MarkerTypeModel.toEntity() = MarkerType(
        id = id,
        name = name,
        icon = icon,
        color = color,
        description = description,
        isVisible = isVisible
    )

    // Marker Mappings
    fun MarkerEntity.toModel(type: MarkerTypeModel) = MarkerModel(
        id = id.toString(),
        title = title,
        type = type,
        mapId = mapId,
        latitude = latitude,
        longitude = longitude,
        description = description
    )

    fun MarkerModel.toEntity() = MarkerEntity(
        id = id.toLongOrNull() ?: 0,
        title = title,
        typeId = type.id,
        mapId = mapId,
        latitude = latitude,
        longitude = longitude,
        description = description
    )
}