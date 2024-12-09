package org.iesharia.roommapapp.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.roommapapp.data.database.entity.CustomMap

@Composable
fun MapStyleDrawer(
    drawerState: DrawerState,
    availableMaps: List<CustomMap>,
    currentMapId: Long?,
    onMapSelected: (CustomMap) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Estilos de Mapa",
                modifier = Modifier.padding(16.dp)
            )
            availableMaps.forEach { map ->
                NavigationDrawerItem(
                    label = { Text(map.name) },
                    selected = map.id == currentMapId,
                    onClick = { onMapSelected(map) }
                )
            }
        }
    }
}