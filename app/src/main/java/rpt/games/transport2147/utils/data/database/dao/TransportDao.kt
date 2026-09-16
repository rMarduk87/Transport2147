package rpt.games.transport2147.utils.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import rpt.games.transport2147.utils.data.database.models.*

@Dao
interface TransportDao {
    suspend fun clear(){
        clearBook()
        clearChapters()
        clearDictionary()
        clearEnemy()
        clearObjects()
        clearProfiles()
        clearSheet()
        clearTalents()
    }
    @Query("DELETE FROM book_version")
    suspend fun clearBook()
    @Query("DELETE FROM chapters")
    suspend fun clearChapters()
    @Query("DELETE FROM dictionary")
    suspend fun clearDictionary()
    @Query("DELETE FROM enemies")
    suspend fun clearEnemy()
    @Query("DELETE FROM objects")
    suspend fun clearObjects()
    @Query("DELETE FROM profiles")
    suspend fun clearProfiles()
    @Query("DELETE FROM sheet_templates")
    suspend fun clearSheet()
    @Query("DELETE FROM talents")
    suspend fun clearTalents()

    @Query("SELECT * FROM book_version")
    fun getBookVersion(): BookVersionModel?

    @Query("SELECT chapter FROM chapters WHERE id = :id")
    fun getChapterById(id: String): String?

    @Query("SELECT sheet_template FROM sheet_templates")
    fun getAllTemplates(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookVersion(bookVersionModel: BookVersionModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChapter(chaptersModel: ChaptersModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEnemy(enemyModel: EnemyModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertObject(objectsModel: ObjectsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTalent(talentsModel: TalentsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSheetTemplate(sheetTemplatesModel: SheetTemplatesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDictionaryEntry(dictionaryModel: DictionaryModel)
}