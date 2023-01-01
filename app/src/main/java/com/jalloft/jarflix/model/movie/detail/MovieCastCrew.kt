package com.jalloft.jarflix.model.movie.detail


import com.google.gson.annotations.SerializedName

data class MovieCastCrew(
    @SerializedName("cast")
    val cast: List<Cast?>?,
    @SerializedName("crew")
    val crew: List<Crew?>?,
    @SerializedName("id")
    val id: Int?
)