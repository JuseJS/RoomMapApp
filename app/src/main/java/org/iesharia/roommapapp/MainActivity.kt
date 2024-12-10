package org.iesharia.roommapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.iesharia.roommapapp.presentation.ui.screens.MainScreen
import org.iesharia.roommapapp.presentation.ui.theme.RoomMapAppTheme
import org.iesharia.roommapapp.util.PermissionsHandler
import org.osmdroid.config.Configuration
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initializeOSMDroid()

        PermissionsHandler.registerPermissions(
            activity = this,
            onPermissionsGranted = {
                lifecycleScope.launch {
                    initializeContent()
                }
            },
            onPermissionsDenied = {
                // Manejar caso de permisos denegados
            }
        )
    }

    private fun initializeOSMDroid() {
        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = packageName

        // Configurar directorios de cache
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConfig.osmdroidBasePath = basePath
        val tileCache = File(osmConfig.osmdroidBasePath.absolutePath, "tile")
        osmConfig.osmdroidTileCache = tileCache

        // Configurar cache
        osmConfig.tileFileSystemCacheMaxBytes = 100L * 1024 * 1024 // 100MB
        osmConfig.tileFileSystemCacheTrimBytes = 80L * 1024 * 1024 // 80MB
        osmConfig.cacheMapTileCount = 12
    }

    private fun initializeContent() {
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            RoomMapAppTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(this, getPreferences(MODE_PRIVATE))
    }
}