package com.jalloft.jarflix.model.genre


import com.google.gson.annotations.SerializedName

data class GenreObject(
    @SerializedName("genres")
    val genres: List<Genre>
)