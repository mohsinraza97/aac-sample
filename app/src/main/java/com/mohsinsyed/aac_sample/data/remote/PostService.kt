package com.mohsinsyed.aac_sample.data.remote

import com.mohsinsyed.aac_sample.data.models.Post
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import retrofit2.http.*

interface PostService {
    @POST(AppConstants.NetworkConstants.CREATE_POST)
    suspend fun create(@Body post: Post?): Post?

    @PUT(AppConstants.NetworkConstants.MODIFY_POST)
    suspend fun update(@Body post: Post?, @Path("postId") id: Int?): Post?

    @DELETE(AppConstants.NetworkConstants.MODIFY_POST)
    suspend fun delete(@Path("postId") id: Int?): Unit?

    @GET(AppConstants.NetworkConstants.FETCH_POSTS_BY_USER_ID)
    suspend fun fetchAll() : List<Post>?
}