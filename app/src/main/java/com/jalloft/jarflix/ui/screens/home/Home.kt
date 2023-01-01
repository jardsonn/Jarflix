package com.jalloft.jarflix.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.jalloft.jarflix.model.genre.Genre
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.ui.components.*
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeViewModel = hiltViewModel(),
         onSeeMore: (MovieRowType) -> Unit,
         onGenre: (Genre) -> Unit,
         onMovieClicked: (MovieResult) -> Unit,) {
    val searchQuery by viewModel.searchQuery.observeAsState()
    val topBarState by viewModel.topBarState.observeAsState()

    Scaffold(
        topBar = {
            HomeTopAppBar(
                topBarState,
                updateTopBaState = { viewModel.updateTopBarState(it) }) {
                viewModel.onSearchQueryChange(
                    it
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (searchQuery.isNullOrEmpty()) {
            HomeScreen(innerPadding, viewModel, onSeeMore = { onSeeMore(it) }, onGenre = onGenre) { onMovieClicked(it) }
        } else {
            SearchScreen(innerPadding, viewModel) { onMovieClicked(it) }
        }
    }
}



