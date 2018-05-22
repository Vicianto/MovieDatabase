package com.gcreative_apps_studio.activities

interface BasePresenter {

    fun loadState(key: String): String?

    fun saveState(key: String, value: String)
}