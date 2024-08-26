package dev.alexeyqqq.wordsapp.data.network.model

import com.google.gson.annotations.SerializedName

data class Pronunciation(

    @SerializedName("source-text-audio")
    val sourceTextAudio: String,

    @SerializedName("destination-text-audio")
    val destinationTextAudio: String,
)