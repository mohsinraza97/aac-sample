package com.mohsinsyed.aac_sample.data.local

import androidx.room.*
import com.mohsinsyed.aac_sample.data.entities.Post

// https://developer.android.com/training/data-storage/room/accessing-data#convenience-insert
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post?) : Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg posts: Post?) : List<Long>?

    @Update
    suspend fun update(post: Post?) : Int?

    @Query("DELETE FROM post where id = :id")
    suspend fun delete(id: Int?) : Int?

    @Query("SELECT * FROM post")
    suspend fun fetchAll() : List<Post>?

    @Query("SELECT * FROM post where id = :id")
    suspend fun fetch(id: Int?) : Post?
}