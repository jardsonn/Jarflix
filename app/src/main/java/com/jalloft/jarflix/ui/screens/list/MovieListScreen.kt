package com.jalloft.jarflix.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.jalloft.jarflix.R
import com.jalloft.jarflix.model.genre.toGenre
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.ui.components.StarVote
import com.jalloft.jarflix.ui.viewmodel.MovieListViewModel
import com.jalloft.jarflix.utils.toImageOrigial

fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onMovieClicked: (MovieResult) -> Unit
) {
    val similarMovieOriginName: String? = if (viewModel.similarMovieNameReference.isNullOrEmpty()) {
        viewModel.currentGenre?.toGenre()?.name
    } else {
        viewModel.similarMovieNameReference
    }

    val moviePaginationResult = remember {
        viewModel.getMoviePagigation()
    }

    MovieListScreenContent(
        lazyPagingItem = moviePaginationResult.collectAsLazyPagingItems(),
        itemRowType = viewModel.itemRowType,
        movieName = similarMovieOriginName,
        onMovieClicked = { onMovieClicked(it) },
        navigationIconContent = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreenContent(
    lazyPagingItem: LazyPagingItems<MovieResult>,
    navigationIconContent: @Composable () -> Unit = { },
    bottomBarContent: @Composable () -> Unit = { },
    itemRowType: MovieRowType = MovieRowType.SIMILAR,
    lazyListState: LazyGridState = rememberLazyGridState(),
    movieName: String? = null,
    onMovieClicked: (MovieResult) -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val title = if (itemRowType == MovieRowType.GENRE) {
        movieName ?: ""
    } else {
        if (movieName == null) {
            stringResource(id = itemRowType.title)
        } else {
            stringResource(id = itemRowType.title).format(movieName)
        }
    }

    Scaffold(
        topBar = {
            MovieListTopBar(
                title = title,
                navigationIconContent = navigationIconContent,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = bottomBarContent
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = innerPadding,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyListState,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp)
        ) {

//            lazyPagingItem.apply {
//                when {
//                    loadState.refresh is LoadState.Loading -> item {
//                        Timber.i("Carregando...")
//                    }
//                    loadState.append is LoadState.Loading -> {
//                        Timber.i("Pronto")
//                    }
//                }
//            }
            items(lazyPagingItem) { item ->
                MovieCard(item, onMovieClicked = { onMovieClicked(it) })
            }
        }
    }
}

@Composable
fun MovieCard(item: MovieResult?, onMovieClicked: (MovieResult) -> Unit) {
    item?.let { movie ->
        Column(
            modifier = Modifier
                .width(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(bottom = 16.dp)
                .clickable { onMovieClicked(movie) }
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = movie.posterPath?.toImageOrigial,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillBounds
                )
                StarVote(
                    movie = movie,
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                    )
                )
            }

            Text(
                text = movie.title!!,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListTopBar(
    title: String,
    navigationIconContent: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        navigationIcon = navigationIconContent,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}