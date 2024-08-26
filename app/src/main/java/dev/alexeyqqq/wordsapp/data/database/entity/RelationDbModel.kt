package dev.alexeyqqq.wordsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "relation_table")
data class RelationDbModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("wordId")
    val wordId: Long,
    @ColumnInfo("dictionaryId")
    val dictionaryId: Long,
)