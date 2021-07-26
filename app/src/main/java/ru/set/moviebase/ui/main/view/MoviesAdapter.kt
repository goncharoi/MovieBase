package ru.set.moviebase.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.set.moviebase.databinding.MovieCardBinding
import ru.set.moviebase.ui.main.model.MovieEntity

class MoviesAdapter :
    ListAdapter<MovieEntity?, MoviesAdapter.MovieViewHolder>(MovieDiff()) {

    private lateinit var binding: MovieCardBinding
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun changeMovieFavoritesStatus(movieGUID: Int)
        fun setMovieRating(movieGUID: Int, rating: Float)
        fun chooseMovie(movieGUID: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        binding = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class MovieDiff : DiffUtil.ItemCallback<MovieEntity?>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            (oldItem.title == newItem.title
                    && oldItem.release_date == newItem.release_date
                    && oldItem.isFavorite == newItem.isFavorite
                    && oldItem.vote_average == newItem.vote_average)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = binding.movieCardTitle
        private val yearTv: TextView = binding.movieCardYear
        private val ratingBar: RatingBar = binding.movieCardRating
        private val image: ImageView = binding.movieCardImage

        private var movie: MovieEntity? = null

        fun bind(movie: MovieEntity) {
            this.movie = movie
            titleTv.text = movie.title
            yearTv.text = movie.release_date
            ratingBar.rating = movie.vote_average / 10
        }

        init {
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, fromUser ->
                    if (fromUser) onItemClickListener?.setMovieRating(movie!!.id, rating * 10)
                }
            image.setOnClickListener { movie?.let { it1 -> onItemClickListener?.chooseMovie(it1.id) } }
            titleTv.setOnClickListener { movie?.let { it1 -> onItemClickListener?.chooseMovie(it1.id) } }
        }
    }
}