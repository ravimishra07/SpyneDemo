package com.ravi.spynedemo.model


import com.google.gson.annotations.SerializedName

data class GIFData(
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: Images,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("username")
    val username: String
)