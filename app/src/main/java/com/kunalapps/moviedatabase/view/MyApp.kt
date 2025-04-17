package com.kunalapps.moviedatabase.view

import com.kunalapps.moviedatabase.data.local.MovieDatabase
import com.kunalapps.moviedatabase.repository.MovieRepository

import android.app.Application
import com.kunalapps.moviedatabase.data.remote.RetrofitClient

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
