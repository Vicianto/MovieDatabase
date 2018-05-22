package com.gcreative_apps_studio.activities.more

import android.app.Activity
import com.gcreative_apps_studio.data.AppDataHandler
import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.data.model.User
import com.gcreative_apps_studio.net.ApiClient
import io.objectbox.Box
import io.objectbox.BoxStore
import retrofit2.Response
import kotlin.properties.Delegates

class MorePresenter(v: MoreContract.View, b: BoxStore, activity: Activity, appName: String, url: String) : MoreContract.Presenter {
    private var view: MoreContract.View by Delegates.notNull()
    private var db: BoxStore by Delegates.notNull()
    private var personStorage: Box<User> by Delegates.notNull()
    private var storage: AppDataHandler by Delegates.notNull()
    private val client = ApiClient(url)


    init {
        view = v
        db = b
        personStorage = db.boxFor(User::class.java)
        storage = AppDataHandler(personStorage, activity, appName)
    }


    @Throws(Exception::class)
    override fun getDetails(id: String, apiKey: String, success: (response: Response<Movie>?) -> Unit, failure: () -> Unit) {
        client.getDetails(id, apiKey, success, failure)
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