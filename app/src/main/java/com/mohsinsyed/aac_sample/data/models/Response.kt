package com.mohsinsyed.aac_sample.data.models

sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val message: String? = null, val errorCode: Int? = null) : Response<T>()
}