package com.mohsinsyed.aac_sample.di

import android.app.Application
import android.content.Context
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.remote.PostService
import com.mohsinsyed.aac_sample.data.repository.outbox.IOutboxRepository
import com.mohsinsyed.aac_sample.data.repository.outbox.OutboxRepository
import com.mohsinsyed.aac_sample.data.repository.post.IPostRepository
import com.mohsinsyed.aac_sample.data.repository.post.PostRepository
import com.mohsinsyed.aac_sample.utils.utilities.StringUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideStringUtils(app: Application): StringUtils = StringUtils(app)

    @Singleton
    @Provides
    fun providePostRepository(
        stringUtils: StringUtils,
        postService: PostService,
        postDao: PostDao,
    ): IPostRepository {
        return PostRepository(stringUtils, postService, postDao)
    }

    @Singleton
    @Provides
    fun provideOutboxRepository(
        stringUtils: StringUtils,
        outboxDao: OutboxDao,
    ): IOutboxRepository {
        return OutboxRepository(stringUtils, outboxDao)
    }
}