package com.example.giphy.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.giphy.db.Converters
import kotlinx.parcelize.Parcelize

//Using interface for scalability

interface IGif {
    val id: String?
    val type: String?
    val title: String?
    val username: String?
    val rating: String?
    val images: Images?
    val slug: String?
}

@Parcelize
@Entity(tableName = "Favorites")
data class Gif (
    @PrimaryKey
    override val id: String,
    override val type: String?,
    override val title: String?,
    override val username: String?,
    override val rating: String?,
    @Embedded override val images: Images?,
    override val slug: String?,
    var isFavorite: Boolean = false
) : IGif, Parcelable

@Parcelize
data class Images (
    @Embedded val original: Original
) : Parcelable

@Parcelize
data class Original (
    val height: Long?,
    val width: Long?,
    val url: String?
) : Parcelable

//Data class that will hold the value of the server response
data class GifResponse (
    val data: List<Gif>,
    val pagination: Paging
)

data class Paging (
    val total_count: Int?,
    val count: Int?,
    val offset: Int?
)
