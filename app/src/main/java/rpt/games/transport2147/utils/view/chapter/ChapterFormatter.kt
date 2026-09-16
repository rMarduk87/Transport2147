package rpt.games.transport2147.utils.view.chapter

import android.content.Context
import android.widget.LinearLayout
import org.w3c.dom.Element
import rpt.com.base.log.e
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GamesConstants
import rpt.games.transport2147.utils.data.enums.GenderEnum
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement


class ChapterFormatter {

    private var _summary: String? = ""

    @Throws(Exception::class)
    fun formatChapter(linearLayout: LinearLayout?, context: Context?, str: String?): Boolean {
        try {
            val strExecReplace: String = AppUtils.execReplace(
                BookManager.getChapter(str),
                GamesConstants.REGEX_GENDER_SEARCH,
                if (GameLogic.PlayerSheet.getGender() == GenderEnum.MALE)
                    GamesConstants.REGEX_GENDER_REPLACE_MALE else
                        GamesConstants.REGEX_GENDER_REPLACE_FEMALE
            )
            val rootElement: Element? = getRootElement(strExecReplace)
            var attribute: String? = rootElement!!
                .getAttribute(GamesConstants.XML_NODE_CHAPTER_ATTR_DREAM)
            if (attribute == null) {
                attribute = "false"
            }
            _summary =
                AppUtils.execSingleRegex(strExecReplace, GamesConstants.REGEX_FIND_CHAPTER_SUMMARY)
            createChapter(linearLayout, context, rootElement)
            return attribute.toBoolean()
        } catch (e: Exception) {
            e(Throwable(e), "Error formatting chapter " + str + " "
                    + e.message)
        }
        return false
    }

    private fun createChapter(
        linearLayout: android.widget.LinearLayout?,
        context: android.content.Context?,
        rootElement: org.w3c.dom.Element
    ) {
    }

    fun getLastSummary(): String? {
        return _summary
    }
}