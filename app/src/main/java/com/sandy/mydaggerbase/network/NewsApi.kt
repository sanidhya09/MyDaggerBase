package com.sandy.mydaggerbase.network

import com.sandy.mydaggerbase.models.NewsMainModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    /**
     * Get top headlines from the API
     */
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Observable<NewsMainModel>
}