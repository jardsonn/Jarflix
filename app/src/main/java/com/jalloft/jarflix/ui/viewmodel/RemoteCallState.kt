package com.jalloft.jarflix.ui.viewmodel

sealed class RemoteCallState {
    object Loading : RemoteCallState()
    data class Success<out T>(val media: T?) : RemoteCallState()
    object Error : RemoteCallState()

}

