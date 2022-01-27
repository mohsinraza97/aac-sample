package com.mohsinsyed.aac_sample.data.core.repository.outbox

import com.mohsinsyed.aac_sample.data.core.source.outbox.IOutboxDataSource
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import javax.inject.Inject

class OutboxRepository @Inject constructor(
    private val dataSource: IOutboxDataSource,
) : IOutboxRepository {
    override suspend fun add(value: Any?, tag: String): Boolean {
        return dataSource.add(value, tag)
    }

    override suspend fun getPendingRequests(): List<Outbox>? {
        return dataSource.getPendingRequests()
    }

    override suspend fun updateStatus(item: Outbox?, status: String?): Boolean {
        return dataSource.updateStatus(item, status)
    }

    override suspend fun delete(id: Long?): Boolean {
        return dataSource.delete(id)
    }
}