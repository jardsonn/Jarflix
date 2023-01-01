package com.jalloft.jarflix.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jalloft.jarflix.R
import com.jalloft.jarflix.model.genre.Genre
import com.jalloft.jarflix.model.genre.GenreObject
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.model.movie.detail.Cast
import com.jalloft.jarflix.model.movie.detail.Crew
import com.jalloft.jarflix.ui.theme.White
import com.jalloft.jarflix.ui.viewmodel.RemoteCallState
import com.jalloft.jarflix.utils.toImageOrigial

@Composable
fun HorizontalGenrePanel(
    modifier: Modifier = Modifier,
    title: String,
    remoteState: RemoteCallState?,
    onGenreSelected: (Genre) -> Unit
) {
    remoteState?.let { state ->
        if (state is RemoteCallState.Success<*>) {
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
fun HorizontalStateMoviePanel(
    rowType: MovieRowType,
    modifier: Modifier = Modifier,
    title: String,
    remoteState: RemoteCallState?,
    onMovieClicked: (MovieResult) -> Unit,
    onSeeMore: (MovieRowType) -> Unit,
) {
    remoteState?.let { state ->
        if (state is RemoteCallState.Success<*>) {
            val movies = (state.media as Movie).results
            HorizontalMoviePanel(
                rowType,
                modifier,
                title,
                movies,
                onMovieClicked = { onMovieClicked(it) },
                onSeeMore = { onSeeMore(it) })
        }
    }
}

@Composable
fun HorizontalMoviePanel(
    rowType: MovieRowType,
    modifier: Modifier,
    title: String,
    movies: List<MovieResult>,
    onMovieClicked: (MovieResult) -> Unit,
    onSeeMore: (MovieRowType) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            TextButton(
                onClick = { onSeeMore(rowType) }
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
                .fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies.take(5)) { movie ->
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onMovieClicked(movie) }
                ) {
                    Box(
                        contentAlignment = Alignment.TopStart,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = movie.posterPath?.toImageOrigial,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        )
                        StarVote(
                            movie = movie,
                            modifier = Modifier.background(MaterialTheme.colorScheme.primary,  RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp))
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

    }
}

@Composable
fun HorizontalCast(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.cast),
    castList: List<Cast?>?,
    onItemSelected: (HorizontalPanelAction) -> Unit
) {
    castList?.let { list ->
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(
                    onClick = { onItemSelected(HorizontalPanelAction.SeeAll) }
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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(list.take(5)) { cast ->
                    Column(
                        modifier = Modifier
                            .width(110.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onItemSelected(HorizontalPanelAction.Item(cast)) }
                    ) {
                        if (cast?.profilePath != null) {
                            AsyncImage(
                                model = cast.profilePath.toImageOrigial,
                                contentDescription = cast.originalName,
                                modifier = Modifier
                                    .height(170.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        } else {
                            Image(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(170.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),
                                colorFilter = ColorFilter.tint(
                                    MaterialTheme.colorScheme.onSurface.copy(
                                        0.5f
                                    )
                                )
                            )
                        }

                        Text(
                            text = cast?.originalName ?: stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = cast?.character ?: stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun HorizontalCrew(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.crew),
    castList: List<Crew?>?,
    onItemSelected: (HorizontalPanelAction) -> Unit
) {
    castList?.let { list ->
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(
                    onClick = { onItemSelected(HorizontalPanelAction.SeeAll) }
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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(list.take(5)) { cast ->
                    Column(
                        modifier = Modifier
                            .width(110.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onItemSelected(HorizontalPanelAction.Item(cast)) }
                    ) {
                        if (cast?.profilePath != null) {
                            AsyncImage(
                                model = cast.profilePath.toImageOrigial,
                                contentDescription = cast.originalName,
                                modifier = Modifier
                                    .height(170.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        } else {
                            Image(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(170.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),
                                colorFilter = ColorFilter.tint(
                                    MaterialTheme.colorScheme.onSurface.copy(
                                        0.5f
                                    )
                                )
                            )
                        }

                        Text(
                            text = cast?.originalName ?: stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = cast?.knownForDepartment
                                ?: stringResource(id = R.string.not_found),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
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
