package com.example.presentation.ui.activity.main

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.presentation.R
import com.example.presentation.ui.adapter.CatsPagedAdapter
import com.example.presentation.ui.adapter.FooterLoadStateAdapter
import com.example.presentation.ui.base.BaseActivity
import com.example.presentation.ui.extensions.clickWithDebounce
import com.example.presentation.ui.extensions.loadUrl
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bRetry
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.koin.android.viewmodel.ext.android.viewModel

// @FlowPreview annotation means you are using Flow preview features that can be changed in future
@FlowPreview
class MainActivity : BaseActivity(R.layout.activity_main), CatsPagedAdapter.OnCatClickListener {

    private val viewModel by viewModel<MainViewModel>()

    private val adapter by lazy { CatsPagedAdapter(this) }

    override fun initUI() {
        initList()
        initClicks()
        initSwipeToRefresh()
        listenErrors()
    }

    override fun onCatClicked(catId: String, thumbUrl: String?) {
        // We opening retrieving full-size cat item and showing it
        // in the BottomSheetDialog
        lifecycleScope.launchWhenCreated {
            viewModel.getCatFlow(catId, thumbUrl)
                .collectLatest { (cat, thumbUrl) ->
                    cat?.apply {
                        BottomSheetDialog(this@MainActivity).apply {
                            setContentView(
                                layoutInflater.inflate(
                                    R.layout.bottom_sheet,
                                    srlRefresh,
                                    false
                                )
                            )

                            show()

                            tvCatId.text = getString(R.string.cat_id_placeholder, id)

                            ivCatImage.loadUrl(
                                url = url,
                                progressView = pbCatLoading,
                                thumbUrl = thumbUrl
                            )
                        }
                    }
                }
        }
    }

    override fun onSeparatorClicked() {
        showToast("Separator clicked")
    }

    // Init paging data adapter
    private fun initList() {
        // First, we add load state footer to show retry placeholder.
        // See FooterLoadStateAdapter for more info
        rvCats.adapter = adapter.withLoadStateFooter(FooterLoadStateAdapter { adapter.retry() })

        // Next, we add listener to handle load states
        adapter.addLoadStateListener {
            val isEmpty = adapter.itemCount == 0

            val errorState = it.source.append as? LoadState.Error ?: it.append as? LoadState.Error
            ?: it.refresh as? LoadState.Error

            if (errorState != null) {
                lifecycleScope.launchWhenCreated {
                    viewModel.processError(errorState.error)
                }
            }

            val loadState =
                it.source.append as? LoadState.Loading ?: it.append as? LoadState.Loading
                ?: it.refresh as? LoadState.Loading

            lpiProgress.isVisible = loadState != null

            val isError = errorState != null && isEmpty

            rlRetry.isVisible = isError
            srlRefresh.isVisible = !isError
        }

        lifecycleScope.launchWhenCreated {
            viewModel.getCats().collectLatest {
                adapter.submitData(it)
            }

            adapter.loadStateFlow.collectLatest { loadStates ->
                // Hide swipe-to-refresh when load state is not loading
                srlRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }

            adapter.loadStateFlow
                // Only emit when refreshing
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where refreshing completes i.e., LoadState.NotLoading
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top to show refreshed data from start
                .collect { rvCats.scrollToPosition(0) }
        }
    }

    private fun initClicks() {
        bRetry.clickWithDebounce {
            adapter.retry()
        }
    }

    private fun initSwipeToRefresh() {
        srlRefresh.setOnRefreshListener {
            // Refreshing list
            adapter.refresh()

            // Hide swipe-to-refresh after 5 seconds
            lifecycleScope.launchWhenCreated {
                delay(5_000L)

                srlRefresh.isRefreshing = false
            }
        }
    }

    // In this function we observing errorStateFlow for errors
    private fun listenErrors() {
        lifecycleScope.launchWhenCreated {
            viewModel.errorStateFlow
                .debounce(400L)
                .filterNotNull()
                .collect { showToast(it) }
        }
    }
}
