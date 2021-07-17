package ru.set.moviebase.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.set.moviebase.databinding.MovieFragmentBinding
import ru.set.moviebase.ui.main.model.MovieEntity
import ru.set.moviebase.ui.main.viewmodel.ViewModel

class ChoosenMovieFragment : Fragment()  {
    private lateinit var viewModel: ViewModel
    private var binding: MovieFragmentBinding? = null

    companion object {
        fun newInstance() = ChoosenMovieFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        viewModel.getChosenMovie().value?.let {setupView(it)}
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unchooseMovie()
    }
    private fun setupView(movie: MovieEntity) {
        binding!!.movieTitle.text = movie.title
        binding!!.movieOriginTitle.text = movie.originTitle
        binding!!.movieDescription.text = movie.description
        binding!!.movieYear.text = movie.year.toString()
        binding!!.movieRating.rating = movie.rating
    }
}
