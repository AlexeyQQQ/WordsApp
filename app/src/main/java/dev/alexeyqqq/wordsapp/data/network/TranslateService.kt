package dev.alexeyqqq.wordsapp.data.network

import dev.alexeyqqq.wordsapp.data.network.model.RootTranslateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateService {

    @GET("translate")
    suspend fun translate(
        @Query("dl") destinationLanguage: String = "ru",
        @Query("text") text: String,
    ): RootTranslateResponse
}