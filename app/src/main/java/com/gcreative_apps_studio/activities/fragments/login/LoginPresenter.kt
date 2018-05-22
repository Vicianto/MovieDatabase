package com.gcreative_apps_studio.activities.fragments.login

import android.app.Activity
import com.gcreative_apps_studio.data.AppDataHandler
import com.gcreative_apps_studio.data.model.User
import com.gcreative_apps_studio.util.MD5HashGenerator
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlin.properties.Delegates

class LoginPresenter(v: LoginContract.View, b: BoxStore, activity: Activity, appName: String) : LoginContract.Presenter {
    private var view: LoginContract.View by Delegates.notNull()
    private var db: BoxStore by Delegates.notNull()
    private var personStorage: Box<User> by Delegates.notNull()
    private var storage: AppDataHandler by Delegates.notNull()

    init {
        view = v
        db = b
        personStorage = db.boxFor(User::class.java)
        storage = AppDataHandler(personStorage, activity, appName)
    }

    @Throws(Exception::class)
    override fun getUserID(username: String): Long? {
        return storage.getUserID(username)
    }

    @Throws(Exception::class)
    override fun loginUser(username: String, pass: String) : Boolean {
        val md5Hash = MD5HashGenerator.generate(pass)
        return storage.logUser(username, md5Hash)
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