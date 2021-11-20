package com.mohsinsyed.aac_sample.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import java.io.Serializable

@Entity
data class Post(
    @ColumnInfo(name = "user_id")
    val userId: Long? = AppConstants.NetworkConstants.USER_ID,

    val title: String? = null,

    @SerializedName("body")
    val description: String? = null,
) : BaseEntity()