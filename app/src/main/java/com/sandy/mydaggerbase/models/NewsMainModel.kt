package com.sandy.mydaggerbase.models

data class NewsMainModel(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticlesModel>
)