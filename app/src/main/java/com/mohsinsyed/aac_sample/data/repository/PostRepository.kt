package com.mohsinsyed.aac_sample.data.repository

import android.content.Context
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.remote.PostService
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.SyncConstants.SYNC_TAG_CREATE_POST
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.SyncConstants.SYNC_TAG_UPDATE_POST
import com.mohsinsyed.aac_sample.utils.constants.AppConstants.SyncConstants.SYNC_TAG_DELETE_POST
import javax.inject.Inject

class PostRepository @Inject constructor(
    override val context: Context?,
    val postService: PostService,
    val postDao: PostDao,
    override val outboxDao: OutboxDao,
) : BaseRepository(context, outboxDao) {

    suspend fun create(post: Post?): Response<Post?> {
        val dbResponse = getResponse { postDao.insert(post) }
        if (dbResponse is Response.Success) {
            addToOutboxWithSyncRequest(post, SYNC_TAG_CREATE_POST)
            return getResponse { postDao.findById(dbResponse.value) }
        }
        return Response.Error(context?.getString(R.string.general_ui_error))
    }

    suspend fun update(post: Post?): Response<Post?> {
        val dbResponse = getResponse { postDao.update(post) }
        if (dbResponse is Response.Success) {
            addToOutboxWithSyncRequest(post, SYNC_TAG_UPDATE_POST)
            return getResponse { postDao.findById(post?.id) }
        }
        return Response.Error(context?.getString(R.string.general_ui_error))
    }

    suspend fun delete(id: Long?): Response<Unit?> {
        val dbResponse = getResponse { postDao.delete(id) }
        if (dbResponse is Response.Success) {
            addToOutboxWithSyncRequest(id, SYNC_TAG_DELETE_POST)
            return Response.Success(Unit)
        }
        return Response.Error(context?.getString(R.string.general_ui_error))
    }

    suspend fun fetchAll(): Response<List<Post>?> {
        val apiResponse = getResponse { postService.fetchAll() }
        if (apiResponse is Response.Success) {
            getResponse { postDao.deleteAll() }
            getResponse { postDao.insertAll(apiResponse.value) }
        }
        return getResponse { postDao.findAll() }
    }
}