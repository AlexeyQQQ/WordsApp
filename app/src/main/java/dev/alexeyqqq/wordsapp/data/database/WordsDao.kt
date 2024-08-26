package dev.alexeyqqq.wordsapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordsDao {

    @Query("SELECT * FROM words_table ORDER BY original")
    fun getAllWords(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words_table WHERE id = :wordId")
    suspend fun getWord(wordId: Long): WordDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordDbModel)

    @Update
    suspend fun updateWord(word: WordDbModel)

    @Query("DELETE FROM words_table WHERE id = :wordId")
    suspend fun deleteWord(wordId: Long)
}