package com.example.giphy.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.giphy.R
import com.example.giphy.databinding.HolderGifBinding
import com.example.giphy.model.Gif
import kotlinx.coroutines.flow.collect

class GifAdapter(
    private val onGifClick: (Gif) -> Unit,
    private val onFavoriteClick: (Gif) -> Unit
) : PagingDataAdapter<Gif, GifAdapter.GifViewHolder>(GifDiffCallback) {

    class GifViewHolder(
        private val holderViewBinding: HolderGifBinding,
        gifClickAtPosition: (Int) -> Unit,
        favoriteClickAtPosition: (Int) -> Unit
    ) : RecyclerView.ViewHolder(holderViewBinding.root) {

        //setting clickListener for the relevant views and sending higher lambda with the current adapter position,
        //only here its possible to retrieve adapter position and adapter position correspond to list position
        init {
            holderViewBinding.apply {
                root.setOnClickListener {
                    gifClickAtPosition(adapterPosition)
                }
                holderGifFavoriteBtn.setOnClickListener {
                    favoriteClickAtPosition(adapterPosition)
                }
                holderGifFavoriteBtn.text = "Add"
            }
        }

        fun bind(gif: Gif) {
            Log.d(TAG, gif.images?.original?.url.toString())
            holderViewBinding.apply {
                Glide.with(this.root)
                    .asGif()
                    .load(gif.images?.original?.url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
//                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holderGifGifImg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {

        val gifClickAtPosition: (Int) -> Unit = { position ->
            getItem(position)?.let { onGifClick(it) }
        }
        val favoriteClickAtPosition: (Int) -> Unit = { position ->
            getItem(position)?.let { onFavoriteClick(it) }
        }
        return GifViewHolder(
            HolderGifBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            gifClickAtPosition, favoriteClickAtPosition
        )
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private const val TAG = "GifAdapterTAG"
    }

}

object GifDiffCallback : DiffUtil.ItemCallback<Gif>() {
    override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean = oldItem == newItem
}
