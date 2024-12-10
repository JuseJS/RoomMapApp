package org.iesharia.roommapapp.domain.util

sealed class AppError : Exception() {
    data class DatabaseError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError()

    data class NetworkError(
        override val message: String,
        val code: Int? = null,
        override val cause: Throwable? = null
    ) : AppError()

    data class LocationError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError()

    data class ValidationError(
        override val message: String,
        val field: String? = null
    ) : AppError()

    data class PermissionError(
        override val message: String,
        val permission: String? = null
    ) : AppError()

    data class MapError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError()

    data class UnknownError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError()

    fun getUserMessage(): String = when (this) {
        is DatabaseError -> "Error en la base de datos: $message"
        is NetworkError -> "Error de conexión: $message"
        is LocationError -> "Error de ubicación: $message"
        is ValidationError -> "Error de validación${field?.let { " en $it" } ?: ""}: $message"
        is PermissionError -> "Permiso denegado${permission?.let { " para $it" } ?: ""}: $message"
        is MapError -> "Error en el mapa: $message"
        is UnknownError -> message
    }
}

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is android.database.sqlite.SQLiteException ->
        AppError.DatabaseError("Error en la operación de base de datos", this)
    is SecurityException ->
        AppError.PermissionError("Permiso denegado", this.message)
    is IllegalArgumentException ->
        AppError.ValidationError(message ?: "Valor inválido")
    is IllegalStateException ->
        AppError.UnknownError(message ?: "Estado inválido de la aplicación", this)
    else -> AppError.UnknownError(message ?: "Error desconocido", this)
}