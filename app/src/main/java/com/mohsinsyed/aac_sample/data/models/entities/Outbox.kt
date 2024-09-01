package com.mohsinsyed.aac_sample.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mohsinsyed.aac_sample.data.local.converters.TimestampConverter
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity class Outbox : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "unique_id")
    var uniqueId: String = UUID.randomUUID().toString()

    @ColumnInfo(name = "created_at")
    @TypeConverters(TimestampConverter::class)
    var createdAt: Date? = Date()

    @ColumnInfo(name = "updated_at")
    @TypeConverters(TimestampConverter::class)
    var updatedAt: Date? = Date()

    var data: String? = null

    var tag: String? = null

    var status: String? = null
}