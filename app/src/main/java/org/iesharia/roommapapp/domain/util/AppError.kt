package org.iesharia.roommapapp.domain.util

sealed class AppError : Exception() {
    data class DatabaseError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError()

    data class NetworkError(
        override val message: String,
        val code: Int? = null
    ) : AppError()

    data class LocationError(
        override val message: String
    ) : AppError()

    data class ValidationError(
        override val message: String
    ) : AppError()

    data class PermissionError(
        override val message: String
    ) : AppError()

    data class MapError(
        override val message: String
    ) : AppError()
}

fun Exception.toAppError(): AppError {
    return when (this) {
        is AppError -> this
        is android.database.sqlite.SQLiteException ->
            AppError.DatabaseError(message ?: "Error de base de datos", this)
        is SecurityException ->
            AppError.PermissionError(message ?: "Error de permisos")
        else -> AppError.MapError(message ?: "Error desconocido")
    }
}