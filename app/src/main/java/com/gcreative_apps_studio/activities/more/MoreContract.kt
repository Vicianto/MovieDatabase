package com.gcreative_apps_studio.activities.more

import com.gcreative_apps_studio.activities.BasePresenter
import com.gcreative_apps_studio.activities.BaseView
import com.gcreative_apps_studio.data.model.Movie
import retrofit2.Response

interface MoreContract {

    interface Presenter : BasePresenter {

        fun getDetails(id: String, apiKey: String, success: (response: Response<Movie>?) -> Unit, failure: () -> Unit)
    }


    interface View : BaseView {

        fun initializeUI()

        fun displayDialog(title: String, message: String)

        fun displayError(ex: Exception)

        fun displayToast(message: String)

        fun loadId()
    }
}