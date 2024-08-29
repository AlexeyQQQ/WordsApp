package dev.alexeyqqq.wordsapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary_table ORDER BY name")
    fun getAllDictionaries(): Flow<List<DictionaryDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDictionary(dictionary: DictionaryDbModel): Long

    @Query("UPDATE dictionary_table SET name = :newName WHERE id = :dictionaryId")
    suspend fun updateDictionary(dictionaryId: Long, newName: String)

    @Query("DELETE FROM dictionary_table WHERE id = :dictionaryId")
    suspend fun deleteDictionary(dictionaryId: Long)
}