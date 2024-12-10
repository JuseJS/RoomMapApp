package org.iesharia.roommapapp.data.mapper

import org.iesharia.roommapapp.data.database.entity.MarkerEntity
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import javax.inject.Inject

class MarkerMapper @Inject constructor(
    private val markerTypeMapper: MarkerTypeMapper
) {
    fun toModel(entity: MarkerEntity, type: MarkerTypeModel): MarkerModel {
        return MarkerModel(
            id = entity.id.toString(),
            title = entity.title,
            type = type,
            mapId = entity.mapId,
            latitude = entity.latitude,
            longitude = entity.longitude,
            description = entity.description
        )
    }

    fun toEntity(model: MarkerModel): MarkerEntity {
        return MarkerEntity(
            id = model.id.toLongOrNull() ?: 0,
            title = model.title,
            typeId = model.type.id,
            mapId = model.mapId,
            latitude = model.latitude,
            longitude = model.longitude,
            description = model.description
        )
    }
}