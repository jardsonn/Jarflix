package com.jalloft.jarflix.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jalloft.jarflix.R
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.model.movie.detail.MovieCastCrew
import com.jalloft.jarflix.model.movie.detail.MovieDetails
import com.jalloft.jarflix.ui.components.HorizontalCast
import com.jalloft.jarflix.ui.components.HorizontalCrew
import com.jalloft.jarflix.ui.components.HorizontalMoviePanel
import com.jalloft.jarflix.ui.theme.SelectiveYellow
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import com.jalloft.jarflix.utils.toHour
import com.jalloft.jarflix.utils.toImageOrigial
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun MovieDetails(
    viewModel: HomeViewModel = hiltViewModel(),
    movieId: Int,
    onSeeMore: (MovieDetails) -> Unit,
    onBackPressed: () -> Unit
) {
    viewModel.initMovieDetails(movieId)
    val movieDetails by viewModel.remoteMovieDetails.observeAsState()
    val movieCastCrew by viewModel.remoteMovieCastCrew.observeAsState()
    val similarMovie by viewModel.remoteSimilarMovie.observeAsState()

    val itensColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = itensColumnState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            movieDetails?.let { movieDetails ->
                Header(movieDetails) { onBackPressed() }
                Overview(movieDetails, movieCastCrew, similarMovie, onSeeMore = {onSeeMore(movieDetails)}) { similarMovieId ->
                    coroutineScope.launch {
                        viewModel.initMovieDetails(similarMovieId)
                        itensColumnState.animateScrollToItem(0)
                    }
                }
            }
        }
    }
}

@Composable
fun Overview(
    movieDetails: MovieDetails,
    movieCastCrew: MovieCastCrew?,
    similarMovie: Movie?,
    onSeeMore: (MovieRowType) -> Unit,
    onMovieClicked: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = movieDetails.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.original_title).format(movieDetails.originalTitle),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = movieDetails.releaseDate.split("-")[0],
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                modifier = Modifier.padding(end = 8.dp)
            )
            Spacer(
                modifier = Modifier
                    .size(3.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        RoundedCornerShape(100f)
                    )
            )
            Text(
                text = movieDetails.runtime?.toHour!!,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                colorFilter = ColorFilter.tint(SelectiveYellow),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = DecimalFormat("#.#").apply {
                    minimumFractionDigits = 1
                }.format(movieDetails.voteAverage),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "/10",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            movieDetails.genres?.let { list ->
                items(list) { genre ->
                    Text(
                        text = genre?.name!!,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(100)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        movieDetails.overview?.let { overview ->
            Text(
                text = stringResource(id = R.string.overview),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = overview,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        movieCastCrew?.let {
            HorizontalCast(modifier = Modifier.padding(top = 16.dp), castList = it.cast) {
            }
            HorizontalCrew(modifier = Modifier.padding(top = 16.dp), castList = it.crew) {
            }
        }


        similarMovie?.results?.let { list ->
            HorizontalMoviePanel(
                rowType = MovieRowType.SIMILAR,
                modifier = Modifier.padding(top = 16.dp),
                stringResource(id = R.string.similar_movies),
                list,
                onMovieClicked = { similarMovie -> onMovieClicked(similarMovie.id ?: -1) },
                onSeeMore = { onSeeMore(it) }
            )
        }
    }
}

@Composable
fun Header(movieDetails: MovieDetails?, onBackPressed: () -> Unit) {
    Box {
        IconButton(
            onClick = { onBackPressed() },
            modifier = Modifier
                .zIndex(1f)
                .padding(8.dp)
                .background(
                    MaterialTheme.colorScheme.background.copy(0.5f),
                    RoundedCornerShape(100)
                ),
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
            )
        }
        AsyncImage(
            model = movieDetails?.backdropPath?.toImageOrigial,
            contentDescription = movieDetails?.originalTitle,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}
