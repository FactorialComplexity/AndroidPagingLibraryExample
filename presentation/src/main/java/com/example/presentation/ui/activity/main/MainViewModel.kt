package com.example.presentation.ui.activity.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.domain.model.response.Cat
import com.example.domain.repository.CatsRepository
import com.example.presentation.ui.base.BaseViewModel
import com.example.presentation.ui.model.PagedItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MainViewModel(private val catsRepository: CatsRepository) : BaseViewModel() {

    fun getCats(): Flow<PagingData<PagedItem>> = catsRepository.getCats()
        .map { pagingData ->
            pagingData.map {
                if (it.url?.split(".")?.last() == "gif") {
                    PagedItem.GifItem(it)
                } else {
                    PagedItem.CatItem(it)
                }
            }
        }
        .map {
            it.insertSeparators { before, after ->
                if (after == null) {
                    return@insertSeparators PagedItem.SeparatorItem("End of the list")
                }

                if (before == null) {
                    return@insertSeparators PagedItem.SeparatorItem("Begin of the list")
                }

                if (after is PagedItem.GifItem) {
                    return@insertSeparators PagedItem.SeparatorItem("Gif with cat below")
                }

                if (before is PagedItem.GifItem) {
                    return@insertSeparators PagedItem.SeparatorItem("Gif with cat above")
                }

                null
            }
        }
        .cachedIn(viewModelScope)

    fun getCatFlow(catId: String, thumbUrl: String?): Flow<Pair<Cat?, String?>> = flow {
        catsRepository.getCat(catId)
            .processResult()
            ?.let { emit(it to thumbUrl) }
    }
}
