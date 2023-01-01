package com.jalloft.jarflix.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jalloft.jarflix.model.movie.MovieRowType
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(private val service: MovieService) :
    BaseDataSource() {

    suspend fun getTrending(
        mediaType: String,
        timeWindow: String,
        language: String,
        page: Int
    ) = getResult { service.getTrending(mediaType, timeWindow, language, page) }


    suspend fun getGenreList(mediaType: String, language: String) =
        getResult { service.getGenreList(mediaType, language) }

    suspend fun getPopularMovieList(language: String, page: Int) =
        getResult { service.getPopularMovieList(language, page) }

    suspend fun getTopRatedMovieList(language: String, page: Int) =
        getResult { service.getTopRatedMovieList(language, page) }

    suspend fun searchMovie(
        language: String,
        query: String,
        page: Int,
        showAdultResult: Boolean,
    ) = getResult { service.searchMovie(language, query, page, showAdultResult) }

    suspend fun getMovieDetails(movieId: Int, language: String) =
        getResult { service.getMovieDetails(movieId, language) }

    suspend fun getMovieVideo(movieId: Int, language: String) =
        getResult { service.getMovieVideo(movieId, language) }

    suspend fun getMovieCast(movieId: Int, language: String) =
        getResult { service.getMovieCast(movieId, language) }

    suspend fun getSimilarMovie(movieId: Int, language: String, page: Int) =
        getResult { service.getSimilarMovie(movieId, language, page) }


    fun getMoviePagigation(type: MovieRowType, movieId: Int, genreId: Int) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 1000),
        pagingSourceFactory = {
            PagingMovieDataSource(
                type = type,
                service = service,
                movieId = movieId,
                genreId = genreId
            )
        }
    ).flow

}