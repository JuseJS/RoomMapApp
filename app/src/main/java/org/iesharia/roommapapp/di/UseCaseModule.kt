package org.iesharia.roommapapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.iesharia.roommapapp.domain.repository.CustomMapRepository
import org.iesharia.roommapapp.domain.repository.MarkerRepository
import org.iesharia.roommapapp.domain.repository.MarkerTypeRepository
import org.iesharia.roommapapp.domain.usecase.InitializeDataUseCase
import org.iesharia.roommapapp.domain.usecase.custommap.*
import org.iesharia.roommapapp.domain.usecase.marker.*
import org.iesharia.roommapapp.domain.usecase.markertype.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Custom Map UseCases
    @Provides
    @Singleton
    fun provideAddDefaultMapUseCase(
        repository: CustomMapRepository
    ): AddDefaultMapUseCase {
        return AddDefaultMapUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCustomMapUseCase(
        repository: CustomMapRepository,
        addDefaultMapUseCase: AddDefaultMapUseCase
    ): GetCustomMapUseCase {
        return GetCustomMapUseCase(repository, addDefaultMapUseCase)
    }

    @Provides
    @Singleton
    fun provideSetDefaultMapUseCase(
        repository: CustomMapRepository
    ): SetDefaultMapUseCase {
        return SetDefaultMapUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateCustomMapUseCase(
        repository: CustomMapRepository
    ): UpdateCustomMapUseCase {
        return UpdateCustomMapUseCase(repository)
    }

    // Marker UseCases
    @Provides
    @Singleton
    fun provideGetMarkersUseCase(
        repository: MarkerRepository
    ): GetMarkersUseCase {
        return GetMarkersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddMarkerUseCase(
        repository: MarkerRepository
    ): AddMarkerUseCase {
        return AddMarkerUseCase(repository)
    }

    // MarkerType UseCases
    @Provides
    @Singleton
    fun provideGetMarkerTypesUseCase(
        repository: MarkerTypeRepository
    ): GetMarkerTypesUseCase {
        return GetMarkerTypesUseCase(repository)
    }

    // Initialize Data UseCase
    @Provides
    @Singleton
    fun provideInitializeDataUseCase(
        markerRepository: MarkerRepository,
        markerTypeRepository: MarkerTypeRepository
    ): InitializeDataUseCase {
        return InitializeDataUseCase(markerRepository, markerTypeRepository)
    }
}