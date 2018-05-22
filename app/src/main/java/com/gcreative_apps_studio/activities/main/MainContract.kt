package com.gcreative_apps_studio.activities.main


import android.app.Fragment
import com.gcreative_apps_studio.activities.BaseView

interface MainContract {

    interface View : BaseView {

        fun displayLogin()

        fun displayDialog(title: String, message: String)

        fun displayError(ex: Exception)

        fun displayToast(message: String)

        fun hideKeyboard()

        fun reloadFragment(fragment: Fragment)

        fun showKeyboard()
    }

}