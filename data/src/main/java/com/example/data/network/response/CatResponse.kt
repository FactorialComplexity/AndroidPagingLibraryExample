package com.example.data.network.response

import com.google.gson.annotations.SerializedName

data class CatResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?
)
