package com.mohsinsyed.aac_sample.di

import android.app.Application
import com.mohsinsyed.aac_sample.data.core.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.core.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.core.remote.PostService
import com.mohsinsyed.aac_sample.data.core.repository.outbox.IOutboxRepository
import com.mohsinsyed.aac_sample.data.core.repository.outbox.OutboxRepository
import com.mohsinsyed.aac_sample.data.core.repository.post.IPostRepository
import com.mohsinsyed.aac_sample.data.core.repository.post.PostRepository
import com.mohsinsyed.aac_sample.data.core.source.outbox.IOutboxDataSource
import com.mohsinsyed.aac_sample.data.core.source.outbox.OutboxDataSource
import com.mohsinsyed.aac_sample.data.core.source.post.IPostLocalDataSource
import com.mohsinsyed.aac_sample.data.core.source.post.IPostRemoteDataSource
import com.mohsinsyed.aac_sample.data.core.source.post.PostLocalDataSource
import com.mohsinsyed.aac_sample.data.core.source.post.PostRemoteDataSource
import com.mohsinsyed.aac_sample.ui.resources.AppStrings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideStringUtils(app: Application): AppStrings = AppStrings(app)

    @Singleton
    @Provides
    fun providePostLocalDataSource(
        postDao: PostDao,
        appStrings: AppStrings?,
    ): IPostLocalDataSource {
        return PostLocalDataSource(postDao, appStrings)
    }

    @Singleton
    @Provides
    fun providePostRemoteDataSource(
        postService: PostService,
        appStrings: AppStrings?,
    ): IPostRemoteDataSource {
        return PostRemoteDataSource(postService, appStrings)
    }

    @Singleton
    @Provides
    fun providePostRepository(
        localDataSource: IPostLocalDataSource,
        remoteDataSource: IPostRemoteDataSource,
        appStrings: AppStrings?,
    ): IPostRepository {
        return PostRepository(localDataSource, remoteDataSource, appStrings)
    }

    @Singleton
    @Provides
    fun provideOutboxDataSource(outboxDao: OutboxDao, appStrings: AppStrings?): IOutboxDataSource {
        return OutboxDataSource(outboxDao, appStrings)
    }

    @Singleton
    @Provides
    fun provideOutboxRepository(dataSource: IOutboxDataSource): IOutboxRepository {
        return OutboxRepository(dataSource)
    }
}