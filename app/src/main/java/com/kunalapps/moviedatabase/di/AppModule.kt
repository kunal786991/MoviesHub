package com.kunalapps.moviedatabase.di

import android.content.Context
import androidx.room.Room
import com.kunalapps.moviedatabase.data.local.MovieDao
import com.kunalapps.moviedatabase.data.local.MovieDatabase
import com.kunalapps.moviedatabase.data.remote.ApiService
import com.kunalapps.moviedatabase.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)  // Use injected client
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideMovieDao(db: MovieDatabase): MovieDao {
        return db.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: ApiService,
        movieDao: MovieDao
    ): MovieRepository {
        return MovieRepository(apiService, movieDao)
    }
}
