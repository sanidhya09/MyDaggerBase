package com.sandy.mydaggerbase.network

class ApiHelper(private val apiService: NewsApi) {

    suspend fun getTopHeadlinesSuspended(
        country: String,
        category: String,
        apiKey: String
    ) = apiService.getTopHeadlinesSuspended(country, category, apiKey)
}