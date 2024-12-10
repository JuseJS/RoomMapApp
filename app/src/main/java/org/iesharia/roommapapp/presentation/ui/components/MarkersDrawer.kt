package org.iesharia.roommapapp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.iesharia.roommapapp.domain.model.MarkerModel
import org.iesharia.roommapapp.domain.model.MapIconType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerDrawer(
    drawerState: DrawerState,
    availibleMarkers: List<MarkerModel> = emptyList(),
    onNavigateToMarker: (Double, Double) -> Unit = { _, _ -> }
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Marcadores Disponibles",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider()

        val groupedMarkers = availibleMarkers.groupBy { it.type }

        LazyColumn {
            groupedMarkers.forEach { (type, markers) ->
                item {
                    var expanded by remember { mutableStateOf(true) }

                    ListItem(
                        headlineContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = when (MapIconType.fromTypeId(type.id)) {
                                        MapIconType.RESTAURANT -> Icons.Default.Restaurant
                                        MapIconType.HOTEL -> Icons.Default.Hotel
                                        MapIconType.MONUMENT -> Icons.Default.Museum
                                        MapIconType.PARK -> Icons.Default.Park
                                        MapIconType.DEFAULT -> Icons.Default.Place
                                    },
                                    contentDescription = type.name,
                                    tint = Color(android.graphics.Color.parseColor(type.color)),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "${type.name} (${markers.size})",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        },
                        trailingContent = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = if (expanded) "Colapsar" else "Expandir"
                                )
                            }
                        },
                        modifier = Modifier.clickable { expanded = !expanded }
                    )

                    if (expanded) {
                        markers.forEach { marker ->
                            ListItem(
                                headlineContent = { Text(marker.title) },
                                supportingContent = marker.description?.let { { Text(it) } },
                                leadingContent = {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(android.graphics.Color.parseColor(type.color))
                                    )
                                },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable {
                                        onNavigateToMarker(marker.latitude, marker.longitude)
                                    }
                            )
                        }
                    }

                    HorizontalDivider()
                }
            }
        }
    }
}