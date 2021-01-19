package com.example.data.network.api

import com.example.data.network.response.CatResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApi {

    @GET("images/search")
    suspend fun getCats(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("size") size: String = "small"
    ): List<CatResponse>

    @GET("images/{catId}")
    suspend fun getCat(@Path("catId") catId: String): CatResponse
}
