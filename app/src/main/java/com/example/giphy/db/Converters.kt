package com.example.giphy.db

import androidx.room.TypeConverter
import com.example.giphy.model.Images
import com.example.giphy.model.Original

class Converters {

//    @TypeConverter
//    fun fromLongToString(long: Long?) = long?.toString()
//
//    @TypeConverter
//    fun fromStringToLong(string: String?) = string?.toLong()

    @TypeConverter
    fun fromImagesToOriginal(images: Images) = images.original

    @TypeConverter
    fun fromOriginalToImages(original: Original) = Images(original)

//    @TypeConverter
//    fun fromOriginalToString(original: Original) =
}