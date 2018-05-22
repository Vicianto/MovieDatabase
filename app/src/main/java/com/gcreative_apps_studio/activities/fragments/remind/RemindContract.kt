package com.gcreative_apps_studio.activities.fragments.remind

import com.gcreative_apps_studio.activities.BasePresenter
import com.gcreative_apps_studio.activities.BaseView

interface RemindContract {

    interface Presenter : BasePresenter {

        fun getSecurity(username: String): String?

        fun resetPass(username: String, sec: String, answer: String, pass: String): Boolean
    }

    interface View : BaseView {

        fun initializeUI(view: android.view.View?)
    }
}