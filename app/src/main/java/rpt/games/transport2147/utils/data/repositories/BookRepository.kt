package rpt.games.transport2147.utils.data.repositories

import rpt.games.transport2147.utils.data.appmodels.BookVersion
import rpt.games.transport2147.utils.data.appmodels.Profile
import rpt.games.transport2147.utils.data.database.dao.TransportDao
import rpt.games.transport2147.utils.data.database.models.*

class BookRepository(
    private val bookDao: TransportDao
) {
    suspend fun clearAll() {
        bookDao.clear()
    }

    fun getBookVersion() : BookVersion? {
        return bookDao.getBookVersion()?.map()
    }

    fun getChapter(id: String): String? {
        return bookDao.getChapterById(id)
    }

    fun getEnemy(id: String): String? {
        return bookDao.getEnemyById(id)
    }

    fun getObject(id: String): String? {
        return bookDao.getObjectById(id)
    }

    fun getTalent(id: String): String? {
        return bookDao.getTalentById(id)
    }

    fun getDictionaryEntry(id: String): String? {
        return bookDao.getDictionaryEntryById(id)
    }

    fun getSheetTemplate(id: String): String? {
        return bookDao.getSheetTemplateById(id)
    }

    fun getAllTemplates(): List<String> {
        return bookDao.getAllTemplates()
    }

    fun insertBookVersion(bookVersion: BookVersion) {
        bookDao.insertBookVersion(bookVersion.toDBModel())
    }

    fun insertChapter(chaptersModel: ChaptersModel) {
        bookDao.insertChapter(chaptersModel)
    }

    fun insertEnemy(enemyModel: EnemyModel) {
        bookDao.insertEnemy(enemyModel)
    }

    fun insertObject(objectsModel: ObjectsModel) {
        bookDao.insertObject(objectsModel)
    }

    fun insertTalent(talentsModel: TalentsModel) {
        bookDao.insertTalent(talentsModel)
    }

    fun insertSheetTemplate(sheetTemplatesModel: SheetTemplatesModel) {
        bookDao.insertSheetTemplate(sheetTemplatesModel)
    }

    fun insertDictionaryEntry(dictionaryModel: DictionaryModel) {
        bookDao.insertDictionaryEntry(dictionaryModel)
    }

    fun getMostRecentProfile(): Profile {
        return bookDao.getMostRecentProfile().map()
    }

    fun getAllTalents(): List<String> {
        return bookDao.getAllTalents()
    }
}