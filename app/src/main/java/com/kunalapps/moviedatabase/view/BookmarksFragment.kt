package com.kunalapps.moviedatabase.view

import android.os.Bundle
import android.view.View
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
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val viewModel: MovieViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = MovieAdapter {
            findNavController().navigate(BookmarksFragmentDirections.actionBookmarksToDetails(it.id))
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvBookmarks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.bookmarked.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
