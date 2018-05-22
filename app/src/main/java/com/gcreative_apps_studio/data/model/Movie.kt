package com.gcreative_apps_studio.data.model

import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Movie(
        @Id                                  var id:                  Long = 0,
        @SerializedName("Title")        var title:              String?,
        @SerializedName("Year")         var year:               String?,
        @SerializedName("Rated")        var rated:              String?,
        @SerializedName("Released")     var released:           String?,
        @SerializedName("Runtime")      var runtime:            String?,
        @SerializedName("Genre")        var genre:              String?,
        @SerializedName("Director")     var director:           String?,
        @SerializedName("Writer")       var writer:             String?,
        @SerializedName("Actors")       var actors:             String?,
        @SerializedName("Plot")         var plot:               String?,
        @SerializedName("Language")     var language:           String?,
        @SerializedName("Poster")       var poster:             String?,
        @SerializedName("Metascore")    var metascore:          String?,
        @SerializedName("imdbRating")   var imdbRating:         String?,
        @SerializedName("imdbID")       var imdbID:             String?,
        @SerializedName("Type")         var type:               String?,
                                              var notes:             String?,
                                              var userID:             Long = 0
)