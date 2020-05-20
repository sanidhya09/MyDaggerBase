package com.sandy.mydaggerbase.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sandy.mydaggerbase.R
import com.sandy.mydaggerbase.database.NewsArticlesDao
import com.sandy.mydaggerbase.models.NewsMainModel
import com.sandy.mydaggerbase.models.NewsRequestModel
import com.sandy.mydaggerbase.network.NewsApi
import com.sandy.mydaggerbase.utility.Resource
import kotlinx.coroutines.Dispatchers

class NewsViewModel(
    var newsArticlesDao: NewsArticlesDao,
    var newsApi: NewsApi,
    var newsRequestModel: NewsRequestModel
) : ViewModel() {

    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    val newsListAdapter: NewsListAdapter = NewsListAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorClickListener = View.OnClickListener { loadTopHeadlines() }

    fun getTopHeadlines() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = newsApi.getTopHeadlinesSuspended(
                        newsRequestModel.country,
                        newsRequestModel.category,
                        newsRequestModel.apiKey
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    private fun loadTopHeadlines() {
//        val topHeadlines: LiveData<NewsMainModel> = getTopHeadlines()
//        if (topHeadlines.value != null) {
//            val value = topHeadlines.value!!
//
//            onRetrieveSuccess(value)
//        }

//        subscription = Observable.fromCallable { newsArticlesDao.newsArticlesList }
//            .concatMap { dbNewsList ->
//                if (dbNewsList.isEmpty()) {
//                    //val topHeadlines = getTopHeadlines()
//                    //newsArticlesDao.insertAll(*topHeadlines.value!!.articles.toTypedArray())
//                    Observable.just(topHeadlines.value!!)
//                } else {
//                    val newsMainModelFromDb = NewsMainModel("ok", dbNewsList.size, dbNewsList)
//                    Observable.just(newsMainModelFromDb)
//                }
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onRetrieveStart() }
//            .doOnTerminate { onRetrieveFinish() }
//            .subscribe(
//                { result -> onRetrieveSuccess(result) },
//                { onRetrieveError() }
//            )
    }

     fun onRetrieveStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

     fun onRetrieveFinish() {
        loadingVisibility.value = View.GONE
    }

     fun onRetrieveSuccess(newsMainModel: NewsMainModel) {
        Log.i("newsMainModel", "newsMainModel::${newsMainModel.status}")
        newsListAdapter.updateNewsList(newsMainModel.articles)
    }

     fun onRetrieveError() {
        errorMessage.value = R.string.api_news_error
    }
}