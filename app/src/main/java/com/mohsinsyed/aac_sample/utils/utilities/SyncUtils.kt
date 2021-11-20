package com.mohsinsyed.aac_sample.utils.utilities

import android.content.Context
import androidx.work.*
import com.mohsinsyed.aac_sample.data.sync.PostWorker
import java.util.concurrent.TimeUnit

object SyncUtils {

    fun createRequest(context: Context, tag: String) {
        val isRunning = isRequestRunning(context, tag)
        if (!isRunning) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<PostWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,
                    WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                    TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(tag,
                ExistingWorkPolicy.KEEP,
                request)
        }
    }

    private fun isRequestRunning(context: Context, tag: String): Boolean {
        val instance = WorkManager.getInstance(context)
        val workInfoList = instance.getWorkInfosByTag(tag).get()
        return try {
            var running = false
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            running
        } catch (e: Exception) {
            LogUtils.debugLog(e.message ?: e.toString())
            false
        }
    }
}