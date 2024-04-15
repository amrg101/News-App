package com.amrg.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amrg.newsapp.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(article: ArticleEntity): Long

    @Query("Select * From articles")
    fun getArticles(): List<ArticleEntity>

    @Query("DELETE FROM articles WHERE title = :title AND author = :author")
    fun delete(title: String, author: String)
}