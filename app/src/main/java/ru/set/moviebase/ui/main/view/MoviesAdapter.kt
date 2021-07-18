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
        fun changeMovieFavoritesStatus(movieGUID: String)
        fun setMovieRating(movieGUID: String, rating: Float)
        fun chooseMovie(movieGUID: String)
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
            oldItem.GUID == newItem.GUID

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            (oldItem.title == newItem.title
                    && oldItem.year == newItem.year
                    && oldItem.isFavorite == newItem.isFavorite
                    && oldItem.rating == newItem.rating)
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
            yearTv.text = movie.year.toString()
            ratingBar.rating = movie.rating
        }

        init {
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, fromUser ->
                    if (fromUser) onItemClickListener?.setMovieRating(movie!!.GUID, rating)
                }
            image.setOnClickListener { movie?.let { it1 -> onItemClickListener?.chooseMovie(it1.GUID) } }
            titleTv.setOnClickListener { movie?.let { it1 -> onItemClickListener?.chooseMovie(it1.GUID) } }
        }
    }
}