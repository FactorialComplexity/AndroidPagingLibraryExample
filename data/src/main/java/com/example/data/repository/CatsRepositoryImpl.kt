package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.network.api.CatsApi
import com.example.data.network.converter.CatResponseConverter
import com.example.data.network.paging_source.CatsPagingSource
import com.example.domain.model.response.Cat
import com.example.domain.model.response.WrappedResource
import com.example.domain.repository.CatsRepository
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class CatsRepositoryImpl(private val api: CatsApi, private val converter: CatResponseConverter) :
    CatsRepository {

    companion object {
        // Page size or limit - how many cats we receive in next response
        private const val PAGE_ZIE = 10
    }

    // For retrieving list of cats
    override fun getCats(): Flow<PagingData<Cat>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_ZIE, enablePlaceholders = false),
            initialKey = 0,
            pagingSourceFactory = { CatsPagingSource(api, converter) }
        ).flow
    }

    // For opening Cat dialog
    override suspend fun getCat(catId: String): WrappedResource<Cat> {
        return getWrappedResource { converter.convert(api.getCat(catId)) }
    }

    private suspend fun <T> getWrappedResource(retrievingResourceCallback: suspend () -> T?): WrappedResource<T> {
        return try {
            WrappedResource.Result(retrievingResourceCallback())
        } catch (exception: Exception) {
            WrappedResource.Error(exception)
        }
    }
}
