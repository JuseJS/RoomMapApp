package org.iesharia.roommapapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.iesharia.roommapapp.data.database.AppDatabase
import org.iesharia.roommapapp.data.database.dao.CustomMapDao
import org.iesharia.roommapapp.data.database.dao.MarkerDao
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideMarkerDao(database: AppDatabase): MarkerDao {
        return database.markerDao()
    }

    @Provides
    fun provideMarkerTypeDao(database: AppDatabase): MarkerTypeDao {
        return database.markerTypeDao()
    }

    @Provides
    fun provideCustomMapDao(database: AppDatabase): CustomMapDao {
        return database.customMapDao()
    }
}