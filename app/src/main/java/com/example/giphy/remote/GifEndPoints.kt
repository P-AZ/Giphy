package com.example.giphy.remote

import com.example.giphy.model.GifResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GifEndPoints {

    /** Returns the current trending gifs - pagination */
    @GET("trending")
    suspend fun getAllGifs(
        @Query("api_key") apiKey: String,
        @Query("offset") pageNumber: Int,
        @Query("limit") pageSize: Int
    ): Response<GifResponse>

    /** Returns the gifs according to the query - pagination */
    @GET("search")
    suspend fun getGifsBySearch(
        @Query("api_key") apiKey: String,
        @Query("q") searchQuery: String,
        @Query("offset") pageNumber: Int,
        @Query("limit") pageSize: Int
    ): Response<GifResponse>
}