package com.gcreative_apps_studio.activities.more

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.gcreative_apps_studio.activities.BaseActivity
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.application.AppClass
import com.gcreative_apps_studio.data.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_more.*
import kotlinx.android.synthetic.main.toast.*
import retrofit2.Response
import java.util.*

class MoreActivity : BaseActivity(), MoreContract.View {
    // VAR
    private var id: String? = null
    private lateinit var presenter: MoreContract.Presenter

    // FUN
    override fun initializeUI() {
        loadId()

        if (!id.isNullOrEmpty()) {
            presenter.getDetails(id!!, getString(R.string.app_omdb_key), ::onSuccess, ::onFailure)
        }
    }

    override fun createPresenter() {
        presenter = MorePresenter(this, (application as AppClass).boxStore, this, this.getString(R.string.app_name),
                getString(R.string.app_omdb_url))
    }

    override fun loadId() {
        val extras = intent.extras

        if (extras != null) {
            id = extras[getString(R.string.app_pass_more_key)].toString()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        try {
            createPresenter()
            initializeUI()
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun restoreState() {

    }

    override fun saveState() {

    }



    private fun loadPoster(url: String?, view: ImageView?) {
        Picasso.get().load(url).into(view)
    }

    private fun onFailure() {
        displayToast(getString(R.string.error_no_internet))
        finish()
    }

    private fun onSuccess(response: Response<Movie>?) {
        val movie = response?.body()

        more_movie_title.text = movie?.title
        more_movie_misc.text = String.format(more_movie_misc.text.toString(), movie?.year, movie?.runtime, movie?.genre)
        if (!movie?.poster?.toLowerCase().equals(more_movie_poster.tag.toString())) loadPoster(movie?.poster, more_movie_poster)
        more_movie_plot.text = movie?.plot
        more_movie_ratings.text = String.format(more_movie_ratings.text.toString(), movie?.imdbRating, movie?.metascore)
        more_movie_actors.text = movie?.actors
        more_movie_director.text = movie?.director
        more_movie_writer.text = movie?.writer

        more_loading_layout.visibility = GONE
    }
}