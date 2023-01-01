package com.jalloft.jarflix.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.model.movie.MovieResult
import retrofit2.HttpException
import java.io.IOException


class PagingMovieDataSource constructor(
    private val type: MovieRowType,
    private val service: MovieService,
    private val mediaType: String = "movie",
    private val timeWindow: String = "day",
    private val language: String = "pt-BR",
    private val movieId: Int,
    private val genreId: Int,
) : PagingSource<Int, MovieResult>() {
    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {
            val page = params.key ?: STARTING_PAGE
            val result = when (type) {
                MovieRowType.TRENDING -> service.getTrending(mediaType, timeWindow, language, page)
                MovieRowType.POPULAR -> service.getPopularMovieList(language, page)
                MovieRowType.TOP_RATED -> service.getTopRatedMovieList(language, page)
                MovieRowType.SIMILAR -> service.getSimilarMovie(movieId, language, page)
                else -> service.getMoviesByGenres(genreId, page)
            }

            LoadResult.Page(
                data = if (result.isSuccessful) {
                    result.body()?.results ?: listOf()
                } else listOf(),
                prevKey = params.key,
                nextKey = params.key?.plus(1) ?: STARTING_PAGE.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }

}