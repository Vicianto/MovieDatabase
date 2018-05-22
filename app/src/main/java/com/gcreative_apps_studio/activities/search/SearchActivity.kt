package com.gcreative_apps_studio.activities.search

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View.*
import android.widget.TextView
import android.widget.Toast
import com.gcreative_apps_studio.activities.BaseActivity
import com.gcreative_apps_studio.activities.R
import com.gcreative_apps_studio.activities.fragments.search.SearchFragment
import com.gcreative_apps_studio.activities.more.MoreActivity
import com.gcreative_apps_studio.adapters.FavoriteAdapter
import com.gcreative_apps_studio.adapters.MovieAdapter
import com.gcreative_apps_studio.application.AppClass
import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.itemtouch.FavoriteItemTouchHelperCallback
import com.gcreative_apps_studio.itemtouch.FavoriteStartDragListener
import com.gcreative_apps_studio.net.responses.MovieResponse
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toast.*
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : BaseActivity(), SearchActivityContract.View, FavoriteStartDragListener {
    private enum class ActivityMode {
        Search,
        Favorite
    }

    // VAR
    private val handler = Handler()
    private val RCSI = 100
    private var favorites: MutableList<Movie> = ArrayList()
    private var mode = ActivityMode.Search
    private var searchFragment = SearchFragment()
    private var isDialogActive: Boolean = false
    private var inSearch = false
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var userID: Long = -1
    private lateinit var presenter: SearchActivityContract.Presenter


    // FUN
    override fun addToFavorites(movie: Movie?) {
        if (favorites.find { m -> m.imdbID.equals(movie?.imdbID) } == null) {
            movie?.userID = userID; favorites.add(movie!!)
        }
    }

    override fun createPresenter() {
        presenter = SearchActivityPresenter(this, (application as AppClass).boxStore, this, this.getString(R.string.app_name),
                getString(R.string.app_omdb_url))
    }

    override fun initializeUI() {
        loadId()

        search_find_micro_button.setOnClickListener({
            if (mode == ActivityMode.Search) {
                if (!inSearch) enableSearch() else displaySpeechInput()
            } else {
                prepareSearchView()
            }
        })

        search_back_button.setOnClickListener({
            if (mode == ActivityMode.Search) disableSearch() else prepareSearchView()
        })

        search_text_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onSearchTextChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        search_fav_button.setOnClickListener({
            disableSearch()
            prepareFavoriteView()
        })
    }

    override fun disableSearch() {
        inSearch = false
        search_back_button.visibility = INVISIBLE
        search_text_field.visibility = INVISIBLE
        search_find_micro_button.setImageResource(R.drawable.ic_action_search)
    }

    override fun displayMore(id: String?) {
        val intent = Intent(this, MoreActivity::class.java)
        intent.putExtra(getString(R.string.app_pass_more_key), id)
        startActivity(intent)
    }

    override fun displaySearch() {
        fragmentManager.beginTransaction().add(R.id.search_fragment_container, searchFragment).commit()
    }

    override fun displaySpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.general_speech_to_text))

        try {
            startActivityForResult(intent, RCSI)
        } catch (a: ActivityNotFoundException) {
            displayToast(getString(R.string.error_speech_to_text_not_supported))
        }

    }

    override fun enableSearch() {
        inSearch = true
        search_back_button.visibility = VISIBLE
        search_text_field.visibility = VISIBLE
        search_find_micro_button.setImageResource(R.drawable.ic_action_micro)
    }

    override fun loadAdapter(movies: List<Movie>?) {
        val adapter = MovieAdapter(movies, favorites, ::displayMore, ::addToFavorites)

        search_fragment_view.setHasFixedSize(false)
        search_fragment_view.layoutManager = LinearLayoutManager(this)
        search_fragment_view.adapter = adapter
        search_fragment_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun loadId() {
        val extras = intent.extras

        if (extras != null) {
            userID = extras[getString(R.string.app_pass_more_key)].toString().toLong()

        }
    }

    override fun loadFavorites() {
        favorites = presenter.getFavorites(userID)?.toMutableList()!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RCSI -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    search_text_field.setText(result[0])
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializeUI()

        try {
            // Init presenter.
            createPresenter()

            // Display search.
            displaySearch()
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun onPause() {
        super.onPause()

        try {
            saveFavorites()
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun onResume() {
        super.onResume()

        try {
            loadFavorites()
            startSearch()
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder)
    }

    override fun prepareFavoriteView() {
        search_back_button.visibility = VISIBLE
        mode = ActivityMode.Favorite
        reloadAdapter(favorites)
    }

    override fun prepareSearchView() {
        search_back_button.visibility = GONE
        mode = ActivityMode.Search
        search(getString(R.string.app_omdb_home))
    }

    override fun reloadAdapter(movies: List<Movie>?) {
        val adapter = if (mode == ActivityMode.Search) MovieAdapter(movies, favorites, ::displayMore, ::addToFavorites) else FavoriteAdapter(favorites, ::displayMore, ::removeFavorite)
        search_fragment_view.adapter = adapter

        mItemTouchHelper?.attachToRecyclerView(null)

        if (mode == ActivityMode.Favorite) {
            // ItemTouch
            val callback = FavoriteItemTouchHelperCallback(search_fragment_view.adapter as FavoriteAdapter)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper?.attachToRecyclerView(search_fragment_view)
        }
    }

    override fun restoreState() {

    }

    override fun removeFavorite(id: Long) {
        try {
            presenter.removeFavorite(id)
        } catch (ex: Exception) {
            displayError(ex)
        }
    }

    override fun saveState() {

    }

    override fun saveFavorites() {
        presenter.putFavorites(favorites)
    }

    override fun search(title: String) {
        presenter.performSearch(title, getString(R.string.app_omdb_home_page), getString(R.string.app_omdb_key), ::onSuccess, ::onFailure)
    }

    override fun startSearch() {
        presenter.performSearch(getString(R.string.app_omdb_home), getString(R.string.app_omdb_home_page), getString(R.string.app_omdb_key), ::onSuccessStart, ::onFailureStart)
    }


    private fun onFailure() {
        displayToast(getString(R.string.error_no_internet))
    }

    private fun onFailureStart() {
        displayToast(getString(R.string.error_no_internet))
    }

    private fun onSearchTextChanged() {
        handler.removeMessages(0)

        if (search_text_field.text.isNotEmpty()) {
            if (search_text_field.text.length > 1) {
                handler.postDelayed({
                    search_fragment_loading_layout.visibility = VISIBLE
                    try {
                        search(search_text_field.text.toString())
                    } catch (ex: Exception) {
                        displayError(ex)
                    }
                }, resources.getInteger(R.integer.general_search_delay).toLong())
            } else {
                handler.postDelayed({
                    displayToast(getString(R.string.error_at_least_two_character))
                }, resources.getInteger(R.integer.general_search_warning_delay).toLong())
            }
        }
    }

    private fun onSuccess(response: Response<MovieResponse>?) {
        reloadAdapter(response?.body()?.results)
        search_fragment_loading_layout.visibility = GONE

    }

    private fun onSuccessStart(response: Response<MovieResponse>?) {
        loadAdapter(response?.body()?.results)
        search_fragment_loading_layout.visibility = GONE

    }

}