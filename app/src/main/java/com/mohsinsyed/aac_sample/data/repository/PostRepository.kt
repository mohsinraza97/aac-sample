package com.mohsinsyed.aac_sample.data.repository

import android.content.Context
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.data.entities.Post
import com.mohsinsyed.aac_sample.data.remote.Response
import com.mohsinsyed.aac_sample.data.local.PostDao
import com.mohsinsyed.aac_sample.data.remote.PostService
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

class PostRepository @Inject constructor(
    override val context: Context?,
    private val postService: PostService,
    private val postDao: PostDao
) : BaseRepository(context) {
    suspend fun create(post: Post?): Response<Post?> {
        return networkRequest { postService.create(post) }
    }

    suspend fun update(post: Post?, id: Int?): Response<Post?> {
        return networkRequest { postService.update(post, id) }
    }

    suspend fun delete(id: Int?): Response<Unit?> {
        return networkRequest { postService.delete(id) }
    }

    suspend fun fetchAll(): Response<List<Post>?> {
        return networkRequest { postService.fetchAll() }
    }
}