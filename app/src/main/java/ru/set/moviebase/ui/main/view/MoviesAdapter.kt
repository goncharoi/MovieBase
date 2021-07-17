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
import ru.set.moviebase.R
import ru.set.moviebase.ui.main.model.MovieEntity

class MoviesAdapter :
    ListAdapter<MovieEntity?, MoviesAdapter.MovieViewHolder>(MovieDiff()) {

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
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class MovieDiff : DiffUtil.ItemCallback<MovieEntity?>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.GUID == newItem.GUID
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return (oldItem.title == newItem.title
                    && oldItem.year == newItem.year
                    && oldItem.isFavorite == newItem.isFavorite
                    && oldItem.rating == newItem.rating)
        }
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.movie_card_title)
        private val yearTv: TextView = itemView.findViewById(R.id.movie_card_year)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.movie_card_rating)
        private val image: ImageView = itemView.findViewById(R.id.movie_card_image)

        private var movie: MovieEntity? = null

        fun bind(movie: MovieEntity) {
            this.movie = movie
            titleTv.text = movie.title
            yearTv.text = movie.year.toString()
            ratingBar.rating = movie.rating
        }

        init {
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    onItemClickListener?.setMovieRating(movie!!.GUID, rating)
                }
            image.setOnClickListener { movie?.let { it1 -> onItemClickListener?.chooseMovie(it1.GUID) } }
            titleTv.setOnClickListener { movie?.let { it1 -> onItemClickListener?.chooseMovie(it1.GUID) } }
        }
    }
}