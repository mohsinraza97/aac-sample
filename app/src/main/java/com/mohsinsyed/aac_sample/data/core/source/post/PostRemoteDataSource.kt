package com.mohsinsyed.aac_sample.data.core.source.post

import com.mohsinsyed.aac_sample.data.core.remote.PostService
import com.mohsinsyed.aac_sample.data.core.source.BaseDataSource
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.ui.resources.AppStrings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface IPostRemoteDataSource {
    suspend fun create(post: Post?): Response<Post?>
    suspend fun update(post: Post?): Response<Post?>
    suspend fun delete(id: Long?): Response<Unit?>
    suspend fun fetchAll(): Response<List<Post>?>
}

class PostRemoteDataSource @Inject constructor(
    private val service: PostService,
    appStrings: AppStrings?,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseDataSource(appStrings, dispatcher), IPostRemoteDataSource {
    override suspend fun create(post: Post?): Response<Post?> {
        return getResponse { service.create(post) }
    }

    override suspend fun update(post: Post?): Response<Post?> {
       return getResponse { service.update(post, post?.id) }
    }

    override suspend fun delete(id: Long?): Response<Unit?> {
        return getResponse { service.delete(id) }
    }

    override suspend fun fetchAll(): Response<List<Post>?> {
        return getResponse { service.fetchAll() }
    }
}