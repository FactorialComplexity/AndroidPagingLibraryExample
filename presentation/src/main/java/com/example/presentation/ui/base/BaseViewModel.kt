package com.example.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.example.domain.model.response.WrappedResource
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseViewModel : ViewModel() {

    val errorStateFlow = MutableStateFlow<String?>(null)

    protected suspend fun <T> WrappedResource<T>.processResult(): T? {
        return when (this) {
            is WrappedResource.Error -> {
                processError(exception)

                null
            }
            is WrappedResource.Result -> data
        }
    }

    suspend fun processError(exception: Throwable?) {
        errorStateFlow.apply {
            emit(null)
            emit(exception?.message)
        }
    }
}
