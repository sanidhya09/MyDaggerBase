package com.sandy.mydaggerbase

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sandy.mydaggerbase.database.NewsArticlesDao
import com.sandy.mydaggerbase.databinding.ActivityNewsBinding
import com.sandy.mydaggerbase.di.AppComponent
import com.sandy.mydaggerbase.di.DaggerAppComponent
import com.sandy.mydaggerbase.di.NetworkModule
import com.sandy.mydaggerbase.di.RoomModule
import com.sandy.mydaggerbase.models.NewsRequestModel
import com.sandy.mydaggerbase.network.NewsApi
import com.sandy.mydaggerbase.ui.NewsViewModel
import com.sandy.mydaggerbase.ui.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var newsArticlesDao: NewsArticlesDao

    @Inject
    lateinit var newsApi: NewsApi
    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel

    private var errorSnackbar: Snackbar? = null

    private val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .roomModule(RoomModule(application))
            .networkModule(NetworkModule)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        binding.newsList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.newsList.setHasFixedSize(true)

        component.inject(this)

        val newsRequestModel = NewsRequestModel("in", "sports", getString(R.string.news_api_key))

        viewModel =
            ViewModelProviders.of(
                this,
                ViewModelFactory(newsArticlesDao, newsApi, newsRequestModel)
            )
                .get(NewsViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}
