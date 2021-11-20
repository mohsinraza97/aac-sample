package com.mohsinsyed.aac_sample.utils.constants

import com.mohsinsyed.aac_sample.BuildConfig

object AppConstants {

    object NetworkConstants {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val USER_ID = 1L // Hard-coded user id

        const val CREATE_POST = "posts" // POST
        const val MODIFY_POST = "posts/{postId}" // PUT, DELETE
        const val FETCH_POSTS_BY_USER_ID = "posts?userId=$USER_ID" // GET
    }

    object DBConstants {
        const val DB_NAME = "AAC_Database"

        const val OUTBOX_STATUS_PENDING = "PENDING"
        const val OUTBOX_STATUS_IN_PROGRESS = "IN_PROGRESS"
        const val OUTBOX_STATUS_FAILED = "FAILED"
        const val OUTBOX_STATUS_COMPLETED = "COMPLETED"
        const val OUTBOX_STATUS_DELETED = "DELETED"
    }

    object SyncConstants {
        private const val SYNC_BASE_TAG = BuildConfig.APPLICATION_ID

        const val SYNC_TAG_CREATE_POST = "$SYNC_BASE_TAG.create_post"
        const val SYNC_TAG_UPDATE_POST = "$SYNC_BASE_TAG.edit_post"
        const val SYNC_TAG_DELETE_POST = "$SYNC_BASE_TAG.delete_post"
    }

    object IntentConstants {
        const val EXTRA_KEY_POST = "post"
    }
}