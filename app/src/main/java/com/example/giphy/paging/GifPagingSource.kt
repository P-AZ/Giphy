package com.example.giphy.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.giphy.db.dao.GifDao
import com.example.giphy.model.Gif
import com.example.giphy.remote.GifEndPoints
import com.example.giphy.utils.Const.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException


private const val INITIAL_PAGE = 1

class GifPagingSource(
    val gifEndPoints: GifEndPoints,
    val apiKey: String,
    val searchQuery: String
) : PagingSource<Int, Gif>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Gif> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: INITIAL_PAGE
            val data = gifEndPoints.getGifsBySearch(
                apiKey = apiKey,
                searchQuery = searchQuery,
                pageNumber = pageNumber,
                pageSize = NETWORK_PAGE_SIZE
            ).body()?.data!!

            Log.d(TAG, "page number - $pageNumber")

            LoadResult.Page(
                data = data,
                prevKey = if (pageNumber == INITIAL_PAGE) null else pageNumber - 1,
                nextKey = if (data.isEmpty()) null else pageNumber + 1 //TODO("fix assertion")
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }



    companion object {
        private const val TAG = "GifPagingSourceTAG"
    }
}