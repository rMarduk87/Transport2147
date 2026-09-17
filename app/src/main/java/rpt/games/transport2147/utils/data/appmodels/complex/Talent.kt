package rpt.games.transport2147.utils.data.appmodels.complex

import android.content.Context
import org.w3c.dom.Element
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getFirstSubNode
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement


class Talent private constructor(
    val basedOn: String?,
    val id: String?,
    val title: String?,
    val text: String?
) {
    var descVisible: Boolean = false
    var usage: Boolean = false

    companion object {
        private var _talentList: ArrayList<Talent>? = null
        fun getTalents(context: Context?): ArrayList<Talent>? {
            if (_talentList == null) {
                _talentList = ArrayList<Talent>()
                val talents: ArrayList<String?> = BookManager.getTalents()
                for (i in talents.indices) {
                    val rootElement: Element? = getRootElement(talents[i])
                    val elementAttribute = getElementAttribute(rootElement, "title")
                    val elementAttribute2 = getElementAttribute(rootElement, "id")
                    val textContent = getFirstSubNode(
                        rootElement,
                        GameConstants.XML_NODE_TALENT_TEXT
                    )!!.textContent
                    _talentList!!.add(
                        Talent(
                            getFirstSubNode(
                                rootElement,
                                GameConstants.XML_NODE_TALENT_BASE
                            )!!.textContent, elementAttribute2, elementAttribute, textContent
                        )
                    )
                }
            } else {
                for (i2 in _talentList!!.indices) {
                    _talentList!![i2].usage = false
                }
            }
            return _talentList
        }

        @JvmOverloads
        fun firstTalentUsed(i: Int = 0): Int {
            var i = i
            while (i < _talentList!!.size) {
                if (_talentList!![i].usage) {
                    return i
                }
                i++
            }
            return -1
        }

        @JvmOverloads
        fun lastTalentUsed(i: Int = _talentList!!.size - 1): Int {
            var i = i
            while (i > -1) {
                if (_talentList!![i].usage) {
                    return i
                }
                i--
            }
            return -1
        }
    }
}