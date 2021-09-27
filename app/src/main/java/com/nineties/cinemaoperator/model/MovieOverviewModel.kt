package com.nineties.cinemaoperator.model

import com.google.gson.annotations.SerializedName

data class MovieOverviewModel(
    @SerializedName("id") val movieID: Int = 0,
    @SerializedName("original_title") val movieTitle: String = "",
    @SerializedName("popularity") val moviePopularity: String = "0",
    @SerializedName("vote_average") val movieRating: Float = 0.0f,
    @SerializedName("poster_path") var moviePoster: String = "",
)