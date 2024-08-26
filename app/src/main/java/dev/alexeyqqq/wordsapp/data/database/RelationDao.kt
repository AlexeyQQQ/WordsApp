package dev.alexeyqqq.wordsapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.RelationDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationDao {

    @Query(
        """
        SELECT words_table.* FROM words_table
        INNER JOIN relation_table
        ON words_table.id = relation_table.wordId
        WHERE relation_table.dictionaryId = :dictionaryId
    """
    )
    fun getDictionaryWords(dictionaryId: Long): Flow<List<WordDbModel>>

    @Query(
        """
        SELECT dictionary_table.* FROM dictionary_table
        LEFT JOIN relation_table
        ON dictionary_table.id = relation_table.dictionaryId AND relation_table.wordId = :wordId
        WHERE relation_table.wordId IS NULL"""
    )
    fun getDictionariesWithoutWord(wordId: Long): Flow<List<DictionaryDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelation(relationDbModel: RelationDbModel)

    @Query("DELETE FROM relation_table WHERE dictionaryId = :dictionaryId")
    suspend fun deleteAllRelationsForDictionary(dictionaryId: Long)

    @Query("DELETE FROM relation_table WHERE wordId = :wordId AND dictionaryId = :dictionaryId")
    suspend fun removeWordFromDictionary(wordId: Long, dictionaryId: Long)
}