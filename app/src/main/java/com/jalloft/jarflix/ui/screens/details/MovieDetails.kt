package com.jalloft.jarflix.ui.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.jalloft.jarflix.model.videos.MovieVideo
import com.jalloft.jarflix.model.videos.VideoResult
import com.jalloft.jarflix.ui.components.YouTubePlayer
import com.jalloft.jarflix.ui.viewmodel.HomeViewModel
import timber.log.Timber


@Composable
fun MovieDetails(viewModel: HomeViewModel = hiltViewModel(), movieId: Int) {
    viewModel.initMovieDetails(movieId)
    val movieDetailsReult by viewModel.remoteMovieDetailsCallState.observeAsState()
    val movieVideoReult by viewModel.remoteMovieVideoCallState.observeAsState()

    if (movieVideoReult is HomeViewModel.RemoteCallState.Success<*>) {
        val videoResult = ((movieVideoReult as HomeViewModel.RemoteCallState.Success<*>).media as MovieVideo).results
        videoResult?.get(0)?.key?.let {
            YouTubePlayer(it)
        }
    }


}