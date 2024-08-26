package dev.alexeyqqq.wordsapp.data.network.model

import com.google.gson.annotations.SerializedName

data class RootTranslateResponse(

    @SerializedName("source-language")
    val sourceLanguage: String,

    @SerializedName("source-text")
    val sourceText: String,

    @SerializedName("destination-language")
    val destinationLanguage: String,

    @SerializedName("destination-text")
    val destinationText: String,

    @SerializedName("pronunciation")
    val pronunciation: Pronunciation,
)