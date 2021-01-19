package com.example.paginationexample.app

import android.app.Application
import com.example.paginationexample.koin.networkModule
import com.example.paginationexample.koin.repositoryModule
import com.example.paginationexample.koin.viewModelModule
import org.koin.core.context.startKoin

class PaginationExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}
