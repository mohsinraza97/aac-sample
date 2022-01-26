package com.mohsinsyed.aac_sample.di

import android.content.Context
import com.mohsinsyed.aac_sample.data.local.PostDatabase
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.local.dao.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providePostDatabase(@ApplicationContext context: Context): PostDatabase {
        return PostDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun providePostDao(postDatabase: PostDatabase): PostDao {
        return postDatabase.postDao()
    }

    @Singleton
    @Provides
    fun provideOutboxDao(postDatabase: PostDatabase): OutboxDao {
        return postDatabase.outboxDao()
    }
}