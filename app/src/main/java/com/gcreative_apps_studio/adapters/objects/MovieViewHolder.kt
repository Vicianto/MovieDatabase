package com.gcreative_apps_studio.adapters.objects

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gcreative_apps_studio.activities.R

class MovieViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val main = itemView?.findViewById<LinearLayout>(R.id.search_main_layout)
    val fav = itemView?.findViewById<ImageView>(R.id.search_fragment_fav_button)
    val poster = itemView?.findViewById<ImageView>(R.id.search_fragment_movie_poster)
    val title = itemView?.findViewById<TextView>(R.id.search_fragment_movie_name_year)
    val id = itemView?.findViewById<TextView>(R.id.search_fragment_movie_id)
    val type = itemView?.findViewById<TextView>(R.id.search_fragment_movie_type)
}