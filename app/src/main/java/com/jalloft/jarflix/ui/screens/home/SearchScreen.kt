package com.jalloft.jarflix.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.ui.components.HorizontalPanelAction
import com.jalloft.jarflix.ui.theme.SelectiveYellow
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import com.jalloft.jarflix.utils.IMAGE_URL_ORIGINAL
import java.text.DecimalFormat


@Composable
fun SearchScreen(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel,
    onClick: (HorizontalPanelAction) -> Unit
) {
    val searchResultState by viewModel.remoteSearchMovieCallState.observeAsState()
    searchResultState?.let { state ->
        if (state is HomeViewModel.RemoteCallState.Success<*>) {
            val movies = (state.media as Movie).results
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                movies.forEach { movie ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onClick(HorizontalPanelAction.Item(movie)) },
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AsyncImage(
                                model = "$IMAGE_URL_ORIGINAL${movie.posterPath}",
                                contentDescription = movie.title,
                                modifier = Modifier
                                    .width(120.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.FillBounds
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = movie.title!!,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    movie.overview?.let {
                                        Text(
                                            text = movie.overview,
                                            style = MaterialTheme.typography.bodyMedium,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 4,
                                            modifier = Modifier
                                                .padding(vertical = 4.dp),
                                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier.weight(1f)
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
                                        }.format(movie.voteAverage),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }


                        }
                    }
                }

            }
        }
    }
}