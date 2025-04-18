package com.kunalapps.moviedatabase.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kunalapps.moviedatabase.model.Movie
import com.kunalapps.moviedatabase.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    val moviesByCategoryTrending: LiveData<List<Movie>> =
        repository.getMoviesByCategory("trending").asLiveData()
    val moviesByCategoryNowPlaying: LiveData<List<Movie>> =
        repository.getMoviesByCategory("now_playing").asLiveData()
    val bookmarked: LiveData<List<Movie>> = repository.getBookmarkedMovies().asLiveData()

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    fun fetchTrending(apiKey: String) = viewModelScope.launch{
        repository.fetchAndCacheTrending(apiKey)
    }

    fun fetchNowPlaying(apiKey: String) = viewModelScope.launch {
        repository.fetchAndCacheNowPlaying(apiKey)
    }

    fun search(query: String, apiKey: String) = viewModelScope.launch {
        _searchResults.value = repository.searchMovies(query, apiKey)
    }

    fun toggleBookmark(movie: Movie) = viewModelScope.launch {
        repository.toggleBookmark(movie)
    }

    fun loadMovie(id: Int, apiKey: String): LiveData<Movie?> = liveData {
        emit(repository.loadMovie(id, apiKey))
    }
}

