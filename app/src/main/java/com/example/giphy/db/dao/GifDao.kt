package com.example.giphy.db.dao

import androidx.room.*
import com.example.giphy.model.Gif
import kotlinx.coroutines.flow.Flow

@Dao
interface GifDao {
    //In case of 2 identical gifs - replace the old one with the new gif
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateToFavorites(gif: Gif)

    @Delete
    fun deleteFromFavorites(gif: Gif)

    @Query("SELECT * FROM Favorites ORDER BY title ASC")
    fun getAllFavorite(): Flow<List<Gif>>

    //Returns all the favorite gifs with include the search query
    @Query("SELECT * FROM Favorites WHERE (slug) LIKE :keyword ORDER BY title COLLATE NOCASE ASC")
    fun getFavoriteGifsBySearch(keyword: String) : Flow<List<Gif>>

}