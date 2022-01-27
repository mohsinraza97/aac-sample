package com.mohsinsyed.aac_sample.data.core.local.dao

import androidx.room.*
import com.mohsinsyed.aac_sample.data.models.entities.Post

// https://developer.android.com/training/data-storage/room/accessing-data#convenience-insert
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post?): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>?): List<Long>?

    @Update
    suspend fun update(post: Post?): Int?

    @Query("DELETE FROM post where id = :id")
    suspend fun delete(id: Long?): Int?

    @Query("DELETE FROM post")
    suspend fun deleteAll(): Int?

    @Query("SELECT * FROM post where id = :id")
    suspend fun findById(id: Long?): Post?

    @Query("SELECT * FROM post")
    suspend fun findAll(): List<Post>?
}