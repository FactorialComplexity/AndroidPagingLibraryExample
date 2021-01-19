package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.presentation.R
import com.example.presentation.ui.adapter.view_holder.CatViewHolder
import com.example.presentation.ui.model.PagedItem

class CatsPagedAdapter(private val listener: OnCatClickListener) :
    PagingDataAdapter<PagedItem, CatViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PagedItem>() {
            override fun areItemsTheSame(oldItem: PagedItem, newItem: PagedItem): Boolean =
                (oldItem is PagedItem.CatItem && newItem is PagedItem.CatItem && oldItem.cat.id == newItem.cat.id) ||
                        (oldItem is PagedItem.GifItem && newItem is PagedItem.GifItem && oldItem.cat.id == newItem.cat.id) ||
                        (oldItem is PagedItem.SeparatorItem && newItem is PagedItem.SeparatorItem && oldItem.text == newItem.text)

            override fun areContentsTheSame(oldItem: PagedItem, newItem: PagedItem): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagedItem.CatItem -> R.layout.item_cat
            is PagedItem.GifItem -> R.layout.item_gif
            is PagedItem.SeparatorItem -> R.layout.item_separator
            else -> throw UnsupportedOperationException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagedItem.CatItem -> (holder as? CatViewHolder.CatVH)?.bind(item.cat)
            is PagedItem.GifItem -> (holder as? CatViewHolder.CatVH)?.bind(item.cat)
            is PagedItem.SeparatorItem -> (holder as? CatViewHolder.SeparatorVH)?.bind(item.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return when (viewType) {
            R.layout.item_cat -> CatViewHolder.CatVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false),
                listener
            )
            R.layout.item_gif -> CatViewHolder.CatVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false),
                listener
            )
            R.layout.item_separator -> CatViewHolder.SeparatorVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_separator, parent, false),
                listener
            )
            else ->  throw UnsupportedOperationException("Unknown view type")
        }
    }

    interface OnCatClickListener {
        fun onCatClicked(catId: String, thumbUrl: String?)

        fun onSeparatorClicked()
    }
}
