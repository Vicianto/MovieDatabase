package com.gcreative_apps_studio.activities.fragments.register

import android.app.Activity
import com.gcreative_apps_studio.data.AppDataHandler
import com.gcreative_apps_studio.data.model.User
import com.gcreative_apps_studio.util.MD5HashGenerator
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlin.properties.Delegates

class RegisterPresenter(v: RegisterContract.View, b: BoxStore, activity: Activity, appName: String) : RegisterContract.Presenter {
    private var view: RegisterContract.View by Delegates.notNull()
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
    override fun checkEmail(email: String): Boolean {
        return storage.checkEmail(email)
    }

    @Throws(Exception::class)
    override fun checkUsername(username: String): Boolean {
        return storage.checkUsername(username)
    }

    override fun createUser(username: String, password: String, email: String, security: String, security_answer: String): User {
        return User(username = username, password = password, email = email, security_question = security, security_answer = security_answer)
    }

    @Throws(Exception::class)
    override fun loadState(key: String): String? {
        return storage.getString(key)
    }

    @Throws(Exception::class)
    override fun registerUser(user: User): Boolean {
        // Generate MD5 hash for password.
        user.password = MD5HashGenerator.generate(user.password)

        // Store
        return storage.putUser(user)
    }

    @Throws(Exception::class)
    override fun saveState(key: String, value: String) {
        storage.putString(key, value)
    }

}