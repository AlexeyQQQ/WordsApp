package dev.alexeyqqq.wordsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.RelationDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel

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

        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(MONITOR) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    AppDatabase::class.java.simpleName,
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}