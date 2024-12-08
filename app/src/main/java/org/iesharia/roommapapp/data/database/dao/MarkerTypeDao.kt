package org.iesharia.roommapapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.data.database.entity.MarkerType

@Dao
interface MarkerTypeDao {
    @Query("SELECT * FROM marker_types")
    fun getAllMarkerTypes(): Flow<List<MarkerType>>

    @Query("SELECT * FROM marker_types WHERE id = :typeId")
    suspend fun getMarkerTypeById(typeId: Long): MarkerType?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkerType(markerType: MarkerType)

    @Update
    suspend fun updateMarkerType(markerType: MarkerType)

    @Delete
    suspend fun deleteMarkerType(markerType: MarkerType)
}