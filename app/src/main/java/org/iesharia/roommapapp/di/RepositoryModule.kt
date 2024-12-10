package org.iesharia.roommapapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.iesharia.roommapapp.data.repository.CustomMapRepositoryImpl
import org.iesharia.roommapapp.data.repository.MarkerRepositoryImpl
import org.iesharia.roommapapp.data.repository.MarkerTypeRepositoryImpl
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarkerRepository(
        repository: MarkerRepositoryImpl
    ): MarkerRepository

    @Binds
    @Singleton
    abstract fun bindMarkerTypeRepository(
        repository: MarkerTypeRepositoryImpl
    ): MarkerTypeRepository

    @Binds
    @Singleton
    abstract fun bindCustomMapRepository(
        repository: CustomMapRepositoryImpl
    ): CustomMapRepository
}