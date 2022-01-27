package com.mohsinsyed.aac_sample.data.core.source.post

import com.mohsinsyed.aac_sample.data.core.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.core.source.BaseDataSource
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.ui.resources.AppStrings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface IPostLocalDataSource {
    suspend fun insert(post: Post?): Response<Long?>
    suspend fun insertAll(posts: List<Post>?): Response<List<Long>?>
    suspend fun update(post: Post?): Response<Int?>
    suspend fun delete(id: Long?): Response<Int?>
    suspend fun deleteAll(): Response<Int?>
    suspend fun find(id: Long?): Response<Post?>
    suspend fun findAll(): Response<List<Post>?>
}

class PostLocalDataSource @Inject constructor(
    private val dao: PostDao,
    appStrings: AppStrings?,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseDataSource(appStrings, dispatcher), IPostLocalDataSource {
    override suspend fun insert(post: Post?): Response<Long?> {
        return getResponse { dao.insert(post) }
    }

    override suspend fun insertAll(posts: List<Post>?): Response<List<Long>?> {
        return getResponse { dao.insertAll(posts) }
    }

    override suspend fun update(post: Post?): Response<Int?> {
        return getResponse { dao.update(post) }
    }

    override suspend fun delete(id: Long?): Response<Int?> {
        return getResponse { dao.delete(id) }
    }

    override suspend fun deleteAll(): Response<Int?> {
        return getResponse { dao.deleteAll() }
    }

    override suspend fun find(id: Long?): Response<Post?> {
        return getResponse { dao.findById(id) }
    }

    override suspend fun findAll(): Response<List<Post>?> {
        return getResponse { dao.findAll() }
    }
}