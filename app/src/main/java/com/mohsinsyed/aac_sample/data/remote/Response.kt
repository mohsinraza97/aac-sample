package com.mohsinsyed.aac_sample.data.remote

sealed class Response<T> {
    data class Success<T>(val value: T) : Response<T>()
    data class Error<T>(val message: String?, val errorCode: Int? = null) : Response<T>()
    data class Failed<T>(val message: String?) : Response<T>()
}