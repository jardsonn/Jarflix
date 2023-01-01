package com.jalloft.jarflix.remote

import com.jalloft.jarflix.model.movie.MovieRowType


class MovieRepository constructor(private val remoteDataSource: MovieRemoteDataSource) {

    suspend fun getMovieTrending(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "pt-BR",
        page: Int,
    ) = remoteDataSource.getTrending(mediaType, timeWindow, language, page)

    suspend fun getGenreList(mediaType: String = "movie", language: String = "pt-BR") =
        remoteDataSource.getGenreList(mediaType, language)

    suspend fun getPopularMovieList(language: String = "pt-BR", page: Int) =
        remoteDataSource.getPopularMovieList(language, page)

    suspend fun getTopRatedMovieList(language: String = "pt-BR", page: Int) =
        remoteDataSource.getTopRatedMovieList(language, page)

    suspend fun searchMovie(
        query: String,
        language: String = "pt-BR",
        page: Int,
        showAdultResult: Boolean = false,
    ) = remoteDataSource.searchMovie(language, query, page, showAdultResult)

    suspend fun getMovieDetails(movieId: Int, language: String = "pt-BR") = remoteDataSource.getMovieDetails(movieId, language)

    suspend fun getMovieVideo(movieId: Int, language: String = "pt-BR") = remoteDataSource.getMovieVideo(movieId, language)

    suspend fun getMovieCast(movieId: Int, language: String = "pt-BR") = remoteDataSource.getMovieCast(movieId, language)

    suspend fun getSimilarMovie(movieId: Int, language: String = "pt-BR", page: Int) = remoteDataSource.getSimilarMovie(movieId, language, page)

    fun getMoviePagigation(type: MovieRowType, movieId: Int, genreId: Int) = remoteDataSource.getMoviePagigation(type, movieId, genreId)
}