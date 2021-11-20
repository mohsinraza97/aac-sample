package com.mohsinsyed.aac_sample.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohsinsyed.aac_sample.data.models.Post
import com.mohsinsyed.aac_sample.utils.constants.AppConstants

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        fun getDatabase(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                buildDatabase(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(appContext: Context): PostDatabase {
            return Room.databaseBuilder(
                appContext,
                PostDatabase::class.java,
                AppConstants.DBConstants.DB_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}