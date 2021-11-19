package com.mohsinsyed.aac_sample.utils.constants

import com.mohsinsyed.aac_sample.BuildConfig

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

    object SyncConstants {
        private const val BASE_TAG = BuildConfig.APPLICATION_ID
        const val TAG_CREATE_POST = "$BASE_TAG.create_post"
        const val TAG_UPDATE_POST = "$BASE_TAG.edit_post"
        const val TAG_DELETE_POST = "$BASE_TAG.delete_post"
    }

    const val EXTRA_KEY_POST = "post"
}