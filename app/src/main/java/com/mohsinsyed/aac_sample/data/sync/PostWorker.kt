package com.mohsinsyed.aac_sample.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.data.repository.PostRepository
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
import java.lang.Exception

@HiltWorker
class PostWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: PostRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        LogUtils.debugLog("Post worker sync initiating...")
        return withContext(Dispatchers.IO) {
            try {
                val outboxDao = repository.outboxDao
                val postService = repository.postService
                val items = repository.fetchOutboxPendingRequests()

                LogUtils.debugLog("${items?.size ?: 0} outbox pending items found to sync.")
                var successCount = 0

                items?.forEach { item ->
                    repository.updateOutboxItemStatus(item, OUTBOX_STATUS_IN_PROGRESS)
                    LogUtils.debugLog("Processing: $item")

                    when {
                        item.tag.equals(SYNC_TAG_CREATE_POST) -> {
                            val post = getPost(item)
                            repository.getResponse { postService.create(post) }.also {
                                handleResponse(it, outboxDao, item).also { success -> if (success) successCount++ }
                            }
                        }
                        item.tag.equals(SYNC_TAG_UPDATE_POST) -> {
                            val post = getPost(item)
                            repository.getResponse { postService.update(post, post?.id) }.also {
                                handleResponse(it, outboxDao, item).also { success -> if (success) successCount++ }
                            }
                        }
                        item.tag.equals(SYNC_TAG_DELETE_POST) -> {
                            val postId = item.data?.toLongOrNull()
                            repository.getResponse { postService.delete(postId) }.also {
                                handleResponse(it, outboxDao, item).also { success -> if (success) successCount++ }
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

    private suspend fun <T> handleResponse(
        response: Response<T>,
        outboxDao: OutboxDao,
        item: Outbox,
    ): Boolean {
        return if (response is Response.Success) {
            repository.getResponse { outboxDao.delete(item.id) }
            true
        } else {
            LogUtils.debugLog("Failed to process: $item")
            repository.updateOutboxItemStatus(item, OUTBOX_STATUS_FAILED)
            false
        }
    }

    private fun getPost(item: Outbox) = GsonUtils.fromJson(item.data, Post::class.java)
}