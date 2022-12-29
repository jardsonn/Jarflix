package com.jalloft.jarflix.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.jalloft.jarflix.model.movie.detail.MovieDetails
import com.jalloft.jarflix.ui.theme.SelectiveYellow
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import com.jalloft.jarflix.utils.toHour
import com.jalloft.jarflix.utils.toImageOrigial
import java.text.DecimalFormat


@Composable
fun MovieDetails(
    viewModel: HomeViewModel = hiltViewModel(),
    movieId: Int,
    onBackPressed: () -> Unit
) {
    viewModel.initMovieDetails(movieId)
    val movieDetails by viewModel.remoteMovieDetails.observeAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            movieDetails?.let {
                Header(it) { onBackPressed() }
                Overview(it)
            }
        }
    }
}

@Composable
fun Overview(movieDetails: MovieDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = movieDetails.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.original_title).format(movieDetails.originalTitle),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
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
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            modifier = Modifier.padding(top = 8.dp)
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
        movieDetails.overview?.let { overview->
            Text(
                text = stringResource(id = R.string.overview),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = overview,
                style = MaterialTheme.typography.bodyLarge
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
