package com.mohsinsyed.aac_sample.data.core.repository.post

import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Post

interface IPostRepository {
    suspend fun create(post: Post?): Response<Post?>
    suspend fun update(post: Post?): Response<Post?>
    suspend fun delete(id: Long?): Response<Unit?>
    suspend fun fetchAll(): Response<List<Post>?>
}