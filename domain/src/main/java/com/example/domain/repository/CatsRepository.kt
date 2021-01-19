package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.response.Cat
import com.example.domain.model.response.WrappedResource
import kotlinx.coroutines.flow.Flow

interface CatsRepository {

    fun getCats(): Flow<PagingData<Cat>>

    suspend fun getCat(catId: String): WrappedResource<Cat>
}
