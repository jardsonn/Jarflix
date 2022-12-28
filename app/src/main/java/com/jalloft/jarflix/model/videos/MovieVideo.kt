package com.jalloft.jarflix.model.videos


import com.google.gson.annotations.SerializedName

data class MovieVideo(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("results")
    val results: List<VideoResult?>?
)