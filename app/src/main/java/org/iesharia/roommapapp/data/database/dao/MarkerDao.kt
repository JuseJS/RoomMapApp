package org.iesharia.roommapapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.iesharia.roommapapp.data.database.entity.MarkerEntity

@Dao
interface MarkerDao {
    @Query("SELECT * FROM markers")
    fun getAllMarkers(): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM markers WHERE mapId = :mapId")
    fun getMarkersByMapId(mapId: Long): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM markers WHERE typeId = :typeId")
    fun getMarkersByTypeId(typeId: Long): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM markers WHERE id = :markerId")
    suspend fun getMarkerById(markerId: Long): MarkerEntity?

    @Query("""
        SELECT * FROM markers 
        WHERE latitude BETWEEN :minLat AND :maxLat 
        AND longitude BETWEEN :minLon AND :maxLon
    """)
    fun getMarkersInBoundingBox(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Flow<List<MarkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(marker: MarkerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkers(markers: List<MarkerEntity>): List<Long>

    @Update
    suspend fun updateMarker(marker: MarkerEntity)

    @Delete
    suspend fun deleteMarker(marker: MarkerEntity)

    @Query("DELETE FROM markers WHERE mapId = :mapId")
    suspend fun deleteMarkersByMapId(mapId: Long)

    @Query("DELETE FROM markers")
    suspend fun deleteAllMarkers()

    @Transaction
    @Query("""
        SELECT * FROM markers 
        WHERE (:mapId IS NULL OR mapId = :mapId)
        AND (:typeId IS NULL OR typeId = :typeId)
    """)
    fun getFilteredMarkers(
        mapId: Long? = null,
        typeId: Long? = null
    ): Flow<List<MarkerEntity>>

    @Query("""
        SELECT COUNT(*) FROM markers 
        WHERE typeId = :typeId
    """)
    fun getMarkerCountByType(typeId: Long): Flow<Int>
}