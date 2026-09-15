package rpt.games.transport2147.utils.data.database

import android.content.Context
import androidx.room.*
import rpt.games.transport2147.utils.data.database.dao.TransportDao
import rpt.games.transport2147.utils.data.database.DatabaseHelper.Companion.databaseName
import rpt.games.transport2147.utils.data.database.models.*


@Database(
    entities = [
        BookVersionModel::class,
        ChaptersModel::class,
        DictionaryModel::class,
        EnemyModel::class,
        ObjectsModel::class,
        ProfilesModel::class,
        SheetTemplatesModel::class,
        TalentsModel::class,
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transportDao(): TransportDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName,
        )
            .build()
    }
}