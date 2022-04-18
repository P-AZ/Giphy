package com.example.giphy.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.giphy.extensions.Extensions.isIndexListValid
import com.example.giphy.extensions.Extensions.updateList
import com.example.giphy.model.Gif
import com.example.giphy.model.GifAttributes
import com.example.giphy.paging.GifPagingSource
import com.example.giphy.repos.GifRepo
import com.example.giphy.utils.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifViewModel @Inject constructor(private val gifRepo: GifRepo) : ViewModel() {

    // Hold the states of the application
    val state = MutableLiveData<States>().apply {
        postValue(States.Idle)
    }

    private var gifFavoritesList = mutableListOf<Gif>()
    val gifFavoritesListEvent = MutableLiveData<List<Gif?>?>()
    var gifPagingRegularListEvent: Flow<PagingData<Gif>> = emptyFlow()

    private val queryPagingEvent: MutableStateFlow<String> = MutableStateFlow("")

    init {
        initRoomData()
        initRepoData()
    }

    private fun initRepoData() {
        getGifsBySearch()
        initPagingQuery()
    }

    private fun initPagingQuery() {
        queryPagingEvent.value = "null"
    }

    private fun initRoomData() {
        getAllFavorites()
    }

    // getGifsBySearch get all gifs from server while listening to query changes
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getGifsBySearch() {
        gifPagingRegularListEvent = queryPagingEvent.flatMapLatest { query ->
            gifRepo.getGifsBySearch(query).cachedIn(viewModelScope)
        }
    }

    fun updateQueryPaging(query: String) {
        queryPagingEvent.value = query
    }

    //getAllFavorites get all favorites gif from db
    fun getAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            gifRepo.getAllFavoritesGif().collect { favoritesList ->
                gifFavoritesList = favoritesList.toMutableList()
                gifFavoritesListEvent.postValue(gifFavoritesList)
                state.postValue(States.Idle)
            }
        }
    }

    // getFavoriteGifsBySearch get gifs from favorites by query search
    fun getFavoriteGifsBySearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            gifRepo.getFavoriteGifsBySearch(query).collectLatest { favoritesList ->
                gifFavoritesList = favoritesList.toMutableList()
                gifFavoritesListEvent.postValue(gifFavoritesList)
                state.postValue(States.Idle)
            }
        }
    }

    //insertGifToFavorites insert gif to favorites in db
    private fun insertOrUpdateGifToFavorites(gif: Gif) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            gifRepo.insertOrUpdateToFavorites(gif)
            state.postValue(States.Deleted)
            state.postValue(States.Idle)
        }
    }

    //deleteTokenFavorites delete gif from db
    private fun deleteGifFromFavorites(gif: Gif) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            gifRepo.deleteGifFromFavorites(gif)
            state.postValue(States.Idle)
        }
    }

    fun onFavoriteClick(gif: Gif) {
        insertOrUpdateGifToFavorites(gif)
    }

    fun onDeleteFavoriteClick(gif: Gif) {
        deleteGifFromFavorites(gif)
    }

    companion object {
        private const val TAG = "GifViewModelTAG"
    }
}
