package com.mohsinsyed.aac_sample.utils.utilities

import android.util.Log

object LogUtils {

    private const val TAG = "Post_TAG"

    @JvmStatic
    fun debugLog(message: String?) {
        message?.let {
            Log.d(TAG, getInfo() + message)
        }
    }

    @JvmStatic
    fun debugLog(message: String?, vararg value: String?) {
        var completeValue = ""
        for (s in value) {
            completeValue += "$s \n"
        }
        Log.d(TAG, "${getInfo()}$message --> $completeValue")
    }

    private fun getInfo(): String {
        val stackTrace = Exception().stackTrace[2]
        var fileName = stackTrace.fileName
        if (fileName == null) fileName = ""
        return ("(" + fileName + ":" + stackTrace.lineNumber + ") -> " + stackTrace.methodName + "() = ")
    }
}