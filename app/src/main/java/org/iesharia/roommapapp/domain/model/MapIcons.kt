package org.iesharia.roommapapp.domain.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import org.iesharia.roommapapp.R
import org.osmdroid.views.overlay.Marker

sealed class MapIcon(val resId: Int) {
    object Default : MapIcon(R.drawable.ic_marker_default)
    object Restaurant : MapIcon(R.drawable.ic_marker_restaurant)
    object Hotel : MapIcon(R.drawable.ic_marker_hotel)
    object Monument : MapIcon(R.drawable.ic_marker_monument)
    object Park : MapIcon(R.drawable.ic_marker_park)

    companion object {
        fun fromType(markerType: MarkerType): MapIcon {
            return when (markerType) {
                MarkerType.RESTAURANT -> Restaurant
                MarkerType.HOTEL -> Hotel
                MarkerType.MONUMENT -> Monument
                MarkerType.PARK -> Park
                MarkerType.DEFAULT -> Default
            }
        }
    }
}

fun Marker.setIconFromType(context: Context, type: MarkerType) {
    val icon = MapIcon.fromType(type)
    icon.getDrawable(context)?.let { drawable ->
        setIcon(drawable)
    }
}

private fun MapIcon.getDrawable(context: Context): Drawable? {
    return ContextCompat.getDrawable(context, resId)
}