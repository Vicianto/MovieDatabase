package com.gcreative_apps_studio.adapters.objects

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.itemtouch.FavoriteItemTouchViewHolder

class FavoriteViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), FavoriteItemTouchViewHolder {
    val main = itemView?.findViewById<LinearLayout>(R.id.fav_main_layout)
    val poster = itemView?.findViewById<ImageView>(R.id.fav_fragment_movie_poster)
    val title = itemView?.findViewById<TextView>(R.id.fav_fragment_movie_name_year)
    val id = itemView?.findViewById<TextView>(R.id.fav_fragment_movie_id)
    val type = itemView?.findViewById<TextView>(R.id.fav_fragment_movie_type)
    val note = itemView?.findViewById<EditText>(R.id.fav_fragment_movie_hint)

    override fun onItemClear() {
        itemView.setBackgroundColor(Color.WHITE)
    }

    override fun onItemSelected() {
        itemView.setBackgroundColor(Color.WHITE)
    }
}