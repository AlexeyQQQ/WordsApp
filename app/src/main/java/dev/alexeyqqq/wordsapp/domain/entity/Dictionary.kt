package dev.alexeyqqq.wordsapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dictionary(
    val id: Long = 0,
    val name: String,
) : Parcelable