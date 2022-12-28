package com.jalloft.jarflix.remote

import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber


abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>) = flow<ResultState<T>> {
        try{
            emit(ResultState.loading())
            val response = call()
            if (response.isSuccessful){
                val body = response.body()
                if(body != null) {
                    emit(ResultState.success(body))
                }else{
                    emit(error("${response.code()} ${response.message()}"))
                }
            }else{
                emit(error("${response.code()} ${response.message()}"))
                Timber.e("A chamada de rede falhou pelo seguinte motivo: ${response.message()}")
            }
        }catch (e: Exception){
            emit(error(e.message ?: e.toString()))
        }
    }

    private fun <T> error(message: String): ResultState<T> {
        Timber.d(message)
        return ResultState.error("A chamada de rede falhou pelo seguinte motivo: $message")
    }
}