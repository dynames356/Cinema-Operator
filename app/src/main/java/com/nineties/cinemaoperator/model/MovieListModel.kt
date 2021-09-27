package com.nineties.cinemaoperator.model

import com.google.gson.annotations.SerializedName

data class MovieListModel(
    @SerializedName("page") val listPage: Int = 0,
    @SerializedName("results") val movieList: List<MovieOverviewModel> = emptyList(),
)
