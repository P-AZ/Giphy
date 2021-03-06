package com.example.giphy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.giphy.db.dao.GifDao
import com.example.giphy.model.Gif

@Database(entities = [
    Gif::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gifDao(): GifDao
}