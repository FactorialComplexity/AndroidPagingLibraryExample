package com.example.domain.model.response

import java.lang.Exception

sealed class WrappedResource<out T> {
    class Result<out T>(val data: T? = null) : WrappedResource<T>()

    class Error<T>(val exception: Exception? = null): WrappedResource<T>()
}
