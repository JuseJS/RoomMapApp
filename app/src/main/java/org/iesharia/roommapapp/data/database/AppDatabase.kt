package org.iesharia.roommapapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.iesharia.roommapapp.data.database.dao.CustomMapDao
import org.iesharia.roommapapp.data.database.dao.MarkerDao
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.data.database.entity.CustomMap
import org.iesharia.roommapapp.data.database.entity.MarkerEntity
import org.iesharia.roommapapp.data.database.entity.MarkerType

@Database(
    entities = [
        MarkerEntity::class,
        MarkerType::class,
        CustomMap::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun markerDao(): MarkerDao
    abstract fun markerTypeDao(): MarkerTypeDao
    abstract fun customMapDao(): CustomMapDao
    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Preparar para futuras migraciones
            }
        }
    }
}