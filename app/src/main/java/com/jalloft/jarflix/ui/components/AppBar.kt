package com.jalloft.jarflix.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jalloft.jarflix.R
import com.jalloft.jarflix.ui.theme.DavysGrey


enum class TopAppBarState {
    NORMAL, SEARCH
}


@Composable
fun HomeTopAppBar(
    topBarState: TopAppBarState?,
    updateTopBaState: (TopAppBarState) -> Unit,
    onPerformQuery: (String) -> Unit
) {

    Column {
        Crossfade(
            targetState = topBarState,
            animationSpec = tween(durationMillis = 500)
        ) { state ->
            when (state) {
                TopAppBarState.NORMAL -> {
                    NormalTopAppBar(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                        onSearchClicked = { updateTopBaState(TopAppBarState.SEARCH )}
                    )
                }
                TopAppBarState.SEARCH -> {
                    SearchTopAppBar(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                        onPerformQuery = { onPerformQuery(it) },
                        onCancelClicked = { updateTopBaState(TopAppBarState.NORMAL) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchTopAppBar(
    modifier: Modifier,
    onPerformQuery: (String) -> Unit,
    onCancelClicked: () -> Unit
) {
    val (text, setText) = rememberSaveable{ mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            value = text,
            onValueChange = {
                setText(it)
                onPerformQuery(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.search_hint),
                        color = DavysGrey,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                it()
            }
        )
        IconButton(
            onClick = {
                onCancelClicked()
                onPerformQuery("")
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(id = android.R.string.cancel)
            )
        }
    }
}

@Composable
fun NormalTopAppBar(modifier: Modifier, onSearchClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { onSearchClicked() }) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        }
    }
}
