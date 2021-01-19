package com.example.paginationexample.koin

import com.example.data.network.converter.CatResponseConverter
import com.example.data.network.getCatsApi
import com.example.data.network.getOkHttpClient
import com.example.data.network.getRetrofit
import org.koin.dsl.module

val networkModule = module {
    single { getRetrofit(get()) }
    single { getOkHttpClient() }
    single { getCatsApi(get()) }
    single { CatResponseConverter() }
}
