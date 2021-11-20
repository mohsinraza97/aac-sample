package com.mohsinsyed.aac_sample.data.repository

import android.content.Context
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_FAILED
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_PENDING
import com.mohsinsyed.aac_sample.utils.utilities.GsonUtils
import com.mohsinsyed.aac_sample.utils.utilities.LogUtils
import com.mohsinsyed.aac_sample.utils.utilities.SyncUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    @ApplicationContext open val context: Context?,
    open val outboxDao: OutboxDao,
) {
    inline fun <T> sendCoroutineRequest(call: () -> T): Response<T> {
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

    fun getHttpErrorMessage(e: HttpException): String? {
        return try {
            val errorJson = e.response()?.errorBody()?.string()?.let(::JSONObject)
            val errors = errorJson?.getJSONObject("errors")
            val firstKey = errors?.keys()?.asSequence()?.firstOrNull()
            errors?.getJSONArray(firstKey)?.getString(0)
        } catch (e: JSONException) {
            null
        }
    }

    // region local-data source
    suspend fun <T> addToOutboxWithSyncRequest(
        apiResponse: Response<T>,
        value: Any?,
        tag: String,
    ) {
        if (apiResponse is Response.Error) {
            val outboxItem = getOutboxItem(value, tag)
            val outboxResponse = sendCoroutineRequest { outboxDao.insert(outboxItem) }
            if (outboxResponse is Response.Success) {
                LogUtils.debugLog("Added to outbox: $outboxItem")
                context?.let { SyncUtils.createRequest(it, tag) }
            }
        }
    }

    private fun getOutboxItem(value: Any?, tag: String): Outbox {
        val data = GsonUtils.toJson(value)
        return Outbox(data, tag, OUTBOX_STATUS_PENDING)
    }

    suspend fun fetchOutboxPendingRequests(): List<Outbox>? {
        val dbResponse = sendCoroutineRequest { outboxDao.findByStatus(OUTBOX_STATUS_PENDING, OUTBOX_STATUS_FAILED) }
        if (dbResponse is Response.Success) {
            return dbResponse.value
        }
        return null
    }
    // endregion
}