package com.sandy.mydaggerbase.di

import com.sandy.mydaggerbase.MainActivity
import com.sandy.mydaggerbase.ui.NewsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RoomModule::class])
interface AppComponent {
    fun inject(newsViewModel: NewsViewModel)
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun networkModule(networkModule: NetworkModule): Builder
        fun roomModule(roomModule: RoomModule): Builder

    }
}