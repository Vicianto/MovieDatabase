package com.gcreative_apps_studio.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.data.model.Movie_
import com.gcreative_apps_studio.data.model.User
import com.gcreative_apps_studio.data.model.User_
import io.objectbox.Box


class AppDataHandler(box: Box<User>, activity: Activity, appName: String) {
    private val userBox = box
    private val lPref: SharedPreferences? = activity.getSharedPreferences(appName, Context.MODE_PRIVATE)
    private lateinit var favBox: Box<Movie>

    constructor(box: Box<User>, favBox: Box<Movie>, activity: Activity, appName: String) : this(box, activity, appName) {
        this.favBox = favBox
    }

    fun checkEmail(email: String): Boolean {
        val builder = userBox.query()

        builder.equal(User_.email, email)
        val result = builder.build().find()

        return result.size == 0
    }

    fun checkUsername(username: String): Boolean {
        val builder = userBox.query()

        builder.equal(User_.username, username)
        val result = builder.build().find()

        return result.size == 0
    }

    fun getFavorites(userID: Long): List<Movie>? {
        val builder = favBox.query()

        // Build query and perform.
        builder.equal(Movie_.userID, userID)
        return builder.build().find()
    }

    fun getUserID(username: String): Long? {
        val builder = userBox.query()

        builder.equal(User_.username, username)
        val result = builder.build().find()

        return result[result.size - 1].id
    }

    fun getString(key: String): String? {
        return if (lPref?.contains(key)!!) {
            lPref.getString(key, "")
        } else {
            ""
        }
    }

    fun getSecurity(username: String): String? {
        val builder = userBox.query()

        // Build query and perform.
        builder.equal(User_.username, username)
        val result = builder.build().find()

        // Get answer
        return if (result.size > 0) result[result.size - 1].security_answer else ""
    }

    fun logUser(username: String, pass: String): Boolean {
        val builder = userBox.query()

        // Build query and perform.
        builder.equal(User_.username, username).and().equal(User_.password, pass)
        val result = builder.build().find()

        return result.size != 0
    }

    fun putUser(user: User): Boolean {
        return if (checkMultiplies(user.username, user.email)) {
            userBox.put(user); false
        } else true
    }

    fun putFavorites(movies: List<Movie>) {
        favBox.put(movies)
    }

    fun putString(key: String, value: String) {
        val editor = lPref?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun resetPass(username: String, sec: String, answer: String, pass: String): Boolean {
        return if (checkSecurity(username, sec, answer)) {
            resetPass(username, pass)
            true
        } else false
    }

    fun removeFavorite(id: Long) {
        val builder = favBox.query()

        // Build query and perform.
        builder.equal(Movie_.id, id)
        val result = builder.build().find()

        if (result.size != 0) {
            favBox.remove(id)
        }
    }

    private fun checkMultiplies(username: String, email: String): Boolean {
        val builder = userBox.query()

        builder.equal(User_.username, username).or().equal(User_.email, email)
        val result = builder.build().find()

        return result.size == 0
    }

    private fun checkSecurity(username: String, sec: String, answer: String): Boolean {
        val builder = userBox.query()

        builder.equal(User_.username, username).and().equal(User_.security_question, sec).and().equal(User_.security_answer, answer)

        val result = builder.build().find()

        return result.size != 0
    }

    private fun resetPass(username: String, pass: String) {
        val builder = userBox.query()

        // Build query and perform.
        builder.equal(User_.username, username)
        val result = builder.build().find()

        // Update.
        if (result.size > 0) {
            result[result.size - 1].password = pass
            userBox.put(result[result.size - 1])
        }
    }
}