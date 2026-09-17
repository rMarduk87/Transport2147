package rpt.games.transport2147.utils.data.appmodels.complex

import android.content.Context
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.xml.XmlUtility
import kotlin.Int
import kotlin.String
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

class Enemy(context: Context?, str: String?) {
    var _craft: Int
    var _damage: Int
    var _mentalEnergy: Int
    var name: String?
    var note: String?
    var noteNode: Element?
    var _phisicalEnergy: Int
    var _protection: Int
    var triggers: ArrayList<Trigger?>
    var _valor: Int

    init {
        val rootElement: Element? =
            XmlUtility.getRootElement(BookManager.getEnemy(str))
        this.name = XmlUtility.getElementAttribute(rootElement, "name")
        this._valor = XmlUtility.getElementAttribute(rootElement, "value")!!.toInt()
        this._craft = XmlUtility.getElementAttribute(rootElement, "craft")!!.toInt()
        this._phisicalEnergy =
            XmlUtility.getElementAttribute(rootElement,
                GameConstants.XML_NODE_ENEMY_ATTR_PE)!!.toInt()
        this._mentalEnergy =
            XmlUtility.getElementAttribute(rootElement,
                GameConstants.XML_NODE_ENEMY_ATTR_ME)!!.toInt()
        this._damage = XmlUtility.getElementAttribute(rootElement, "damage")!!.toInt()
        this._protection = XmlUtility.getElementAttribute(rootElement, "protection")!!.toInt()
        this.noteNode = XmlUtility.getFirstSubNode(rootElement, "note")
        this.note = if (this.noteNode == null) "" else this.noteNode!!.textContent
        this.triggers = ArrayList<Trigger?>()
        val allSubNodes: NodeList =
            XmlUtility.getAllSubNodes(rootElement!!, GameConstants.XML_NODE_TRIGGER)
        for (i in 0..<allSubNodes.length) {
            val element = allSubNodes.item(i) as Element
            this.triggers.add(
                Trigger(
                    Condition.get(
                        XmlUtility.getElementAttribute(
                            element,
                            GameConstants.XML_NODE_TRIGGER_ATTR_CONDITION
                        )
                    ),
                    XmlUtility.getElementAttribute(
                        element,
                        GameConstants.XML_NODE_TRIGGER_ATTR_THRESHOLD
                    )!!.toInt(),
                    XmlUtility.getElementAttribute(
                        element,
                        GameConstants.XML_NODE_TRIGGER_ATTR_TTYPE
                    )?.equals(GameConstants.XML_NODE_TRIGGER_ATTR_TTYPE_OVER, ignoreCase = true),
                    element.textContent
                )
            )
        }
    }

    private fun getNormalizedValue(i: Int): Int {
        return max(min(i, MAXVAL), 0)
    }

    var phisicalEnergy: Int
        get() = this._phisicalEnergy
        set(i) {
            this._phisicalEnergy = getNormalizedValue(i)
        }

    var mentalEnergy: Int
        get() = this._mentalEnergy
        set(i) {
            this._mentalEnergy = getNormalizedValue(i)
        }

    var valor: Int
        get() = this._valor
        set(i) {
            this._valor = getNormalizedValue(i)
        }

    var craft: Int
        get() = this._craft
        set(i) {
            this._craft = getNormalizedValue(i)
        }

    var protection: Int
        get() = this._protection
        set(i) {
            this._protection = getNormalizedValue(i)
        }

    var damage: Int
        get() = this._damage
        set(i) {
            this._damage = getNormalizedValue(i)
        }

    inner class Trigger(condition: Condition?, i: Int, bool: kotlin.Boolean?, str: String?) {
        var activated: kotlin.Boolean? = false
        var condition: Condition? = null
            private set
        var note: String? = null
            private set
        var overThreshold: kotlin.Boolean? = false
            private set
        var threshold: Int
            private set

        init {
            this.threshold = -1
            this.condition = condition
            this.threshold = i
            this.overThreshold = bool
            this.activated = false
            this.note = str
        }
    }

    enum class Condition(str: String) {
        TURN(GameConstants.CONDITION_TURN),
        PLAYER_EF(GameConstants.CONDITION_PLAYER_EF),
        ENEMY_EF(GameConstants.CONDITION_ENEMY_EF);

        val value = str

        companion object {
            private val lookup = HashMap<String?, Condition?>()

            init {
                for (condition in Condition.entries) {
                    lookup[condition.value] = condition
                }
            }

            fun get(str: String?): Condition? {
                return Condition.lookup[str]
            }
        }
    }

    companion object {
        const val MAXVAL: Int = 99
        const val MINVAL: Int = 0
    }
}
