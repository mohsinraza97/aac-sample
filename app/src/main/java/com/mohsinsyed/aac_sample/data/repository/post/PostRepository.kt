package com.mohsinsyed.aac_sample.data.repository.post

import com.mohsinsyed.aac_sample.data.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.data.remote.PostService
import com.mohsinsyed.aac_sample.data.repository.BaseRepository
import com.mohsinsyed.aac_sample.utils.utilities.StringUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val stringUtils: StringUtils,
    private val postService: PostService,
    private val postDao: PostDao,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseRepository(stringUtils, dispatcher), IPostRepository {
    override suspend fun create(post: Post?, isWorkRequest: Boolean): Response<Post?> {
        if (isWorkRequest) {
            return getResponse { postService.create(post) }
        }
        val dbResponse = getResponse { postDao.insert(post) }
        if (dbResponse is Response.Success) {
            return getResponse { postDao.findById(dbResponse.data) }
        }
        return Response.Error(stringUtils.generalUiError())
    }

    override suspend fun update(post: Post?, isWorkRequest: Boolean): Response<Post?> {
        if (isWorkRequest) {
            return getResponse { postService.update(post, post?.id) }
        }
        val dbResponse = getResponse { postDao.update(post) }
        if (dbResponse is Response.Success) {
            return getResponse { postDao.findById(post?.id) }
        }
        return Response.Error(stringUtils.generalUiError())
    }

    override suspend fun delete(id: Long?, isWorkRequest: Boolean): Response<Unit?> {
        if (isWorkRequest) {
            return getResponse { postService.delete(id) }
        }
        val dbResponse = getResponse { postDao.delete(id) }
        if (dbResponse is Response.Success) {
            return Response.Success(Unit)
        }
        return Response.Error(stringUtils.generalUiError())
    }

    override suspend fun fetchAll(): Response<List<Post>?> {
        val apiResponse = getResponse { postService.fetchAll() }
        if (apiResponse is Response.Success) {
            getResponse { postDao.deleteAll() }
            getResponse { postDao.insertAll(apiResponse.data) }
        }
        return getResponse { postDao.findAll() }
    }
}