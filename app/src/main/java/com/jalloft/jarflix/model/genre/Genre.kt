package com.jalloft.jarflix.model.genre


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

fun Genre.toJson() = Gson().toJson(this)

fun String.toGenre() = Gson().fromJson(this, Genre::class.java)