package com.kunalapps.moviedatabase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.kunalapps.moviedatabase.viewModel.MovieViewModel
import com.kunalapps.moviedatabase.viewModel.MovieViewModelFactory

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory((requireActivity().application as MyApp).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val apiKey = "54969c34fe6c23296273a590dafbc63a"
        val movieId = arguments?.getInt("movieId") ?: return
        val imgPoster = view.findViewById<ImageView>(R.id.imagePoster)
        val textTitle = view.findViewById<TextView>(R.id.textTitle)
        val textOverview = view.findViewById<TextView>(R.id.textOverview)
        val btnBookmark = view.findViewById<CheckBox>(R.id.btnBookmark)
        val btnShare = view.findViewById<ImageButton>(R.id.btnShare)
        val rating = view.findViewById<TextView>(R.id.textRating)


        viewModel.loadMovie(movieId, apiKey).observe(viewLifecycleOwner) { movie ->
            movie?.let {
                textTitle.text = it.title
                textOverview.text = it.overview
                btnBookmark.isChecked = it.isBookmarked
                val ratingFormatted = String.format("%.1f/10", movie.vote_average)
                rating.text = ratingFormatted
                btnBookmark.text = if (it.isBookmarked) "Unbookmark" else "Bookmark"
                Glide.with(this).load("https://image.tmdb.org/t/p/w500${it.poster_path}")
                    .into(imgPoster)

                btnBookmark.setOnClickListener {
                    viewModel.toggleBookmark(movie)
                    btnBookmark.text = if (btnBookmark.isChecked) "Unbookmark" else "Bookmark"
                }

            }

        }
        btnShare.setOnClickListener {
            val deepLink = "https://com.kunalapps.moviedatabase/movie/${movieId}"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this movie: $deepLink")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

    }
}
