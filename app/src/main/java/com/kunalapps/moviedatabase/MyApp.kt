package com.kunalapps.moviedatabase

import com.kunalapps.moviedatabase.data.local.MovieDatabase
import com.kunalapps.moviedatabase.repository.MovieRepository

import android.app.Application

class MyApp : Application() {
    lateinit var repository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val database = MovieDatabase.getInstance(this)
        val dao = database.movieDao()
        repository = MovieRepository(
            apiService = RetrofitClient.apiService,
            dao = dao
        )
    }
}
