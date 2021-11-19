package com.mohsinsyed.aac_sample.data.repository

import android.content.Context
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.remote.Response
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    @ApplicationContext open val context: Context?
) {
    protected inline fun <T> sendCoroutineRequest(call: () -> T): Response<T> {
        return try {
            Response.Success(call())
        } catch (e: HttpException) {
            val firstMessage = when (e.code()) {
                in 500..511 -> context?.getString(R.string.error_internal_server)
                else -> getHttpErrorMessage(e)
            }
            Response.Error(firstMessage ?: e.message() ?: e.toString(), e.code())
        } catch (e: SocketTimeoutException) {
            Response.Error(context?.getString(R.string.error_timeout) ?: e.message ?: e.toString())
        } catch (e: SocketException) {
            Response.Error(context?.getString(R.string.error_internet) ?: e.message ?: e.toString())
        } catch (e: Exception) {
            Response.Error(e.message ?: e.toString())
        }
    }

    protected fun getHttpErrorMessage(e: HttpException): String? {
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