package com.kunalapps.moviedatabase.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kunalapps.moviedatabase.model.Movie

@Database(entities = [Movie::class], version = 4, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                                context.applicationContext,
                                MovieDatabase::class.java,
                                "movie_database"
                            ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it }
            }
        }
    }
}
