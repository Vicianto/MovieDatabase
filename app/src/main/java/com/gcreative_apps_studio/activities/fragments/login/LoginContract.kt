package com.gcreative_apps_studio.activities.fragments.login

import android.app.Fragment
import com.gcreative_apps_studio.activities.BasePresenter
import com.gcreative_apps_studio.activities.BaseView

interface LoginContract {

    interface Presenter : BasePresenter {

        fun getUserID(username: String): Long?

        fun loginUser(username: String, pass: String): Boolean
    }

    interface View : BaseView {

        fun initializeUI(view: android.view.View?)

        fun loadFragment(fragment: Fragment)

        fun loadSearchActivity(username: String)
    }
}