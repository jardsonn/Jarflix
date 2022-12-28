package com.jalloft.jarflix.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jalloft.jarflix.remote.MovieRemoteDataSource
import com.jalloft.jarflix.remote.MovieRepository
import com.jalloft.jarflix.remote.MovieService
import com.jalloft.jarflix.utils.API_URL_BASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(API_URL_BASE)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(service: MovieService) =
        MovieRemoteDataSource(service)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: MovieRemoteDataSource) =
        MovieRepository(remoteDataSource)

}