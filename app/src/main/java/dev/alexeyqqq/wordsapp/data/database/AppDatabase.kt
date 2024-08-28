package dev.alexeyqqq.wordsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.RelationDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        WordDbModel::class,
        DictionaryDbModel::class,
        RelationDbModel::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordsDao(): WordsDao
    abstract fun dictionaryDao(): DictionaryDao
    abstract fun relationDao(): RelationDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MONITOR = Any()

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(MONITOR) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    AppDatabase::class.java.simpleName,
                ).addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = db
                return db
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { appDatabase ->
                scope.launch {
                    appDatabase.dictionaryDao().insertDictionary(
                        DictionaryDbModel(id = 1, name = "Базовый набор слов")
                    )

                    val listOfWords = listOf(
                        WordDbModel(original = "hello", translation = "привет"),
                        WordDbModel(original = "dog", translation = "собака"),
                        WordDbModel(original = "cat", translation = "кошка"),
                        WordDbModel(original = "thank you", translation = "спасибо"),
                        WordDbModel(original = "text", translation = "текст"),
                        WordDbModel(original = "news", translation = "новость"),
                        WordDbModel(original = "word", translation = "слово"),
                        WordDbModel(original = "letter", translation = "письмо"),
                        WordDbModel(original = "message", translation = "сообщение"),
                        WordDbModel(original = "note", translation = "заметка"),
                        WordDbModel(original = "weather", translation = "погода"),
                        WordDbModel(original = "sun", translation = "солнце"),
                        WordDbModel(original = "pigeon", translation = "голубь"),
                        WordDbModel(original = "human", translation = "человек"),
                        WordDbModel(original = "piano", translation = "пианино"),
                        WordDbModel(original = "fork", translation = "вилка"),
                        WordDbModel(original = "spoon", translation = "ложка"),
                        WordDbModel(original = "country", translation = "страна"),
                        WordDbModel(original = "apple", translation = "яблоко"),
                        WordDbModel(original = "mouse", translation = "мышь"),
                    )

                    listOfWords.forEach {
                        appDatabase.wordsDao().insertWord(it)
                        appDatabase.relationDao().insertRelation(
                            RelationDbModel(wordId = it.id, dictionaryId = 1)
                        )
                    }
                }
            }
        }
    }
}