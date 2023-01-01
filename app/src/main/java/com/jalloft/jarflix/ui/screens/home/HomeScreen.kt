package com.jalloft.jarflix.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jalloft.jarflix.R
import com.jalloft.jarflix.model.genre.Genre
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.ui.components.CarouselTrendingPanel
import com.jalloft.jarflix.ui.components.HorizontalGenrePanel
import com.jalloft.jarflix.ui.components.HorizontalStateMoviePanel
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    onSeeMore: (MovieRowType) -> Unit,
    onGenre: (Genre) -> Unit,
    onMovieClicked: (MovieResult) -> Unit,
) {
    val genreResult by viewModel.remoteGenreCallState.observeAsState()
    val movieTrendingResult by viewModel.remoteTrendingCallState.observeAsState()
    val popularMovieResult by viewModel.remotePopularMovieCallState.observeAsState()
    val topRatedMovieCallState by viewModel.remoteTopRatedMovieCallState.observeAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        item {
            CarouselTrendingPanel(movieTrendingResult) {
                onMovieClicked(it)
            }
            HorizontalGenrePanel(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.genres),
                genreResult
            ) { onGenre(it) }
            HorizontalStateMoviePanel(
                rowType = MovieRowType.TRENDING,
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.trending),
                movieTrendingResult,
                onMovieClicked = { onMovieClicked(it) },
                onSeeMore = { onSeeMore(it) }
            )
            HorizontalStateMoviePanel(
                rowType = MovieRowType.POPULAR,
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.popular),
                popularMovieResult,
                onMovieClicked = { onMovieClicked(it) },
                onSeeMore = { onSeeMore(it) }
            )
            HorizontalStateMoviePanel(
                rowType = MovieRowType.TOP_RATED,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                title = stringResource(id = R.string.top_rated),
                topRatedMovieCallState,
                onMovieClicked = { onMovieClicked(it) },
                onSeeMore = { onSeeMore(it) }
            )
        }
    }
}