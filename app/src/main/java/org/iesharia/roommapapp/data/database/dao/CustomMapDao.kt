package org.iesharia.roommapapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.data.database.entity.CustomMap

@Dao
interface CustomMapDao {
    @Query("SELECT * FROM custom_maps")
    fun getAllCustomMaps(): Flow<List<CustomMap>>

    @Query("SELECT * FROM custom_maps WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultMap(): CustomMap?

    @Query("SELECT * FROM custom_maps WHERE id = :mapId")
    suspend fun getCustomMapById(mapId: Long): CustomMap?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomMap(customMap: CustomMap)

    @Update
    suspend fun updateCustomMap(customMap: CustomMap)

    @Delete
    suspend fun deleteCustomMap(customMap: CustomMap)

    @Query("UPDATE custom_maps SET isDefault = 0 WHERE isDefault = 1")
    suspend fun clearDefaultMap()

    @Transaction
    suspend fun setDefaultMap(mapId: Long) {
        clearDefaultMap()
        getCustomMapById(mapId)?.let { map ->
            updateCustomMap(map.copy(isDefault = true))
        }
    }
}