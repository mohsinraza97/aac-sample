package com.mohsinsyed.aac_sample.data.core.repository.post

import com.mohsinsyed.aac_sample.data.core.source.post.IPostLocalDataSource
import com.mohsinsyed.aac_sample.data.core.source.post.IPostRemoteDataSource
import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.ui.resources.AppStrings
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val localDataSource: IPostLocalDataSource,
    private val remoteDataSource: IPostRemoteDataSource,
    private val appStrings: AppStrings?,
) : IPostRepository {
    override suspend fun create(post: Post?): Response<Post?> {
        val dbResponse = localDataSource.insert(post)
        if (dbResponse is Response.Success) {
            return localDataSource.find(dbResponse.data)
        }
        return Response.Error(appStrings?.generalUiError())
    }

    override suspend fun update(post: Post?): Response<Post?> {
        val dbResponse = localDataSource.update(post)
        if (dbResponse is Response.Success) {
            return localDataSource.find(post?.id)
        }
        return Response.Error(appStrings?.generalUiError())
    }

    override suspend fun delete(id: Long?): Response<Unit?> {
        val dbResponse = localDataSource.delete(id)
        if (dbResponse is Response.Success) {
            return Response.Success(Unit)
        }
        return Response.Error(appStrings?.generalUiError())
    }

    override suspend fun fetchAll(): Response<List<Post>?> {
        val apiResponse = remoteDataSource.fetchAll()
        if (apiResponse is Response.Success) {
            localDataSource.deleteAll()
            localDataSource.insertAll(apiResponse.data)
        }
        return localDataSource.findAll()
    }
}