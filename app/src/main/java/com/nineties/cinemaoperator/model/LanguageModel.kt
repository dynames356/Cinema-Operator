package com.nineties.cinemaoperator.model

import com.google.gson.annotations.SerializedName

data class LanguageModel(
    @SerializedName("english_name") val languageName: String,
    @SerializedName("name") val languageOriName: String,
)