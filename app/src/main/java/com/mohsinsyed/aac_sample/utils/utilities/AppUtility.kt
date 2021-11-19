package com.mohsinsyed.aac_sample.utils.utilities

import timber.log.Timber

object AppUtility {

    @JvmStatic
    fun debugLog(message: String) {
        Timber.d(message)
    }
}