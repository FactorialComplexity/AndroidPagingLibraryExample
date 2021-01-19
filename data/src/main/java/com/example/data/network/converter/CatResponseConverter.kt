package com.example.data.network.converter

import com.example.data.network.response.CatResponse
import com.example.domain.model.response.Cat

class CatResponseConverter : BaseConverter<CatResponse, Cat>() {

    override fun convert(response: CatResponse?): Cat? = response?.run {
        Cat(id, url, width, height)
    }
}
