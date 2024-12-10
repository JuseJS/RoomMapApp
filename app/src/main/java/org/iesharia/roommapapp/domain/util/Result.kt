package org.iesharia.roommapapp.domain.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isLoading() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw error
        is Loading -> throw IllegalStateException("Cannot get data while loading")
    }

    fun getErrorOrNull(): AppError? = when (this) {
        is Error -> error
        else -> null
    }

    // Funciones de transformación
    suspend fun <R> map(transform: suspend (T) -> R): Result<R> = when (this) {
        is Success -> try {
            Success(transform(data))
        } catch (e: Exception) {
            Error(e.toAppError())
        }
        is Error -> this
        is Loading -> this
    }

    suspend fun onSuccess(action: suspend (T) -> Unit): Result<T> = apply {
        if (this is Success) action(data)
    }

    suspend fun onError(action: suspend (AppError) -> Unit): Result<T> = apply {
        if (this is Error) action(error)
    }

    suspend fun onLoading(action: suspend () -> Unit): Result<T> = apply {
        if (this is Loading) action()
    }
}

// Funciones de utilidad para crear Result
suspend fun <T> runCatching(block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Error(e.toAppError())
}

fun <T> Result<T>.requireData(): T = when (this) {
    is Result.Success -> data
    is Result.Error -> throw error
    is Result.Loading -> throw IllegalStateException("Data is not available while loading")
}