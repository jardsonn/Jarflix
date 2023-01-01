package com.jalloft.jarflix.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.ui.theme.SelectiveYellow
import java.text.DecimalFormat

@Composable
fun StarVote(movie: MovieResult, modifier: Modifier = Modifier){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(end = 8.dp, start = 8.dp)
    ) {
        Image(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            colorFilter = ColorFilter.tint(SelectiveYellow),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = DecimalFormat("#.#").apply {
                minimumFractionDigits = 1
            }.format(movie.voteAverage),
            style = MaterialTheme.typography.bodySmall
        )
    }
}