package com.kunalapps.moviedatabase.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kunalapps.moviedatabase.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE Category = :category")
    fun getMoviesByCategory(category: String): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE isBookmarked = 1")
    fun getBookmarkedMovies(): Flow<List<Movie>>

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)
}

