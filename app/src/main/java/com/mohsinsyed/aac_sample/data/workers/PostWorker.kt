package com.mohsinsyed.aac_sample.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mohsinsyed.aac_sample.data.core.repository.outbox.IOutboxRepository
import com.mohsinsyed.aac_sample.data.core.source.post.IPostRemoteDataSource
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_FAILED
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.DBConstants.OUTBOX_STATUS_IN_PROGRESS
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.SyncConstants.SYNC_TAG_CREATE_POST
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.SyncConstants.SYNC_TAG_DELETE_POST
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.SyncConstants.SYNC_TAG_UPDATE_POST
import com.mohsinsyed.aac_sample.utils.utilities.GsonUtils
import com.mohsinsyed.aac_sample.utils.utilities.LogUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class PostWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val postDataSource: IPostRemoteDataSource,
    private val outboxRepository: IOutboxRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        LogUtils.debugLog("Post worker sync initiating...")
        return withContext(Dispatchers.IO) {
            try {
                var successCount = 0
                val items = outboxRepository.getPendingRequests()
                LogUtils.debugLog("${items?.size ?: 0} outbox pending items found to sync.")

                items?.forEach { item ->
                    outboxRepository.updateStatus(item, OUTBOX_STATUS_IN_PROGRESS)
                    LogUtils.debugLog("Processing: $item")

                    when {
                        item.tag.equals(SYNC_TAG_CREATE_POST) -> {
                            val post = getPost(item)
                            postDataSource.create(post).also {
                                handleResponse(it, item).also { success ->
                                    if (success) successCount++
                                }
                            }
                        }
                        item.tag.equals(SYNC_TAG_UPDATE_POST) -> {
                            val post = getPost(item)
                            postDataSource.update(post).also {
                                handleResponse(it, item).also { success ->
                                    if (success) successCount++
                                }
                            }
                        }
                        item.tag.equals(SYNC_TAG_DELETE_POST) -> {
                            val postId = item.data?.toLongOrNull()
                            postDataSource.delete(postId).also {
                                handleResponse(it, item).also { success ->
                                    if (success) successCount++
                                }
                            }
                        }
                    }
                }
                LogUtils.debugLog("${items?.size ?: 0} items processed.")
                if (items?.size == successCount) {
                    LogUtils.debugLog("Success!")
                    Result.success()
                } else {
                    LogUtils.debugLog("Re-try! Remaining items: ${items?.size?.minus(successCount)}")
                    Result.retry()
                }
            } catch (e: Exception) {
                LogUtils.debugLog("Post worker sync failed due to ${e.message ?: e.toString()}")
                Result.retry()
            }
        }
    }

    private suspend fun <T> handleResponse(response: Response<T>, item: Outbox): Boolean {
        return if (response is Response.Success) {
            outboxRepository.delete(item.id)
            true
        } else {
            LogUtils.debugLog("Failed to process: $item")
            outboxRepository.updateStatus(item, OUTBOX_STATUS_FAILED)
            false
        }
    }

    private fun getPost(item: Outbox) = GsonUtils.fromJson(item.data, Post::class.java)
}