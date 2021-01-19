package com.example.data.network

import com.example.data.network.api.CatsApi
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(getLoggingInterceptor())
    .addInterceptor {
        it.proceed(it.request().newBuilder().header("Content-Type", "application/json").build())
    }
    .readTimeout(10L, TimeUnit.SECONDS)
    .writeTimeout(10L, TimeUnit.SECONDS)
    .build()

fun getLoggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

fun getCatsApi(retrofit: Retrofit): CatsApi = retrofit.create(CatsApi::class.java)
