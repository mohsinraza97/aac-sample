package com.mohsinsyed.aac_sample.data.core.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohsinsyed.aac_sample.data.core.local.converters.TimestampConverter
import com.mohsinsyed.aac_sample.data.core.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.core.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.models.entities.Outbox
import com.mohsinsyed.aac_sample.data.models.entities.Post
import com.mohsinsyed.aac_sample.utils.constants.AppConstants

@Database(entities = [Post::class, Outbox::class], version = 1, exportSchema = false)
@TypeConverters(TimestampConverter::class)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun outboxDao(): OutboxDao

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