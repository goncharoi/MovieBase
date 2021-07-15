package ru.set.moviebase.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.set.moviebase.databinding.NowPlayingFragmentBinding
import ru.set.moviebase.ui.main.model.Movies
import ru.set.moviebase.ui.main.viewmodel.ViewModel


class MoviesFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private var binding: NowPlayingFragmentBinding? = null
    private lateinit var moviesAdapter: MoviesAdapter

    companion object {
        fun newInstance() = MoviesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NowPlayingFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
        setupRecyclerView(view)
        //viewModel.loadData()
        viewModel.loadDataLocal()
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView = binding!!.nowPlayingRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        setupAdapter()
        recyclerView.adapter = moviesAdapter
    }

    private fun setupAdapter() {
        moviesAdapter = MoviesAdapter()
        moviesAdapter.setOnItemClickListener(viewModel)
    }

    private fun renderData(movies: Movies) {
        moviesAdapter.submitList(movies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}