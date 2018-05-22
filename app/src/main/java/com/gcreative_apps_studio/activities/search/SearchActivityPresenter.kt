package com.gcreative_apps_studio.activities.search

import android.app.Activity
import android.util.Log
import com.gcreative_apps_studio.data.AppDataHandler
import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.data.model.User
import com.gcreative_apps_studio.net.ApiClient
import com.gcreative_apps_studio.net.responses.MovieResponse
import io.objectbox.Box
import io.objectbox.BoxStore
import retrofit2.Response
import kotlin.properties.Delegates

class SearchActivityPresenter(v: SearchActivityContract.View, b: BoxStore, activity: Activity, appName: String, url: String) : SearchActivityContract.Presenter {
    private var view: SearchActivityContract.View by Delegates.notNull()
    private var db: BoxStore by Delegates.notNull()
    private var personStorage: Box<User> by Delegates.notNull()
    private var favStorage: Box<Movie> by Delegates.notNull()
    private var storage: AppDataHandler by Delegates.notNull()
    private val client = ApiClient(url)

    init {
        view = v
        db = b
        personStorage = db.boxFor(User::class.java)
        favStorage = db.boxFor(Movie::class.java)
        storage = AppDataHandler(personStorage, favStorage, activity, appName)
    }


    @Throws(Exception::class)
    override fun getFavorites(userID: Long): List<Movie>? {
        return storage.getFavorites(userID)
    }

    @Throws(Exception::class)
    override fun performSearch(title: String, page: String, apiKey: String, success: (response: Response<MovieResponse>?) -> Unit, failure: () -> Unit) {
        client.search(title, page, apiKey, success, failure)
    }

    @Throws(Exception::class)
    override fun putFavorites(movies: List<Movie>?) {
        storage.putFavorites(movies!!)
    }

    @Throws(Exception::class)
    override fun removeFavorite(id: Long) {
        storage.removeFavorite(id)
    }

    @Throws(Exception::class)
    override fun loadState(key: String): String? {
        return storage.getString(key)
    }

    @Throws(Exception::class)
    override fun saveState(key: String, value: String) {
        storage.putString(key, value)
    }

}