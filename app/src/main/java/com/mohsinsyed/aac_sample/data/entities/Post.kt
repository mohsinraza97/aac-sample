package com.mohsinsyed.aac_sample.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Post(
    @PrimaryKey
    val id: Int?,
    val userId: Int?,
    val title: String?,
    val body: String?
) : Serializable