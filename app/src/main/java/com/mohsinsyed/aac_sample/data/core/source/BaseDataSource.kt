package com.mohsinsyed.aac_sample.data.core.source

import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.ui.resources.AppStrings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseDataSource @Inject constructor(
    private val appStrings: AppStrings?,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
)  {
    protected suspend fun <T> getResponse(call: suspend () -> T): Response<T> = withContext(dispatcher) {
        return@withContext try {
            Response.Success(call())
        } catch (e: HttpException) {
            val firstMessage = when (e.code()) {
                in 500..511 -> appStrings?.internalServerError()
                else -> getHttpErrorMessage(e)
            }
            Response.Error(firstMessage ?: e.message() ?: e.toString(), e.code())
        } catch (e: SocketTimeoutException) {
            Response.Error(appStrings?.timeoutError())
        } catch (e: SocketException) {
            Response.Error(appStrings?.noInternetError())
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