package com.kunalapps.moviedatabase.repository


import android.util.Log
import com.kunalapps.moviedatabase.data.local.MovieDao
import com.kunalapps.moviedatabase.data.remote.ApiService
import com.kunalapps.moviedatabase.model.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val apiService: ApiService, private val dao: MovieDao) {
    private val TAG = "MovieRepository"
    suspend fun fetchAndCacheTrending(apiKey: String) {
        try {
            val response = apiService.getTrendingMovies(apiKey)
            response.body()?.let {
                val categorized = it.results.map { movie -> movie.copy(category = "trending") }
                for (movie in categorized) {
                    if (dao.getMovieById(movie.id) == null) {
                        dao.insertMovie(movie)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // fallback to nothing
        }
    }

    suspend fun fetchAndCacheNowPlaying(apiKey: String) {
        try{   val response = apiService.getNowPlayingMovies(apiKey)
        response.body()?.let {
            val categorized = it.results.map { movie -> movie.copy(category = "now_playing") }
            for (movie in categorized) {
                if (dao.getMovieById(movie.id) == null) {
                    dao.insertMovie(movie)
                }
            }
        } }catch (e: Exception) {
            e.printStackTrace()
            // fallback to nothing
        }
    }

    suspend fun searchMovies(query: String, apiKey: String): List<Movie> {
     return try{   val response = apiService.searchMovies(query, apiKey)
        val movies = response.body()?.results ?: emptyList()

        // Set category before inserting
        val categorized = movies.map { it.copy(category = "") }
         for (movie in categorized) {
             if (dao.getMovieById(movie.id) == null) {
                 dao.insertMovie(movie)
             }
         }
        return categorized}
     catch (e: Exception) {
         e.printStackTrace()
         emptyList()
         // fallback to nothing
     }
    }
    fun getMoviesByCategory(category: String): Flow<List<Movie>> = dao.getMoviesByCategory(category)
    fun getBookmarkedMovies(): Flow<List<Movie>> = dao.getBookmarkedMovies()
    suspend fun toggleBookmark(movie: Movie) {
        val localMovie = dao.getMovieById(movie.id)

        if (localMovie != null) {
            dao.updateMovie(localMovie.copy(isBookmarked = !localMovie.isBookmarked))
        } else {
            // fallback, maybe insert it with bookmark if not in DB
            dao.insertMovie(movie.copy(isBookmarked = true))
        }

    }

    suspend fun loadMovieWithNetworkFallback(movieId: Int, apiKey: String): Movie? {
        return try {
            val response = apiService.getMovieDetails(movieId, apiKey)
            val networkMovie = response.body()
            Log.i(TAG, "loadMovieWithNetworkFallback: $networkMovie")
            if (response.isSuccessful && networkMovie != null) {
                val localMovie = dao.getMovieById(movieId)
                val mergedMovie = networkMovie.copy(isBookmarked = localMovie?.isBookmarked ?: false)
                dao.insertMovie(mergedMovie) // Save it to DB
                mergedMovie
            } else {
                dao.getMovieById(movieId)
            }
        } catch (e: Exception) {
            dao.getMovieById(movieId)
        }
    }
}

