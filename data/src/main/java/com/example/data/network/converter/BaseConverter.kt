package com.example.data.network.converter

abstract class BaseConverter<R, M> {

    abstract fun convert(response: R?): M?
}
