package com.mohsinsyed.aac_sample.data.repository.outbox

import android.content.Context
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import com.mohsinsyed.aac_sample.data.repository.BaseRepository
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_FAILED
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_PENDING
import com.mohsinsyed.aac_sample.utils.utilities.GsonUtils
import com.mohsinsyed.aac_sample.utils.utilities.LogUtils
import com.mohsinsyed.aac_sample.utils.utilities.StringUtils
import com.mohsinsyed.aac_sample.utils.utilities.WorkUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class OutboxRepository @Inject constructor(
    private val stringUtils: StringUtils,
    private val outboxDao: OutboxDao,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseRepository(stringUtils, dispatcher), IOutboxRepository {
    override suspend fun add(value: Any?, tag: String): Response<Long?> {
        val outboxItem = getNewOutboxItem(value, tag)
        val dbResponse = getResponse { outboxDao.insert(outboxItem) }
        if (dbResponse is Response.Success) {
            LogUtils.debugLog("Added to outbox: $outboxItem")
            WorkUtils.createRequest(tag)
        }
        return dbResponse
    }

    override suspend fun getPendingRequests(): List<Outbox>? {
        val dbResponse = getResponse {
            outboxDao.findByStatus(OUTBOX_STATUS_PENDING, OUTBOX_STATUS_FAILED)
        }
        if (dbResponse is Response.Success) {
            return dbResponse.data
        }
        return null
    }

    override suspend fun updateStatus(item: Outbox?, status: String?): Boolean {
        item?.status = status
        val dbResponse = getResponse { outboxDao.update(item) }
        return dbResponse is Response.Success
    }

    override suspend fun delete(id: Long?): Boolean {
        val dbResponse = getResponse { outboxDao.delete(id) }
        return dbResponse is Response.Success
    }

    private fun getNewOutboxItem(value: Any?, tag: String? = null): Outbox {
        val data = GsonUtils.toJson(value)
        return Outbox(data, tag, OUTBOX_STATUS_PENDING)
    }
}