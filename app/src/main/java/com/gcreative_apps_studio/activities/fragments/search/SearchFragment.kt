package com.gcreative_apps_studio.activities.fragments.search

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcreative_apps_studio.activities.R

class SearchFragment : Fragment() {


    fun initializeUI(view: View?) {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_search, container, false)

        // Init.
        initializeUI(view)

        return view
    }
}