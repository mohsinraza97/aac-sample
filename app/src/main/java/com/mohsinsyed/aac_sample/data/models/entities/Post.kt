package com.mohsinsyed.aac_sample.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.mohsinsyed.aac_sample.data.local.converters.TimestampConverter
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity
class Post : Serializable {
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

    @ColumnInfo(name = "user_id")
    var userId: Long? = AppConstants.NetworkConstants.USER_ID

    var title: String? = null

    @SerializedName("body")
    var description: String? = null
}