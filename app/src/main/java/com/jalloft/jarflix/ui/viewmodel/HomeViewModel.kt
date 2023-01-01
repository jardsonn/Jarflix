package com.jalloft.jarflix.ui.viewmodel

import androidx.lifecycle.*
import com.jalloft.jarflix.model.movie.Movie
import com.jalloft.jarflix.model.movie.detail.MovieCastCrew
import com.jalloft.jarflix.model.movie.detail.MovieDetails
import com.jalloft.jarflix.remote.MovieRepository
import com.jalloft.jarflix.remote.ResultState
import com.jalloft.jarflix.ui.components.TopAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    private val _topBarState = MutableLiveData(TopAppBarState.NORMAL)
    val topBarState: LiveData<TopAppBarState> = _topBarState

    fun updateTopBarState(state: TopAppBarState) {
        _topBarState.value = state
    }


    private suspend fun getMovieTrending(page: Int = 1) = repository.getMovieTrending(page = page)

    private suspend fun getPopularMovie(page: Int = 1) = repository.getPopularMovieList(page = page)

    private suspend fun getTopRatedMovie(page: Int = 1) =
        repository.getTopRatedMovieList(page = page)

    private suspend fun getGenreList() = repository.getGenreList()

    private suspend fun getMovieDetails(movieId: Int) = repository.getMovieDetails(movieId)

    private suspend fun getMovieVideo(movieId: Int) = repository.getMovieVideo(movieId)

    private suspend fun getMovieCast(movieId: Int) = repository.getMovieCast(movieId)

    private suspend fun getSimilarMovie(movieId: Int, page: Int = 1) =
        repository.getSimilarMovie(movieId, page = page)

    private suspend fun searchRemoteMovie(
        query: String,
        showAdultResult: Boolean = false,
        page: Int = 1
    ) =
        repository.searchMovie(query, showAdultResult = showAdultResult, page = page)

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

    private val _remoteMovieDetails = MutableLiveData<MovieDetails>()
    val remoteMovieDetails: LiveData<MovieDetails> = _remoteMovieDetails

    private val _remoteMovieCastCrew = MutableLiveData<MovieCastCrew>()
    val remoteMovieCastCrew: LiveData<MovieCastCrew> = _remoteMovieCastCrew

    private val _remoteSimilarMovie = MutableLiveData<Movie>()
    val remoteSimilarMovie: LiveData<Movie> = _remoteSimilarMovie


    init {
        makeRemoteCall(_remoteTrendingCallState) { getMovieTrending() }
        makeRemoteCall(_remoteGenreCallState) { getGenreList() }
        makeRemoteCall(_remotePopularMovieCallState) { getPopularMovie() }
        makeRemoteCall(_remoteTopRatedMovieCallState) { getTopRatedMovie() }
    }

    fun initMovieDetails(movieId: Int) {
//        makeRemoteCall(_remoteMovieDetailsCallState){getMovieDetails(movieId)}
        viewModelScope.launch {
            getMovieDetails(movieId).collect {
                if (it.status == ResultState.Status.SUCCESS) {
                    _remoteMovieDetails.postValue(it.data)
                }
            }
            getMovieCast(movieId).collect {
                if (it.status == ResultState.Status.SUCCESS) {
                    _remoteMovieCastCrew.postValue(it.data)
                }
            }

            getSimilarMovie(movieId).collect {
                if (it.status == ResultState.Status.SUCCESS) {
                    _remoteSimilarMovie.postValue(it.data)
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