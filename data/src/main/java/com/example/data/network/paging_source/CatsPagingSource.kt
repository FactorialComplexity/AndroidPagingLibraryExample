package com.example.data.network.paging_source

import androidx.paging.PagingSource
import com.example.data.network.api.CatsApi
import com.example.data.network.converter.CatResponseConverter
import com.example.domain.model.response.Cat
import retrofit2.HttpException
import java.io.IOException
import kotlin.Exception

class CatsPagingSource(private val api: CatsApi, private val converter: CatResponseConverter) :
    PagingSource<Int, Cat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        return try {
            params.key?.let { key ->
                val response = api.getCats(limit = params.loadSize, page = key)
                    .mapNotNull { converter.convert(it) }

                val prevKey = if (key == 0) null else key - 1

                // Check is end of pagination reached
                val nextKey = if (response.isEmpty()) null else key + 1

                LoadResult.Page(data = response, prevKey = prevKey, nextKey = nextKey)
            } ?: LoadResult.Error(Exception("End of pagination reached"))
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
