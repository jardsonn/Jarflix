package com.jalloft.jarflix.remote


class MovieRepository constructor(private val remoteDataSource: MovieRemoteDataSource) {

    suspend fun getMovieTrending(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "pt-BR"
    ) = remoteDataSource.getTrending(mediaType, timeWindow, language)

    suspend fun getGenreList(mediaType: String = "movie", language: String = "pt-BR") =
        remoteDataSource.getGenreList(mediaType, language)

    suspend fun getPopularMovieList(language: String = "pt-BR", page: Int = 1) =
        remoteDataSource.getPopularMovieList(language, page)

    suspend fun getTopRatedMovieList(language: String = "pt-BR", page: Int = 1) =
        remoteDataSource.getTopRatedMovieList(language, page)

    suspend fun searchMovie(
        query: String,
        language: String = "pt-BR",
        page: Int = 1,
        showAdultResult: Boolean = false,
    ) = remoteDataSource.searchMovie(language, query, page, showAdultResult)

    suspend fun getMovieDetails(movieId: Int, language: String = "pt-BR") = remoteDataSource.getMovieDetails(movieId, language)

    suspend fun getMovieVideo(movieId: Int, language: String = "pt-BR") = remoteDataSource.getMovieVideo(movieId, language)

}