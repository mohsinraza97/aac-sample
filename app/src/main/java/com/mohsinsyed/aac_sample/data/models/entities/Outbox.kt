package com.mohsinsyed.aac_sample.data.models.entities

import androidx.room.Entity
import com.mohsinsyed.aac_sample.utils.constants.AppConstants

@Entity
data class Outbox(
    val data: String? = null,
    val tag: String? = null,
    var status: String?,
) : BaseEntity()