package com.mohsinsyed.aac_sample.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import java.io.Serializable

@Entity
data class Post(
    @PrimaryKey
    var id: Int? = null,
    val userId: Int? = AppConstants.NetworkConstants.USER_ID,
    val title: String? = null,
    val body: String? = null,
) : Serializable