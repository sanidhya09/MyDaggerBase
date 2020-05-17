package com.sandy.mydaggerbase.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sandy.mydaggerbase.database.AppDatabase
import com.sandy.mydaggerbase.database.NewsArticlesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(application: Application) {
    private var libraryApplication = application

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("RoomDatabaseModule", "onCreate")
        }
    }


    @Singleton
    @Provides
    fun provideRoomDatabase(): AppDatabase {
        return Room.databaseBuilder(
            libraryApplication,
            AppDatabase::class.java,
            AppDatabase.NEWS_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase): NewsArticlesDao = database.newsArticlesDao()

}