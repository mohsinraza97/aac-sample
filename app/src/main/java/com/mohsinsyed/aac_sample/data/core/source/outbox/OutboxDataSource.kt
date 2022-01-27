package com.mohsinsyed.aac_sample.data.core.source.outbox

import com.mohsinsyed.aac_sample.data.core.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.core.source.BaseDataSource
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import com.mohsinsyed.aac_sample.ui.resources.AppStrings
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_FAILED
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_PENDING
import com.mohsinsyed.aac_sample.utils.utilities.GsonUtils
import com.mohsinsyed.aac_sample.utils.utilities.LogUtils
import com.mohsinsyed.aac_sample.utils.utilities.WorkUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface IOutboxDataSource {
    suspend fun add(value: Any?, tag: String): Boolean
    suspend fun getPendingRequests(): List<Outbox>?
    suspend fun updateStatus(item: Outbox?, status: String?): Boolean
    suspend fun delete(id: Long?): Boolean
}

class OutboxDataSource @Inject constructor(
    private val dao: OutboxDao,
    appStrings: AppStrings?,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseDataSource(appStrings, dispatcher), IOutboxDataSource {
    override suspend fun add(value: Any?, tag: String): Boolean {
        val outboxItem = getNewOutboxItem(value, tag)
        val dbResponse = getResponse { dao.insert(outboxItem) }
        if (dbResponse is Response.Success) {
            LogUtils.debugLog("Added to outbox: $outboxItem")
            WorkUtils.createRequest(tag)
            return true
        }
        return false
    }

    override suspend fun getPendingRequests(): List<Outbox>? {
        val dbResponse = getResponse {
            dao.findByStatus(OUTBOX_STATUS_PENDING, OUTBOX_STATUS_FAILED)
        }
        if (dbResponse is Response.Success) {
            return dbResponse.data
        }
        return null
    }

    override suspend fun updateStatus(item: Outbox?, status: String?): Boolean {
        item?.status = status
        val dbResponse = getResponse { dao.update(item) }
        return dbResponse is Response.Success
    }

    override suspend fun delete(id: Long?): Boolean {
        val dbResponse = getResponse { dao.delete(id) }
        return dbResponse is Response.Success
    }

    private fun getNewOutboxItem(value: Any?, tag: String? = null): Outbox {
        val data = GsonUtils.toJson(value)
        return Outbox(data, tag, OUTBOX_STATUS_PENDING)
    }
}