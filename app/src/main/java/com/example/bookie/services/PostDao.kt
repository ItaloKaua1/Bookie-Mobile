package com.example.bookie.services

import androidx.room.*
import androidx.room.Dao
import androidx.room.RoomDatabase
import com.example.bookie.models.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getAll(): List<Post>

    @Insert
    fun insert(post: Post)
}

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}