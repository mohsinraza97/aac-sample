package com.mohsinsyed.aac_sample.data.repository.post

import com.mohsinsyed.aac_sample.data.models.Response
import com.mohsinsyed.aac_sample.data.models.entities.Post

interface IPostRepository {
    suspend fun create(post: Post?, isWorkRequest: Boolean = false): Response<Post?>

    suspend fun update(post: Post?, isWorkRequest: Boolean = false): Response<Post?>

    suspend fun delete(id: Long?, isWorkRequest: Boolean = false): Response<Unit?>

    suspend fun fetchAll(): Response<List<Post>?>
}