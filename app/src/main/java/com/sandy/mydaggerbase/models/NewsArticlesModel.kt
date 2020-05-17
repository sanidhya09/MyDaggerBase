package com.sandy.mydaggerbase.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsArticlesModel(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int, val title: String,
    val description: String,
    val urlToImage: String
)