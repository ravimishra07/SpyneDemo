package com.ravi.spynedemo.model


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("downsized_large")
    val downsizedLarge: DownsizedLarge,
    @SerializedName("downsized_medium")
    val downsizedMedium: DownsizedMedium,
    @SerializedName("downsized_small")
    val downsizedSmall: DownsizedSmall,
    @SerializedName("original")
    val original: Original,
    @SerializedName("original_mp4")
    val originalMp4: OriginalMp4
)