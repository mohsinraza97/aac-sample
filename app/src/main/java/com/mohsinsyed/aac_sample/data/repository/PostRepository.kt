package com.mohsinsyed.aac_sample.data.repository

import android.content.Context
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.models.Post
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.local.PostDao
import com.mohsinsyed.aac_sample.data.remote.PostService
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import com.mohsinsyed.aac_sample.utils.utilities.AppUtility
import javax.inject.Inject

class PostRepository @Inject constructor(
    override val context: Context?,
    private val postService: PostService,
    private val postDao: PostDao,
) : BaseRepository(context) {
    suspend fun create(post: Post?): Response<Post?> {
        val dbResponse = sendCoroutineRequest { postDao.insert(post) }
        if (dbResponse is Response.Success) {
            sendCoroutineRequest { postService.create(post) }.also {
                checkIfSyncRequestRequired(it, AppConstants.SyncConstants.TAG_CREATE_POST)
            }
            return sendCoroutineRequest { postDao.findById(dbResponse.value as? Int?) }
        }
        return Response.Failed(context?.getString(R.string.general_ui_error))
    }

    suspend fun update(post: Post?): Response<Post?> {
        val dbResponse = sendCoroutineRequest { postDao.update(post) }
        if (dbResponse is Response.Success) {
            sendCoroutineRequest { postService.update(post, post?.id) }.also {
                checkIfSyncRequestRequired(it, AppConstants.SyncConstants.TAG_UPDATE_POST)
            }
            return sendCoroutineRequest { postDao.findById(post?.id) }
        }
        return Response.Failed(context?.getString(R.string.general_ui_error))
    }

    suspend fun delete(id: Int?): Response<Unit?> {
        val dbResponse = sendCoroutineRequest { postDao.delete(id) }
        if (dbResponse is Response.Success) {
            sendCoroutineRequest { postService.delete(id) }.also {
                checkIfSyncRequestRequired(it, AppConstants.SyncConstants.TAG_DELETE_POST)
            }
            return Response.Success(Unit)
        }
        return Response.Failed(context?.getString(R.string.general_ui_error))
    }

    suspend fun fetchAll(): Response<List<Post>?> {
        val apiResponse = sendCoroutineRequest { postService.fetchAll() }
        if (apiResponse is Response.Success) {
            sendCoroutineRequest { postDao.deleteAll() }
            sendCoroutineRequest { postDao.insertAll(apiResponse.value) }
        }
        return sendCoroutineRequest { postDao.findAll() }
    }

    private fun <T> checkIfSyncRequestRequired(response: Response<T>, tag: String): Boolean {
        if (response is Response.Error) {
            // Create a work-manager sync request in case API call failure for whatever reason
            AppUtility.debugLog("Sync request to be initiated with TAG '$tag' because API call failed due to reason '${response.message}'")
            return true
        }
        return false
    }
}