package dev.alexeyqqq.wordsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
class WordDbModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("original")
    val original: String,
    @ColumnInfo("translation")
    val translation: String,
    @ColumnInfo("correctAnswersCount")
    var correctAnswersCount: Int = BASIC_VALUE,
) {

    companion object {
        private const val BASIC_VALUE = 0
    }
}