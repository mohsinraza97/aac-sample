package com.mohsinsyed.aac_sample.data.local

import androidx.room.*
import com.mohsinsyed.aac_sample.data.models.Post

// https://developer.android.com/training/data-storage/room/accessing-data#convenience-insert
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post?): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>?): List<Long>?

    @Update
    suspend fun update(post: Post?): Int?

    @Update
    suspend fun updateAll(posts: List<Post>?): Int?

    @Query("DELETE FROM post where id = :id")
    suspend fun delete(id: Int?): Int?

    @Query("DELETE FROM post")
    suspend fun deleteAll(): Int?

    @Query("SELECT * FROM post where id = :id")
    suspend fun findById(id: Int?): Post?

    @Query("SELECT * FROM post")
    suspend fun findAll(): List<Post>?
}