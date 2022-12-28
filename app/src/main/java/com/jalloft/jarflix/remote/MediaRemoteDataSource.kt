package com.jalloft.jarflix.remote

import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(private val service: MovieService) :
    BaseDataSource() {

    suspend fun getTrending(
        mediaType: String,
        timeWindow: String,
        language: String
    ) = getResult { service.getTrending(mediaType, timeWindow, language) }


    suspend fun getGenreList(mediaType: String, language: String) =
        getResult { service.getGenreList(mediaType, language) }

    suspend fun getPopularMovieList(language: String, page: Int) =
        getResult { service.getPopularMovieList(language, page.toString()) }

    suspend fun getTopRatedMovieList(language: String, page: Int) =
        getResult { service.getTopRatedMovieList(language, page) }

    suspend fun searchMovie(
        language: String,
        query: String,
        page: Int,
        showAdultResult: Boolean,
    ) = getResult { service.searchMovie(language, query, page, showAdultResult) }

    suspend fun getMovieDetails(movieId: Int, language: String) = getResult { service.getMovieDetails(movieId, language) }

    suspend fun getMovieVideo(movieId: Int, language: String) = getResult { service.getMovieVideo(movieId, language) }
}