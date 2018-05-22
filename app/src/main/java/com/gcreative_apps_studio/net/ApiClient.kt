package com.gcreative_apps_studio.net

import com.gcreative_apps_studio.data.model.Movie
import com.gcreative_apps_studio.net.responses.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class ApiClient(private var url: String) {
    private var retrofit: Retrofit? = null


    init {
        initialize()
    }


    // GET ALL DETAILS FROM MOVIE OBJECT
    fun getDetails(id: String, apiKey: String, success: (response: Response<Movie>?) -> Unit, failure: () -> Unit) {
        val apiService = retrofit?.create(ApiInterface::class.java)

        val call = apiService?.getMovieDetails(id, apiKey)
        call?.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                failure()
            }

            override fun onResponse(call: Call<Movie>?, response: Response<Movie>?) {
                success(response)
            }
        })
    }

    // SEARCH OMDB DATABASE
    fun search(title: String, page: String, apiKey: String, success: (response: Response<MovieResponse>?) -> Unit, failure: () -> Unit) {
        val apiService = retrofit?.create(ApiInterface::class.java)

        val call = apiService?.getMovieSearch(title, page, apiKey)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                failure()
            }

            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                success(response)
            }
        })
    }


    // INIT API
    private fun initialize() {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}

interface ApiInterface {
    @GET("/")
    fun getMovieDetails(@Query("i") id: String, @Query("apikey") apiKey: String): Call<Movie>

    @GET("/")
    fun getMovieSearch(@Query("s") title: String, @Query("page") page: String, @Query("apikey") apiKey: String): Call<MovieResponse>
}