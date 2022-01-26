package com.mohsinsyed.aac_sample.data.repository.outbox

import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Outbox

interface IOutboxRepository {
    suspend fun add(value: Any?, tag: String): Response<Long?>

    suspend fun getPendingRequests(): List<Outbox>?

    suspend fun updateStatus(item: Outbox?, status: String?): Boolean

    suspend fun delete(id: Long?): Boolean
}