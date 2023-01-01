package com.jalloft.jarflix.remote

import com.jalloft.jarflix.model.genre.GenreObject
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.model.movie.detail.MovieCastCrew
import com.jalloft.jarflix.model.movie.detail.MovieDetails
import com.jalloft.jarflix.model.videos.MovieVideo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {
    @GET("trending/{mediaType}/{timeWindow}")
    suspend fun getTrending(
        @Path("mediaType") mediaType: String,
        @Path("timeWindow") timeWindow: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("genre/{mediaType}/list")
    suspend fun getGenreList(
        @Path("mediaType") mediaType: String,
        @Query("language") language: String
    ): Response<GenreObject>

    @GET("movie/popular")
    suspend fun getPopularMovieList(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovieList(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") showAdultResult: Boolean,
    ): Response<Movie>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
    ): Response<MovieDetails>

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideo(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
    ): Response<MovieVideo>

    @GET("movie/{movieId}/credits")
    suspend fun getMovieCast(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
    ): Response<MovieCastCrew>

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovie(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("discover/movie")
    suspend fun getMoviesByGenres(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): Response<Movie>
}