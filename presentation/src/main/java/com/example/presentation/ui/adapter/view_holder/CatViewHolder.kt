package com.example.presentation.ui.adapter.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.response.Cat
import com.example.presentation.R
import com.example.presentation.ui.adapter.CatsPagedAdapter
import com.example.presentation.ui.extensions.clickWithDebounce
import com.example.presentation.ui.extensions.loadUrl

sealed class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class CatVH(itemView: View, private val listener: CatsPagedAdapter.OnCatClickListener) :
        CatViewHolder(itemView) {

        private val tvCatId = itemView.findViewById<TextView>(R.id.tvCatId)
        private val ivCatImage = itemView.findViewById<ImageView>(R.id.ivCatImage)
        private val pbCatLoading = itemView.findViewById<ProgressBar>(R.id.pbCatLoading)

        fun bind(cat: Cat?) {
            cat?.apply {
                setCatId(id)
                setCatPicture(url)

                itemView.clickWithDebounce {
                    id?.let {
                        listener.onCatClicked(it, url)
                    }
                }
            }
        }

        private fun setCatId(id: String?) {
            tvCatId.text = itemView.context.getString(R.string.cat_id_placeholder, id)
        }

        private fun setCatPicture(url: String?) {
            ivCatImage.loadUrl(url = url, progressView = pbCatLoading)
        }
    }

    class SeparatorVH(itemView: View, private val listener: CatsPagedAdapter.OnCatClickListener) :
        CatViewHolder(itemView) {

        private val tvSeparatorText = itemView.findViewById<TextView>(R.id.tvSeparatorText)

        fun bind(text: String?) {
            tvSeparatorText.text = text

            itemView.clickWithDebounce {
                listener.onSeparatorClicked()
            }
        }
    }
}
