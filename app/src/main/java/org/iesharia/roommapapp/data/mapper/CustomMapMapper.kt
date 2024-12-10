package org.iesharia.roommapapp.data.mapper

import org.iesharia.roommapapp.data.database.entity.CustomMap
import org.iesharia.roommapapp.domain.model.CustomMapModel
import javax.inject.Inject

class CustomMapMapper @Inject constructor() {
    fun toModel(entity: CustomMap): CustomMapModel {
        return CustomMapModel(
            id = entity.id,
            name = entity.name,
            styleJson = entity.styleJson,
            initialLatitude = entity.initialLatitude,
            initialLongitude = entity.initialLongitude,
            initialZoom = entity.initialZoom,
            isDefault = entity.isDefault
        )
    }

    fun toEntity(model: CustomMapModel): CustomMap {
        return CustomMap(
            id = model.id,
            name = model.name,
            styleJson = model.styleJson,
            initialLatitude = model.initialLatitude,
            initialLongitude = model.initialLongitude,
            initialZoom = model.initialZoom,
            isDefault = model.isDefault
        )
    }
}