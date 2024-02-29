package com.engineerfred.kotlin.ktor.util

sealed class Response<out T> {
    data object Undefined: Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error( val errorMessage: String ) : Response<Nothing>()
}