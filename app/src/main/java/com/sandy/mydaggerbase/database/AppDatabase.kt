package com.sandy.mydaggerbase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandy.mydaggerbase.models.NewsArticlesModel

@Database(entities = [NewsArticlesModel::class], version = AppDatabase.NEWS_DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val NEWS_DATABASE_NAME: String = "news-db"
        const val NEWS_DATABASE_VERSION: Int = 1
    }

    abstract fun newsArticlesDao(): NewsArticlesDao
}