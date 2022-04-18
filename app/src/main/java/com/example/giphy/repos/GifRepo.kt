package com.example.giphy.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.giphy.db.dao.GifDao
import com.example.giphy.model.Gif
import com.example.giphy.paging.GifPagingSource
import com.example.giphy.remote.GifEndPoints
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GifRepo @Inject constructor(
    private val gifEndPoints: GifEndPoints,
    private val gifDao: GifDao
) {
    private val API_KEY = "lITqTFLzmkGcN54lFnjqMZQOJ79Zj6c9"

    //Get gifs by query search
    fun getGifsBySearch(searchQuery: String) =
        Pager(
            pagingSourceFactory = {
                GifPagingSource(
                    gifEndPoints = gifEndPoints,
                    apiKey = API_KEY,
                    searchQuery = searchQuery
                )
            },
            config = PagingConfig(
                pageSize = 2
            )
        ).flow

    //Get all favorites gif from DB
    fun getAllFavoritesGif(): Flow<List<Gif>> = gifDao.getAllFavorite()

    //Insert gif into favorites from DB
    fun insertOrUpdateToFavorites(gif: Gif) = gifDao.insertOrUpdateToFavorites(gif)

    //Delete gif from favorites from DB
    fun deleteGifFromFavorites(gif: Gif) = gifDao.deleteFromFavorites(gif)

    //Get favorite gifs by search from DB
    fun getFavoriteGifsBySearch(keyword: String) = gifDao.getFavoriteGifsBySearch("%$keyword%")

    companion object {
        private const val TAG = "GifRepoTAG"
    }
}