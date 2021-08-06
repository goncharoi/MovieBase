package ru.set.moviebase.ui.main.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.set.moviebase.databinding.MainFragmentBinding
import ru.set.moviebase.ui.main.model.DATA_TYPE_EXTRA
import ru.set.moviebase.ui.main.model.DETAILS_INTENT_FILTER
import ru.set.moviebase.ui.main.model.TheMovieDBService
import ru.set.moviebase.ui.main.viewmodel.NOW_PLAYING_REQUEST_TYPE
import ru.set.moviebase.ui.main.viewmodel.UPCOMING_REQUEST_TYPE
import ru.set.moviebase.ui.main.viewmodel.ViewModel

class MoviesFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private var binding: MainFragmentBinding? = null
    private lateinit var nowPlayingAdapter: MoviesAdapter
    private lateinit var upcomingAdapter: MoviesAdapter

    companion object {
        fun newInstance() = MoviesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        viewModel.registerReceiver(context)
        viewModel.getNowPlayingMovies()
            .observe(viewLifecycleOwner, { nowPlayingAdapter.submitList(it) })
        viewModel.getUpcomingMovies()
            .observe(viewLifecycleOwner, { upcomingAdapter.submitList(it) })
        setupView()
        viewModel.loadDataWithService(context)
//        viewModel.loadData()
    }

    private fun setupView() {
        setupNowPlayingAdapter()
        setupMoviesRecyclerView(binding!!.nowPlayingRecyclerView, nowPlayingAdapter)
        setupUpcomingAdapter()
        setupMoviesRecyclerView(binding!!.upcomingRecyclerView, upcomingAdapter)
    }

    private fun setupMoviesRecyclerView(recyclerView: RecyclerView, adapter: MoviesAdapter) {
        with(recyclerView) {
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun setupNowPlayingAdapter() {
        nowPlayingAdapter = MoviesAdapter().apply { setOnItemClickListener(viewModel) }
    }

    private fun setupUpcomingAdapter() {
        upcomingAdapter = MoviesAdapter().apply { setOnItemClickListener(viewModel) }
    }

    override fun onDestroyView() {
        viewModel.unregisterReceiver(context)
        binding = null
        super.onDestroyView()
    }
}