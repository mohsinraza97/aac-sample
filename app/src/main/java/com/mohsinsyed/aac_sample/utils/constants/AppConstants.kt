package com.mohsinsyed.aac_sample.utils.constants

object AppConstants {

    object NetworkConstants {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val USER_ID = 1 // Hard-coded user id

        const val CREATE_POST = "posts" // POST
        const val MODIFY_POST = "posts/{postId}" // PUT, DELETE
        const val FETCH_POSTS_BY_USER_ID = "posts?userId=$USER_ID" // GET
    }

    object DBConstants {
        const val DB_NAME = "AAC_Database"
    }
}