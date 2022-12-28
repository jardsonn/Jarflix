package com.jalloft.jarflix.remote


data class ResultState<out T>(val status: Status, val data: T?, val message: String? = null){
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): ResultState<T> {
            return ResultState(Status.SUCCESS, data)
        }

        fun <T> error(message: String, data: T? = null): ResultState<T> {
            return ResultState(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): ResultState<T> {
            return ResultState(Status.LOADING, data)
        }
    }
}