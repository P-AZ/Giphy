package com.example.giphy.extensions

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.giphy.model.Gif
import com.example.giphy.model.GifAttributes

//This object will contain extension functions
object Extensions {

    /** return true if the index is within the list limits */
    fun Int.isIndexListValid(listSize: Int): Boolean = this in 0 until listSize

    /** return true if float is greater or equal to 0 */
    fun Float.isPositive() = this >= 0

    /** update the list with the updated values of the token attribute sent */
    fun List<Gif?>.updateList(gif: Gif, gifAttribute: GifAttributes): List<Gif?> {
        val index = this.indexOfFirst { it?.id == gif.id }
        if(index.isIndexListValid(this.size)) {
            this[index]?.let { currentGif ->
                when(gifAttribute) {
                    GifAttributes.IS_FAVORITE -> currentGif.isFavorite = !currentGif.isFavorite
                    GifAttributes.IS_EXPANDED -> currentGif.isExpanded = !currentGif.isExpanded
                }
                //creating copy of list due to Diffutill not notice the different
                val tempTokensList = mutableListOf<Gif>()
                this.forEach { newToken ->
                    newToken?.let {
                        tempTokensList.add(it.copy())
                    }
                }
                return tempTokensList
            }
        }
        return this
    }
}