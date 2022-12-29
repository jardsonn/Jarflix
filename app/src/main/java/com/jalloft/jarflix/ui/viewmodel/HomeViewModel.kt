package com.jalloft.jarflix.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jalloft.jarflix.model.movie.detail.MovieDetails
import com.jalloft.jarflix.remote.MovieRepository
import com.jalloft.jarflix.remote.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private suspend fun getMovieTrending() = repository.getMovieTrending()

    private suspend fun getPopularMovie() = repository.getPopularMovieList()

    private suspend fun getTopRatedMovie() = repository.getTopRatedMovieList()

    private suspend fun getGenreList() = repository.getGenreList()

    private suspend fun getMovieDetails(movieId: Int) = repository.getMovieDetails(movieId)

    private suspend fun getMovieVideo(movieId: Int) = repository.getMovieVideo(movieId)

    private suspend fun searchRemoteMovie(query: String, showAdultResult: Boolean = false) =
        repository.searchMovie(query, showAdultResult = showAdultResult)

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _remoteTrendingCallState = MutableLiveData<RemoteCallState>()
    val remoteTrendingCallState: LiveData<RemoteCallState> = _remoteTrendingCallState

    private val _remoteGenreCallState = MutableLiveData<RemoteCallState>()
    val remoteGenreCallState: LiveData<RemoteCallState> = _remoteGenreCallState

    private val _remotePopularMovieCallState = MutableLiveData<RemoteCallState>()
    val remotePopularMovieCallState: LiveData<RemoteCallState> = _remotePopularMovieCallState

    private val _remoteTopRatedMovieCallState = MutableLiveData<RemoteCallState>()
    val remoteTopRatedMovieCallState: LiveData<RemoteCallState> = _remoteTopRatedMovieCallState

    private val _remoteSearchMovieCallState = MutableLiveData<RemoteCallState>()
    val remoteSearchMovieCallState: LiveData<RemoteCallState> = _remoteSearchMovieCallState

//    private val _remoteMovieDetailsCallState = MutableLiveData<RemoteCallState>()
//    val remoteMovieDetailsCallState: LiveData<RemoteCallState> = _remoteMovieDetailsCallState


      private val _remoteMovieDetails = MutableLiveData<MovieDetails>()
    val remoteMovieDetails: LiveData<MovieDetails> = _remoteMovieDetails




// private val _remoteMovieVideoCallState = MutableLiveData<RemoteCallState>()
//    val remoteMovieVideoCallState: LiveData<RemoteCallState> = _remoteMovieVideoCallState


    init {
        makeRemoteCall(_remoteTrendingCallState) { getMovieTrending() }
        makeRemoteCall(_remoteGenreCallState) { getGenreList() }
        makeRemoteCall(_remotePopularMovieCallState) { getPopularMovie() }
        makeRemoteCall(_remoteTopRatedMovieCallState) { getTopRatedMovie() }
    }

    fun initMovieDetails(movieId: Int){
//        makeRemoteCall(_remoteMovieDetailsCallState){getMovieDetails(movieId)}
        viewModelScope.launch {
            getMovieDetails(movieId).collect{
                if (it.status == ResultState.Status.SUCCESS){
                    _remoteMovieDetails.postValue(it.data)
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchMovie(query)
        }
    }

    private fun searchMovie(query: String, showAdultResult: Boolean = false) {
        viewModelScope.launch {
            searchRemoteMovie(query, showAdultResult).collect {
                when (it.status) {
                    ResultState.Status.LOADING -> {
                        _remoteSearchMovieCallState.postValue(RemoteCallState.Loading)
                    }
                    ResultState.Status.SUCCESS -> {
                        _remoteSearchMovieCallState.postValue(RemoteCallState.Success(it.data))
                    }
                    ResultState.Status.ERROR -> {
                        _remoteSearchMovieCallState.postValue(RemoteCallState.Error)
                    }
                }
            }
        }
    }

    private fun <T> makeRemoteCall(
        remoteCallState: MutableLiveData<RemoteCallState>,
        call: suspend () -> Flow<ResultState<T>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            call.invoke().collect { resultState ->
                when (resultState.status) {
                    ResultState.Status.LOADING -> {
                        remoteCallState.postValue(RemoteCallState.Loading)
                    }
                    ResultState.Status.SUCCESS -> {
                        remoteCallState.postValue(RemoteCallState.Success(resultState.data))
                    }
                    ResultState.Status.ERROR -> {
                        remoteCallState.postValue(RemoteCallState.Error)
                    }
                }
            }
        }
    }
//
//    private fun loadPopularMovieList() {
//        viewModelScope.launch(Dispatchers.IO) {
//
//        }
//    }

//    private fun loadGenreList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            getGenreList().collect { resultState ->
//                when (resultState.status) {
//                    ResultState.Status.LOADING -> {
//                        _remoteGenreCallState.postValue(RemoteCallState.Loading)
//                    }
//                    ResultState.Status.SUCCESS -> {
//                        _remoteGenreCallState.postValue(RemoteCallState.Success(resultState.data))
//                    }
//                    ResultState.Status.ERROR -> {
//                        _remoteGenreCallState.postValue(RemoteCallState.Error)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun loadMovieTrending() {
//        viewModelScope.launch(Dispatchers.IO) {
//            getMovieTrending().collect { resultState ->
//                when (resultState.status) {
//                    ResultState.Status.LOADING -> {
//                        _remoteTrendingCallState.postValue(RemoteCallState.Loading)
//                    }
//                    ResultState.Status.SUCCESS -> {
//                        _remoteTrendingCallState.postValue(RemoteCallState.Success(resultState.data))
//                    }
//                    ResultState.Status.ERROR -> {
//                        _remoteTrendingCallState.postValue(RemoteCallState.Error)
//                    }
//                }
//            }
//        }
//    }

}