package com.mohsinsyed.aac_sample.data.models.entities

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.mohsinsyed.aac_sample.data.core.local.converters.TimestampConverter
import java.io.Serializable
import java.util.*

@Entity
abstract class BaseEntity : Serializable {
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
}