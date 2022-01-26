package com.mohsinsyed.aac_sample.data.repository

import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.utils.utilities.StringUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    private val stringUtils: StringUtils,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun <T> getResponse(call: suspend () -> T): Response<T> = withContext(dispatcher) {
        return@withContext try {
            Response.Success(call())
        } catch (e: HttpException) {
            val firstMessage = when (e.code()) {
                in 500..511 -> stringUtils.internalServerError()
                else -> getHttpErrorMessage(e)
            }
            Response.Error(firstMessage ?: e.message() ?: e.toString(), e.code())
        } catch (e: SocketTimeoutException) {
            Response.Error(stringUtils.timeoutError())
        } catch (e: SocketException) {
            Response.Error(stringUtils.noInternetError())
        } catch (e: Exception) {
            Response.Error(e.message ?: e.toString())
        }
    }

    private fun getHttpErrorMessage(e: HttpException): String? {
        return try {
            val errorJson = e.response()?.errorBody()?.string()?.let(::JSONObject)
            val errors = errorJson?.getJSONObject("errors")
            val firstKey = errors?.keys()?.asSequence()?.firstOrNull()
            errors?.getJSONArray(firstKey)?.getString(0)
        } catch (e: JSONException) {
            null
        }
    }
}