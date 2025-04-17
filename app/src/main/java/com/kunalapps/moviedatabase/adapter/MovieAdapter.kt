package com.kunalapps.moviedatabase.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kunalapps.moviedatabase.R
import com.kunalapps.moviedatabase.model.Movie

class MovieAdapter(
    private val onClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val poster = view.findViewById<ImageView>(R.id.imagePoster)
        private val title = view.findViewById<TextView>(R.id.textTitle)
        private val date = view.findViewById<TextView>(R.id.textReleaseDate)
        private val rating = view.findViewById<TextView>(R.id.textRating)
        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bind(movie: Movie) {

            title.text = movie.title
            date.text = movie.release_date
            val ratingFormatted = String.format("%.1f/10", movie.vote_average)
            rating.text =ratingFormatted
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w500${movie.poster_path}").into(poster)
            itemView.setOnClickListener { onClick(movie) }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(old: Movie, new: Movie) = old.id == new.id
        override fun areContentsTheSame(old: Movie, new: Movie) = old == new
    }
}
