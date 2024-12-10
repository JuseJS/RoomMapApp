package org.iesharia.roommapapp.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.roommapapp.domain.model.CustomMapModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapStyleDrawer(
    drawerState: DrawerState,
    availableMaps: List<CustomMapModel> = emptyList(),
    currentMapId: Long? = null,
    onMapSelected: (Long) -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Estilos de Mapa",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Divider()
        availableMaps.forEach { map ->
            NavigationDrawerItem(
                label = { Text(map.name) },
                selected = map.id == currentMapId,
                onClick = { onMapSelected(map.id) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}