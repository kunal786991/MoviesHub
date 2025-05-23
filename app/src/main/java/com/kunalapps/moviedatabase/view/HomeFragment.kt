package com.kunalapps.moviedatabase.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunalapps.moviedatabase.R
import com.kunalapps.moviedatabase.adapter.MovieAdapter
import com.kunalapps.moviedatabase.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var nowPlayingAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val apiKey = "54969c34fe6c23296273a590dafbc63a"
        val toolbar = view.findViewById<Toolbar>(R.id.homeToolbar)
        toolbar.inflateMenu(R.menu.menu_home)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeToSearch())
                    true
                }

                R.id.action_bookmarks -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeToBookmarks())
                    true
                }

                else -> false
            }
        }
        trendingAdapter = MovieAdapter { movie ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetails(movie.id))
        }
        nowPlayingAdapter = MovieAdapter { movie ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetails(movie.id))
        }

        val trendingRecyclerView = view.findViewById<RecyclerView>(R.id.rvTrending)
        val nowPlayingRecyclerView = view.findViewById<RecyclerView>(R.id.rvNowPlaying)

        trendingRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        nowPlayingRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        trendingRecyclerView.adapter = trendingAdapter
        nowPlayingRecyclerView.adapter = nowPlayingAdapter

        viewModel.fetchTrending(apiKey)
        viewModel.fetchNowPlaying(apiKey)

        viewModel.moviesByCategoryNowPlaying.observe(viewLifecycleOwner) { movies ->
            nowPlayingAdapter.submitList(movies.filter { it.title.isNotEmpty() })
        }
        viewModel.moviesByCategoryTrending.observe(viewLifecycleOwner) { movies ->
            trendingAdapter.submitList(movies.filter { it.title.isNotEmpty() })
        }
    }

}
