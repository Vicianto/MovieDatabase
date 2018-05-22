package com.gcreative_apps_studio.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.adapters.objects.MovieViewHolder
import com.gcreative_apps_studio.data.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(movies: List<Movie>?, private val favorites: List<Movie>?, loader: (id: String?) -> Unit, manager: (movie: Movie?) -> Unit) : RecyclerView.Adapter<MovieViewHolder>() {
    private var movies: List<Movie>? = movies
    private var moreLoader = loader
    private var favManager = manager

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }


    override fun getItemViewType(position: Int) : Int {
        return position
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie? = movies?.get(position)

        if (!movie?.poster?.toLowerCase().equals(holder.poster?.tag.toString())) loadPoster(movie?.poster, holder.poster)
        holder.title?.text = String.format(holder.title?.text.toString(), movie?.title, movie?.year)
        holder.id?.text = String.format(holder.id?.text.toString(), movie?.imdbID)
        holder.type?.text = String.format(holder.type?.text.toString(), movie?.type)

        holder.main?.setOnClickListener(null)
        holder.main?.setOnClickListener({ moreLoader.invoke(movie?.imdbID) })
        controlFav(movie, holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_search_adapter, parent, false)
        return MovieViewHolder(itemView)
    }



    private fun loadPoster(url: String?, view: ImageView?) {
        Picasso.get().load(url).into(view)
    }

    private fun controlFav(current: Movie?, holder: MovieViewHolder) {
        holder.fav?.setOnClickListener(null)
        holder.fav?.setImageResource(R.drawable.ic_action_star_off)

        if (favorites?.find { m -> m.imdbID.equals(current?.imdbID) } != null) holder.fav?.setImageResource(R.drawable.ic_action_star_on)
        else {
            holder.fav?.setOnClickListener({
                holder.fav.setImageResource(R.drawable.ic_action_star_on)
                favManager.invoke(current)
                notifyItemChanged(holder.adapterPosition)
            })
        }
    }
}