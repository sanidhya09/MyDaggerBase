package com.sandy.mydaggerbase.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sandy.mydaggerbase.models.NewsArticlesModel

@Dao
interface NewsArticlesDao {

    @get:Query("SELECT * FROM NewsArticlesModel")
    val newsArticlesList: List<NewsArticlesModel>

    @Insert
    fun insertAll(vararg users: NewsArticlesModel)
}