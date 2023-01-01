package com.jalloft.jarflix.model.movie

import androidx.annotation.StringRes
import com.jalloft.jarflix.R


enum class MovieRowType(@StringRes val title: Int = -1) {
    TRENDING(R.string.movie_trending),
    POPULAR(R.string.movie_popular),
    TOP_RATED(R.string.movie_top_rated),
    SIMILAR(R.string.movie_similar),
    GENRE
}