package rpt.games.transport2147.utils.managers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import rpt.com.base.log.e
import rpt.games.transport2147.R
import rpt.games.transport2147.TransportApplication
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.appmodels.BookVersion
import rpt.games.transport2147.utils.data.appmodels.Profile
import rpt.games.transport2147.utils.data.database.models.*
import rpt.games.transport2147.utils.file.FileUtility


class BookManager {

    companion object {

        fun openBook() = runBlocking(Dispatchers.IO) {
            val dbBookVersion: BookVersion? = getDbBookVersion()
            val strLoadBook = loadBook() ?: return@runBlocking
            
            val bookVersion = BookVersion(1,
                AppUtils.execSingleRegex(strLoadBook,
                    GameConstants.REGEX_FIND_BOOK_LANGUAGE, 1),
                AppUtils.execSingleRegex(strLoadBook,
                    GameConstants.REGEX_FIND_BOOK_VERSION, 1)?.toInt() ?: 0
            )
            
            val isDatabaseEmpty = getChapter("INTRODUZIONE").isEmpty()
            
            if (dbBookVersion == null || isDatabaseEmpty ||
                dbBookVersion.language != bookVersion.language ||
                dbBookVersion.version < bookVersion.version) {
                populateDatabase(strLoadBook, bookVersion)
            }
        }

        private fun getDbBookVersion(): BookVersion? {
            try {
                return RepositoryManager.bookRepository.getBookVersion()
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
            return null
        }

        private fun loadBook(): String? {
            return FileUtility.loadResourceFileAsString(TransportApplication.instance,
                R.raw.transport_2147);
        }

        private fun populateDatabase(
            strLoadBook: String,
            bookVersion: BookVersion
        ) {
            populateTable("chapters", strLoadBook, "chapter", "name")
            populateTable("enemy", strLoadBook, GameConstants.XML_NODE_ENEMY,
                "id")
            populateTable("objects", strLoadBook, GameConstants.XML_NODE_OBJECT,
                "id")
            populateTable("talents", strLoadBook, GameConstants.XML_NODE_TALENT,
                "id")
            populateTable("sheet-templates", strLoadBook, "sheet", "id")
            populateTable("dictionary", strLoadBook, GameConstants.XML_NODE_ENTRY,
                "id")
            RepositoryManager.bookRepository.insertBookVersion(bookVersion)
        }

        private fun populateTable(
            tableName: String,
            book: String,
            string3: String,
            string4: String
        ) {
            val mapExecMultiMatchRegularExpression: MutableMap<String, String> =
                AppUtils.execMultiMatchRegularExpression(string3, string4, book)
            for (str4 in mapExecMultiMatchRegularExpression.keys) {
                val value = mapExecMultiMatchRegularExpression[str4]!!
                val processedValue = value.replace("'", "''")

                when (tableName) {
                    "chapters" -> RepositoryManager.bookRepository.insertChapter(
                        ChaptersModel(str4, processedValue)
                    )
                    "enemy" -> RepositoryManager.bookRepository.insertEnemy(
                        EnemyModel(str4, processedValue)
                    )
                    "objects" -> RepositoryManager.bookRepository.insertObject(
                        ObjectsModel(str4, processedValue)
                    )
                    "talents" -> RepositoryManager.bookRepository.insertTalent(
                        TalentsModel(str4, processedValue)
                    )
                    "sheet-templates" -> RepositoryManager.bookRepository.insertSheetTemplate(
                        SheetTemplatesModel(str4, processedValue)
                    )
                    "dictionary" -> RepositoryManager.bookRepository.insertDictionaryEntry(
                        DictionaryModel(str4, processedValue)
                    )
                }
            }
        }
        fun getTemplates(): ArrayList<String?> = runBlocking(Dispatchers.IO) {
            val templates = RepositoryManager.bookRepository.getAllTemplates()
            return@runBlocking ArrayList(templates)
        }

        @Throws(Exception::class)
        fun getChapter(str: String?): String = runBlocking(Dispatchers.IO) {
            if (str == null) return@runBlocking ""
            return@runBlocking RepositoryManager.bookRepository.getChapter(str) ?: ""
        }

        @Throws(Exception::class)
        fun getEnemy(str: String?): String = runBlocking(Dispatchers.IO) {
            return@runBlocking getXmlElement("enemies", "id",
                "enemy", str)
        }

        private fun getXmlElement(
            tableName: String?,
            queryColumn: String?,
            resultColumn: String?,
            value: String?
        ): String = runBlocking(Dispatchers.IO) {
            if (value == null) return@runBlocking ""
            
            return@runBlocking when (tableName) {
                "enemies" -> RepositoryManager.bookRepository.getEnemy(value)
                "objects" -> RepositoryManager.bookRepository.getObject(value)
                "talents" -> RepositoryManager.bookRepository.getTalent(value)
                "dictionary" -> RepositoryManager.bookRepository.getDictionaryEntry(value)
                "chapters" -> RepositoryManager.bookRepository.getChapter(value)
                "sheet-templates" -> RepositoryManager.bookRepository.getSheetTemplate(value)
                else -> null
            } ?: ""
        }

        fun getMostRecentProfile() : rpt.games.transport2147.utils.data.appmodels.complex.Profile? =
            runBlocking(Dispatchers.IO) {
                val profile =
                    RepositoryManager.bookRepository.getMostRecentProfile()?.map<Profile>()
                        ?: return@runBlocking null
                val profileComplex = rpt.games.transport2147.utils.data.appmodels.complex.Profile(
                profile.id, profile.name, profile.sheet,
                profile.history, profile.used)
            return@runBlocking profileComplex
        }

        fun getTalents(): ArrayList<String?> = runBlocking(Dispatchers.IO) {
            val talents = RepositoryManager.bookRepository.getAllTalents()
            return@runBlocking ArrayList(talents)
        }
        fun getObject(str: String?): String = runBlocking(Dispatchers.IO) {
            return@runBlocking getXmlElement("objects", "id", "item",
                str)
        }

        fun getDictionaryEntry(str: String?): String = runBlocking(Dispatchers.IO) {
            return@runBlocking getXmlElement("dictionary", "id",
                "entry", str)
        }

        fun deleteProfile(profile: rpt.games.transport2147.utils.data.appmodels.complex.Profile) =
            runBlocking(Dispatchers.IO) {
            val profileApp = Profile(profile.id!!, profile.name!!,
                profile.sheetData!!, profile.historyData!!, profile.lastUsed!!)
            RepositoryManager.bookRepository.deleteProfile(profileApp.id)
        }

        fun addProfile(profile: rpt.games.transport2147.utils.data.appmodels.complex.Profile):
                Boolean = runBlocking(Dispatchers.IO) {
            val profileApp = Profile(profile.id!!, profile.name!!,
                profile.sheetData!!, profile.historyData!!, profile.lastUsed!!)
            RepositoryManager.bookRepository.insertNewProfile(profileApp.toDBModel())
            return@runBlocking true
        }

        fun updateProfileHistory(profile: rpt.games.transport2147.utils.data.appmodels.complex.Profile):
                Boolean = runBlocking(Dispatchers.IO) {
            RepositoryManager.bookRepository.updateProfileHistory(profile.id!!,
                profile.historyData!!, profile.lastUsed!!)
            return@runBlocking true
        }

        fun updateProfileSheet(profile: rpt.games.transport2147.utils.data.appmodels.complex.Profile):
                Boolean = runBlocking(Dispatchers.IO) {
            RepositoryManager.bookRepository.updateProfileSheet(profile.id!!,
                profile.sheetData!!, profile.lastUsed!!)
            return@runBlocking true
        }

        fun getProfiles(): ArrayList<rpt.games.transport2147.utils.data.appmodels.complex.Profile?> =
            runBlocking(Dispatchers.IO) {
            val profiles = RepositoryManager.bookRepository.getAllProfiles()
            val arrayList = ArrayList<rpt.games.transport2147.utils.data.appmodels.complex.Profile?>()
            for (profilesModel in profiles) {
                val profile = profilesModel.map<Profile>()
                arrayList.add(
                    rpt.games.transport2147.utils.data.appmodels.complex.Profile(
                        profile.id, profile.name, profile.used,
                        profile.sheet, profile.history
                    )
                )
            }
            return@runBlocking arrayList
        }

        @Throws(Exception::class)
        fun getChapterSummary(str: String?): String? = runBlocking(Dispatchers.IO) {
            return@runBlocking AppUtils.execSingleRegex(getChapter(str),
                GameConstants.REGEX_FIND_CHAPTER_SUMMARY)
        }

        fun getDictionaryEntries(): ArrayList<String?> = runBlocking(Dispatchers.IO) {
            return@runBlocking ArrayList(
                RepositoryManager.bookRepository.getAllDictionaryEntries())
        }
    }
}
