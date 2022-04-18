package com.example.giphy.di

import android.content.Context
import androidx.room.Room
import com.example.giphy.db.AppDatabase
import com.example.giphy.db.dao.GifDao
import com.example.giphy.remote.GifEndPoints
import com.example.giphy.repos.GifRepo
import com.example.giphy.utils.Const.BASE_URL
import com.example.giphy.utils.Const.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    fun provideGifDao(appDataBase: AppDatabase) = appDataBase.gifDao()

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideGifService(retrofit: Retrofit): GifEndPoints = retrofit.create(GifEndPoints::class.java)

    @Singleton
    @Provides
    fun provideRepoService(gifEndPoints: GifEndPoints, gifDao: GifDao): GifRepo = GifRepo(gifEndPoints, gifDao)

}