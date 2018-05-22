package com.gcreative_apps_studio.activities.search

import com.gcreative_apps_studio.activities.BasePresenter
import com.gcreative_apps_studio.activities.BaseView
import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.net.responses.MovieResponse
import retrofit2.Response

interface SearchActivityContract {

    interface Presenter : BasePresenter {

        fun getFavorites(userID: Long): List<Movie>?

        fun performSearch(title:String, page: String, apiKey: String, success: (response: Response<MovieResponse>?) -> Unit, failure: () -> Unit)

        fun putFavorites(movies: List<Movie>?)

        fun removeFavorite(id: Long)
    }

    interface View : BaseView {

        fun addToFavorites(movie: Movie?)

        fun initializeUI()

        fun disableSearch()

        fun displaySearch()

        fun displaySpeechInput()

        fun displayDialog(title: String, message: String)

        fun displayError(ex: Exception)

        fun displayMore(id: String?)

        fun displayToast(message: String)

        fun enableSearch()

        fun loadAdapter(movies: List<Movie>?)

        fun loadId()

        fun loadFavorites()

        fun prepareFavoriteView()

        fun prepareSearchView()

        fun reloadAdapter(movies: List<Movie>?)

        fun removeFavorite(id: Long)

        fun saveFavorites()

        fun search(title: String)

        fun startSearch()

    }

}