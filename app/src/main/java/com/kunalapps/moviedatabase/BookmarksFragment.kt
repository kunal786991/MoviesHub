package com.kunalapps.moviedatabase

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunalapps.moviedatabase.adapter.MovieAdapter
import com.kunalapps.moviedatabase.viewModel.MovieViewModel
import com.kunalapps.moviedatabase.viewModel.MovieViewModelFactory

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory((requireActivity().application as MyApp).repository)
    }

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
