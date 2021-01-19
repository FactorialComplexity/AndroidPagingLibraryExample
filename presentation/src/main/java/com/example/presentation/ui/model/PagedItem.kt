package com.example.presentation.ui.model

import com.example.domain.model.response.Cat

sealed class PagedItem {
    data class CatItem(val cat: Cat): PagedItem()
    data class GifItem(val cat: Cat): PagedItem()
    data class SeparatorItem(val text: String) : PagedItem()
}
