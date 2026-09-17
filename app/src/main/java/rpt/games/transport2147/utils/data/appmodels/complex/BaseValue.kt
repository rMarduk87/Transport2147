package rpt.games.transport2147.utils.data.appmodels.complex

import org.w3c.dom.Element
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.enums.BaseValueModeEnum
import rpt.games.transport2147.utils.xml.XmlUtility
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute


open class BaseValue {
    private val _maxValue: Int
    private val _minValue: Int
    private var _mode: BaseValueModeEnum? = null
    var value: Int
        private set

    constructor(baseValueModeEnum: BaseValueModeEnum, i: Int, i2: Int, i3: Int) {
        this._minValue = i
        this._maxValue = i2
        this.value = i3
        this._mode = baseValueModeEnum
    }

    constructor(i: Int, i2: Int, i3: Int) {
        this._minValue = i
        this._maxValue = i2
        this.value = i3
    }

    fun toXml(): String {
        val map: HashMap<String, String> = HashMap<String, String>()
        map[GameConstants.XML_NODE_BASEVALUE_ATTR_MODE] = this._mode!!.value
        map[GameConstants.XML_NODE_BASEVALUE_ATTR_MIN] = this._minValue.toString()
        map[GameConstants.XML_NODE_BASEVALUE_ATTR_MAX] = this._maxValue.toString()
        map["value"] = this.value.toString()
        return XmlUtility.formatNode(GameConstants.XML_NODE_BASEVALUE, null,
            map as MutableMap<String?, String?>?
        )
    }

    val mode: BaseValueModeEnum
        get() = this._mode!!

    fun add(): Int {
        if (this.value < this._maxValue) {
            this.value++
        }
        return this.value
    }

    fun set(i: Int): Int {
        if (i < this._maxValue) {
            this.value = i
        } else {
            this.value = this._maxValue
        }
        return this.value
    }

    fun subtract(): Int {
        if (this.value > this._minValue) {
            this.value--
        }
        return this.value
    }

    companion object {
        fun fromXml(element: Element?): BaseValue {
            return BaseValue(
                BaseValueModeEnum.get(
                    getElementAttribute(
                        element,
                        GameConstants.XML_NODE_BASEVALUE_ATTR_MODE
                    )
                )!!,
                getElementAttribute(element,
                    GameConstants.XML_NODE_BASEVALUE_ATTR_MIN)!!.toInt(),
                getElementAttribute(element,
                    GameConstants.XML_NODE_BASEVALUE_ATTR_MAX)!!.toInt(),
                getElementAttribute(element, "value")!!.toInt()
            )
        }
    }
}