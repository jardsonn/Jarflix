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
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.ui.components.CarouselTrendingPanel
import com.jalloft.jarflix.ui.components.HorizontalGenrePanel
import com.jalloft.jarflix.ui.components.HorizontalMoviePanel
import com.jalloft.jarflix.ui.components.HorizontalPanelAction
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    onClick: (HorizontalPanelAction) -> Unit
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
            CarouselTrendingPanel(movieTrendingResult)
            HorizontalGenrePanel(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.genres),
                genreResult
            ) {

            }
            HorizontalMoviePanel(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.trending),
                movieTrendingResult
            ) { action ->
                when (action) {
                    is HorizontalPanelAction.SeeAll -> {
                        Timber.i("Ver mais")
                    }
                    is HorizontalPanelAction.Item<*> -> {
                        onClick(HorizontalPanelAction.Item(action.data as MovieResult))
                    }
                }
            }
            HorizontalMoviePanel(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.popular),
                popularMovieResult
            ) { action ->
                when (action) {
                    is HorizontalPanelAction.SeeAll -> {
                        Timber.i("Ver mais")
                    }
                    is HorizontalPanelAction.Item<*> -> {
                        onClick(HorizontalPanelAction.Item(action.data as MovieResult))
                    }
                }
            }
            HorizontalMoviePanel(
                modifier = Modifier.padding(top = 16.dp),
                title = stringResource(id = R.string.top_rated),
                topRatedMovieCallState
            ) { action ->
                when (action) {
                    is HorizontalPanelAction.SeeAll -> {
                        Timber.i("Ver mais")
                    }
                    is HorizontalPanelAction.Item<*> -> {
                        onClick(HorizontalPanelAction.Item(action.data as MovieResult))
                    }
                }
            }
        }
    }
}