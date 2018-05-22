package com.gcreative_apps_studio.activities.fragments.register

import com.gcreative_apps_studio.activities.BasePresenter
import com.gcreative_apps_studio.activities.BaseView
import com.gcreative_apps_studio.data.model.User

interface RegisterContract {

    interface Presenter : BasePresenter {

        fun checkEmail(email: String) : Boolean

        fun checkUsername(username: String) : Boolean

        fun createUser(username: String, password: String, email: String, security: String, security_answer: String): User

        fun registerUser(user: User) : Boolean
    }

    interface View : BaseView {

        fun initializeUI(view: android.view.View?)

        fun controlUserField()

        fun controlPassAgainField()

        fun controlEmailField()

        fun hideLogo()

        fun showLogo()
    }

}