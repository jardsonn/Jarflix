package com.jalloft.jarflix.remote

import com.jalloft.jarflix.BuildConfig
import com.jalloft.jarflix.model.genre.GenreObject
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.model.movie.detail.MovieDetails
import com.jalloft.jarflix.model.videos.MovieVideo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {
    @GET("trending/{mediaType}/{timeWindow}?api_key=${BuildConfig.API_KEY}")
    suspend fun getTrending(
        @Path("mediaType") mediaType: String,
        @Path("timeWindow") timeWindow: String,
        @Query("language") language: String
    ): Response<Movie>

    @GET("genre/{mediaType}/list?api_key=${BuildConfig.API_KEY}")
    suspend fun getGenreList(
        @Path("mediaType") mediaType: String,
        @Query("language") language: String
    ): Response<GenreObject>

    @GET("movie/popular?api_key=${BuildConfig.API_KEY}")
    suspend fun getPopularMovieList(
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<Movie>

    @GET("movie/top_rated?api_key=${BuildConfig.API_KEY}")
    suspend fun getTopRatedMovieList(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("search/movie?api_key=${BuildConfig.API_KEY}")
    suspend fun searchMovie(
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") showAdultResult: Boolean,
    ): Response<Movie>

    @GET("movie/{movieId}?api_key=${BuildConfig.API_KEY}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
    ): Response<MovieDetails>

    @GET("movie/{movieId}/videos?api_key=${BuildConfig.API_KEY}")
    suspend fun getMovieVideo(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
    ): Response<MovieVideo>
}