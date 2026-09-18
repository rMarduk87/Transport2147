package rpt.games.transport2147.utils.managers

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

        fun openBook(){
            val dbBookVersion: BookVersion? = getDbBookVersion()
            val strLoadBook: String? = loadBook()
            val bookVersion = BookVersion(1,
                AppUtils.execSingleRegex(strLoadBook,
                    GameConstants.REGEX_FIND_BOOK_LANGUAGE, 1),
                AppUtils.execSingleRegex(strLoadBook,
                    GameConstants.REGEX_FIND_BOOK_VERSION, 1)?.toInt() ?: 0
            )
            if (dbBookVersion == null ||
                dbBookVersion.language != bookVersion.language ||
                dbBookVersion.version < bookVersion.version) {
                populateDatabase(strLoadBook, bookVersion)
            }
        }

        private fun getDbBookVersion(): BookVersion? {
            try {
                val bookVersion = RepositoryManager.bookRepository.getBookVersion()
                return bookVersion
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
            strLoadBook: String?,
            bookVersion: BookVersion
        ) {
            populateTable("chapters", strLoadBook!!, "chapter", "name")
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
        fun getTemplates(): ArrayList<String?> {
            val templates = RepositoryManager.bookRepository.getAllTemplates()
            return ArrayList(templates)
        }

        @Throws(Exception::class)
        fun getChapter(str: String?): String {
            if (str == null) return ""
            return RepositoryManager.bookRepository.getChapter(str) ?: ""
        }

        @Throws(Exception::class)
        fun getEnemy(str: String?): String {
            return getXmlElement("enemies", "id", "enemy", str)
        }

        private fun getXmlElement(
            tableName: String?,
            queryColumn: String?,
            resultColumn: String?,
            value: String?
        ): String {
            if (value == null) return ""
            
            return when (tableName) {
                "enemies" -> RepositoryManager.bookRepository.getEnemy(value)
                "objects" -> RepositoryManager.bookRepository.getObject(value)
                "talents" -> RepositoryManager.bookRepository.getTalent(value)
                "dictionary" -> RepositoryManager.bookRepository.getDictionaryEntry(value)
                "chapters" -> RepositoryManager.bookRepository.getChapter(value)
                "sheet-templates" -> RepositoryManager.bookRepository.getSheetTemplate(value)
                else -> null
            } ?: ""
        }

        fun getMostRecentProfile() : Profile {
            return RepositoryManager.bookRepository.getMostRecentProfile()
        }

        fun getTalents(): ArrayList<String?> {
            val talents = RepositoryManager.bookRepository.getAllTalents()
            return ArrayList(talents)
        }
        fun getObject(str: String?): String {
            return getXmlElement("objects", "id", "item",
                str)
        }

        fun getDictionaryEntry(str: String?): String {
            return getXmlElement("dictionary", "id",
                "entry", str)
        }
    }
}