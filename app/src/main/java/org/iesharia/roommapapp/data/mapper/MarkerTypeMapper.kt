package org.iesharia.roommapapp.data.mapper

import org.iesharia.roommapapp.data.database.entity.MarkerType
import org.iesharia.roommapapp.domain.model.MarkerTypeModel
import javax.inject.Inject

class MarkerTypeMapper @Inject constructor() {
    fun toModel(entity: MarkerType): MarkerTypeModel {
        return MarkerTypeModel(
            id = entity.id,
            name = entity.name,
            icon = entity.icon,
            color = entity.color,
            description = entity.description,
            isVisible = entity.isVisible
        )
    }

    fun toEntity(model: MarkerTypeModel): MarkerType {
        return MarkerType(
            id = model.id,
            name = model.name,
            icon = model.icon,
            color = model.color,
            description = model.description,
            isVisible = model.isVisible
        )
    }
}