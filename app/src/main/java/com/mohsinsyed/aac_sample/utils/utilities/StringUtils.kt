package com.mohsinsyed.aac_sample.utils.utilities

import android.app.Application
import androidx.annotation.StringRes
import com.mohsinsyed.aac_sample.R
import javax.inject.Inject

class StringUtils @Inject constructor(
    private val app: Application,
) {
    fun getMessage(@StringRes resId: Int): String = app.getString(resId)

    fun internalServerError() = getMessage(R.string.error_internal_server)
    fun noInternetError() = getMessage(R.string.error_internet)
    fun timeoutError() = getMessage(R.string.error_timeout)
    fun generalUiError() = getMessage(R.string.general_ui_error)

    fun notImplementedYet() = getMessage(R.string.not_implemented_yet)
}