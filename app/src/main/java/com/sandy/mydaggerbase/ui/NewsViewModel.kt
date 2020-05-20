package com.sandy.mydaggerbase.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sandy.mydaggerbase.R
import com.sandy.mydaggerbase.database.NewsArticlesDao
import com.sandy.mydaggerbase.models.NewsMainModel
import com.sandy.mydaggerbase.models.NewsRequestModel
import com.sandy.mydaggerbase.network.NewsApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsViewModel(
    var newsArticlesDao: NewsArticlesDao,
    var newsApi: NewsApi,
    var newsRequestModel: NewsRequestModel
) : ViewModel() {

    private lateinit var subscription: Disposable
    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    val newsListAdapter: NewsListAdapter = NewsListAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    val errorClickListener = View.OnClickListener { loadTopHeadlines() }

    init {
        loadTopHeadlines()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    private fun loadTopHeadlines() {

        subscription = Observable.fromCallable { newsArticlesDao.newsArticlesList }
            .concatMap { dbNewsList ->
                if (dbNewsList.isEmpty())
                    newsApi.getTopHeadlines(
                        newsRequestModel.country,
                        newsRequestModel.category,
                        newsRequestModel.apiKey
                    )
                        .concatMap { newsMainModelFromApi ->
                            newsArticlesDao.insertAll(*newsMainModelFromApi.articles.toTypedArray())
                            Observable.just(newsMainModelFromApi)
                        }
                else {
                    val newsMainModelFromDb = NewsMainModel("ok", dbNewsList.size, dbNewsList)
                    Observable.just(newsMainModelFromDb)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveStart() }
            .doOnTerminate { onRetrieveFinish() }
            .subscribe(
                { result -> onRetrieveSuccess(result) },
                { onRetrieveError() }
            )
    }

    private fun onRetrieveStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveSuccess(newsMainModel: NewsMainModel) {
        Log.i("newsMainModel", "newsMainModel::${newsMainModel.status}")
        newsListAdapter.updateNewsList(newsMainModel.articles)
    }

    private fun onRetrieveError() {
        errorMessage.value = R.string.api_news_error
    }
}