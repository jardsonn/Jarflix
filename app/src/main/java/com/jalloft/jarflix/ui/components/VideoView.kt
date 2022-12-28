package com.jalloft.jarflix.ui.components

import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.get
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import timber.log.Timber

@Composable
fun YouTubePlayer(videoId: String){
    val context = LocalContext.current
    val viewView = YouTubePlayerView(context).apply {
        addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
               youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }
    DisposableEffect(
        AndroidView(factory ={
            viewView
        })
    ){
        onDispose { viewView.release() }
    }
}