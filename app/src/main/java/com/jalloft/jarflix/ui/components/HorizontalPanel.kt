package com.jalloft.jarflix.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jalloft.jarflix.R
import com.jalloft.jarflix.model.genre.Genre
import com.jalloft.jarflix.model.genre.GenreObject
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.ui.theme.SelectiveYellow
import com.jalloft.jarflix.ui.theme.White
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import com.jalloft.jarflix.utils.IMAGE_URL_ORIGINAL
import java.text.DecimalFormat

@Composable
fun HorizontalGenrePanel(
    modifier: Modifier = Modifier,
    title: String,
    remoteState: HomeViewModel.RemoteCallState?,
    onGenreSelected: (Genre) -> Unit
) {
    remoteState?.let { state ->
        if (state is HomeViewModel.RemoteCallState.Success<*>) {
            val genres = (state.media as GenreObject).genres
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(genres) { genre ->
                        Button(
                            onClick = { onGenreSelected(genre) },
                        ) {
                            Text(
                                text = genre.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun HorizontalMoviePanel(
    modifier: Modifier = Modifier,
    title: String,
    remoteState: HomeViewModel.RemoteCallState?,
    onClick: (HorizontalPanelAction) -> Unit
) {
    remoteState?.let { state ->
        if (state is HomeViewModel.RemoteCallState.Success<*>) {
            val movies = (state.media as Movie).results
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    TextButton(
                        onClick = { onClick(HorizontalPanelAction.SeeAll) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.see_all),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(movies.take(10)) { movie ->
                        Column(
                            modifier = Modifier.width(120.dp).clip(RoundedCornerShape(10.dp))
                                .clickable { onClick(HorizontalPanelAction.Item(movie)) }
                        ) {
                            AsyncImage(
                                model = "$IMAGE_URL_ORIGINAL${movie.posterPath}",
                                contentDescription = movie.title,
                                modifier = Modifier
                                    .height(180.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Text(
                                text = movie.title!!,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(SelectiveYellow),
                                    modifier = Modifier.size(24.dp)
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

sealed class HorizontalPanelAction {
    object SeeAll : HorizontalPanelAction()
    data class Item<out T>(val data: T) : HorizontalPanelAction()
}
