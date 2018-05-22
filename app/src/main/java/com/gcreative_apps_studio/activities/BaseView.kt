package com.gcreative_apps_studio.activities


interface BaseView {

    fun createPresenter()

    fun restoreState()

    fun saveState()
}