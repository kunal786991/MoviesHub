package com.kunalapps.moviedatabase.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunalapps.moviedatabase.R
import com.kunalapps.moviedatabase.adapter.MovieAdapter
import com.kunalapps.moviedatabase.viewModel.MovieViewModel
import com.kunalapps.moviedatabase.viewModel.MovieViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory((requireActivity().application as MyApp).repository)
    }
    private val TAG = "SearchFragment"

    private lateinit var adapter: MovieAdapter
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editSearch = view.findViewById<EditText>(R.id.editSearch)
        val rvSearch = view.findViewById<RecyclerView>(R.id.rvSearchResults)
       // val rvSearch = view.findViewById<RecyclerView>(R.id.rvSearchResults)
        adapter = MovieAdapter { movie ->
            findNavController().navigate(SearchFragmentDirections.actionSearchToDetails(movie.id))
        }
        rvSearch.layoutManager = LinearLayoutManager(requireContext())
        rvSearch.adapter = adapter
      // rvSearch.adapter = adapter

        editSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500) // debounce
                val query = text.toString()
                if (query.isNotEmpty()) {
                    viewModel.search(query, "54969c34fe6c23296273a590dafbc63a")
                }
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            Log.i(TAG, "Results: ${it.size}\")")
            adapter.submitList(it)
        }
    }
}
