package com.mohsinsyed.aac_sample.data.core.local.dao

import androidx.room.*
import com.mohsinsyed.aac_sample.data.models.entities.Outbox

@Dao
interface OutboxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outbox: Outbox?): Long?

    @Update
    suspend fun update(outbox: Outbox?): Int?

    @Query("DELETE FROM outbox where id = :id")
    suspend fun delete(id: Long?): Int?

    @Query("DELETE FROM outbox")
    suspend fun deleteAll(): Int?

    @Query("SELECT * FROM outbox where id = :id")
    suspend fun findById(id: Long?): Outbox?

    @Query("SELECT * FROM outbox where status IN (:statuses)")
    suspend fun findByStatus(vararg statuses: String?): List<Outbox>?

    @Query("SELECT * FROM outbox")
    suspend fun findAll(): List<Outbox>?
}