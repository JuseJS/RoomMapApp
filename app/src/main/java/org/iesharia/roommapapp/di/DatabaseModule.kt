package org.iesharia.roommapapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.iesharia.roommapapp.data.database.AppDatabase
import org.iesharia.roommapapp.data.database.dao.CustomMapDao
import org.iesharia.roommapapp.data.database.dao.MarkerDao
import org.iesharia.roommapapp.data.database.dao.MarkerTypeDao
import org.iesharia.roommapapp.domain.util.Constants
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
            Constants.Database.DATABASE_NAME
        )
            .enableMultiInstanceInvalidation()
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
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