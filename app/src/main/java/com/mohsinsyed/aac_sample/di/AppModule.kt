package com.mohsinsyed.aac_sample.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mohsinsyed.aac_sample.data.local.dao.PostDao
import com.mohsinsyed.aac_sample.data.local.PostDatabase
import com.mohsinsyed.aac_sample.data.local.dao.OutboxDao
import com.mohsinsyed.aac_sample.data.remote.PostService
import com.mohsinsyed.aac_sample.data.repository.PostRepository
import com.mohsinsyed.aac_sample.utils.constants.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // region network
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.NetworkConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providePostService(retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }
    // endregion

    // region database
    @Singleton
    @Provides
    fun providePostDatabase(@ApplicationContext context: Context): PostDatabase {
        return PostDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providePostDao(postDatabase: PostDatabase): PostDao {
        return postDatabase.postDao()
    }

    @Provides
    @Singleton
    fun provideOutboxDao(postDatabase: PostDatabase): OutboxDao {
        return postDatabase.outboxDao()
    }
    // endregion

    // region repository
    @Singleton
    @Provides
    fun providePostRepository(
        @ApplicationContext context: Context,
        postService: PostService,
        postDao: PostDao,
        outboxDao: OutboxDao
    ): PostRepository {
        return PostRepository(context, postService, postDao, outboxDao)
    }
    // endregion
}