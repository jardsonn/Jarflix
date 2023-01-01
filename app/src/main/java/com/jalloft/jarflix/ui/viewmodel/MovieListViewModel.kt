package com.jalloft.jarflix.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jalloft.jarflix.model.genre.toGenre
import com.jalloft.jarflix.model.movie.MovieRowType
import com.jalloft.jarflix.model.movie.MovieResult
import com.jalloft.jarflix.remote.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    val similarMovieNameReference = savedState.get<String>("movieName")
    private val currentMovieId = savedState.get<Int>("movieId")

    val currentGenre = savedState.get<String>("genre")

    private val _itemRowType = savedState.get<String>("itemType")
    val itemRowType =
        if (!_itemRowType.isNullOrEmpty()) MovieRowType.valueOf(_itemRowType) else MovieRowType.SIMILAR

    private var lastScrollIndex = 0
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }

    fun getMoviePagigation(): Flow<PagingData<MovieResult>> {
        val genreId = if (currentGenre.isNullOrEmpty()) -1 else currentGenre.toGenre()?.id
        return repository.getMoviePagigation(itemRowType, currentMovieId ?: -1, genreId ?: -1)
            .cachedIn(viewModelScope).flowOn(Dispatchers.IO)
    }
}