package com.mohsinsyed.aac_sample.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mohsinsyed.aac_sample.data.repository.BaseRepository
import com.mohsinsyed.aac_sample.data.repository.PostRepository
import com.mohsinsyed.aac_sample.utils.utilities.LogUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

@HiltWorker
class PostWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val postRepository: PostRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Result.success()
            } catch (e: Exception) {
                LogUtils.debugLog(e.message ?: e.toString())
                Result.retry()
            }
        }
    }
}