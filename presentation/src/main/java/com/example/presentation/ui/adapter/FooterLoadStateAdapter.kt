package com.example.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.ui.extensions.clickWithDebounce

class FooterLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<FooterLoadStateAdapter.FooterViewHolder>() {

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        FooterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer, parent, false)
        )

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bRetry = itemView.findViewById<Button>(R.id.bRetry)
        private val llRetry = itemView.findViewById<LinearLayout>(R.id.llRetry)
        private val llProgress = itemView.findViewById<LinearLayout>(R.id.llProgress)

        fun bind(loadState: LoadState) {
            val isLoading = loadState is LoadState.Loading && !loadState.endOfPaginationReached

            llProgress.isVisible = isLoading
            llRetry.isVisible = !isLoading

            bRetry.clickWithDebounce { retry() }
        }
    }
}
