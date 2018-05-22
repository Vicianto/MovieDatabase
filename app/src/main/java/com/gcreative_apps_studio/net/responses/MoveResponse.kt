package com.gcreative_apps_studio.net.responses

import com.gcreative_apps_studio.data.model.Movie
import com.google.gson.annotations.SerializedName

class MovieResponse(@SerializedName("Search") var results: List<Movie>,
                    @SerializedName("totalResults") var totalResults: Int,
                    @SerializedName("Response") var response: String)