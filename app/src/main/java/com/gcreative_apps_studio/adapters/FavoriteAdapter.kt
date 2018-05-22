package com.gcreative_apps_studio.adapters

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.adapters.objects.FavoriteViewHolder
import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.itemtouch.FavoriteItemTouchAdapter
import com.squareup.picasso.Picasso

class FavoriteAdapter(movies: MutableList<Movie>?, loader: (id: String?) -> Unit, sHandler: (id: Long) -> Unit) : RecyclerView.Adapter<FavoriteViewHolder>(), FavoriteItemTouchAdapter {
    private var movies: MutableList<Movie>? = movies
    private var moreLoader = loader
    private var storageHandler = sHandler
    private var watcher: TextWatcher? = null

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun getItemViewType(position: Int) : Int {
        return position
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie: Movie? = movies?.get(position)

        if (!movie?.poster?.toLowerCase().equals(holder.poster?.tag.toString())) loadPoster(movie?.poster, holder.poster)
        holder.title?.text = String.format(holder.title?.tag.toString(), movie?.title, movie?.year)
        holder.id?.text = String.format(holder.id?.tag.toString(), movie?.imdbID)
        holder.type?.text = String.format(holder.type?.tag.toString(), movie?.type)
        holder.note?.setText(movie?.notes)

        holder.main?.setOnClickListener(null)
        holder.main?.setOnClickListener({ moreLoader.invoke(movie?.imdbID) })
        manageWatcher(holder, holder.note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_fav_adapter, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onItemDismiss(position: Int?) {
        val realPos = movies?.get(position!!)?.id!!

        storageHandler.invoke(realPos)
        movies?.removeAt(position!!)
        notifyItemRemoved(position!!)
    }

    override fun onItemMove(fromPosition: Int?, toPosition: Int?): Boolean {
        return false
    }


    private fun loadPoster(url: String?, view: ImageView?) {
        Picasso.get().load(url).into(view)
    }

    private fun manageWatcher(holder: FavoriteViewHolder, editor: EditText?) {
        editor?.removeTextChangedListener(watcher)

        watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                movies?.get(holder.adapterPosition)?.notes = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }

        editor?.addTextChangedListener(watcher)
    }

}