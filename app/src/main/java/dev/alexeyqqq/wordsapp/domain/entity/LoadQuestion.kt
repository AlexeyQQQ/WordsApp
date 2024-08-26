package dev.alexeyqqq.wordsapp.domain.entity

interface LoadQuestion {

    fun isSuccessful(): Boolean

    fun question(): Question

    data class Success(private val question: Question) : LoadQuestion {

        override fun isSuccessful(): Boolean = true

        override fun question(): Question = question
    }

    object Error : LoadQuestion {

        override fun isSuccessful(): Boolean = false

        override fun question(): Question = throw IllegalStateException("Question cannot exist")
    }
}