package com.mohsinsyed.aac_sample.data.local

import androidx.room.*
import com.mohsinsyed.aac_sample.data.entities.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(post: List<Post>?)

    @Update
    suspend fun update(post: Post?)

    @Delete
    suspend fun delete(post: Post?)

    @Query("SELECT * FROM post")
    suspend fun fetchAll() : List<Post>?

    @Query("SELECT * FROM post where id = :id")
    suspend fun fetch(id: Int?) : Post
}