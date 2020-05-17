package com.sandy.mydaggerbase.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sandy.mydaggerbase.database.NewsArticlesDao
import com.sandy.mydaggerbase.models.NewsRequestModel
import com.sandy.mydaggerbase.network.NewsApi

class ViewModelFactory(
    var newsArticlesDao: NewsArticlesDao,
    var newsApi: NewsApi,
    var newsRequestModel: NewsRequestModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsArticlesDao, newsApi, newsRequestModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}