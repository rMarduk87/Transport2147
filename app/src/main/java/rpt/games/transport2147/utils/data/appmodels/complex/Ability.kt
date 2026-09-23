package rpt.games.transport2147.utils.data.appmodels.complex

import org.w3c.dom.Element
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.enums.AbilityTypeEnum
import rpt.games.transport2147.utils.data.enums.BaseValueModeEnum
import rpt.games.transport2147.utils.xml.XmlUtility
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getFirstSubNode


open class Ability {
    var _modes: MutableMap<BaseValueModeEnum?, BaseValue?>?
    val type: AbilityTypeEnum

    constructor(abilityTypeEnum: AbilityTypeEnum) {
        this._modes = null
        this.type = abilityTypeEnum
        this._modes = HashMap()
        this._modes!![BaseValueModeEnum.ACTUAL] = BaseValue(
            BaseValueModeEnum.ACTUAL,
            abilityTypeEnum.min,
            abilityTypeEnum.max,
            abilityTypeEnum.start
        )
        this._modes!![BaseValueModeEnum.INITIAL] = BaseValue(
            BaseValueModeEnum.INITIAL,
            abilityTypeEnum.min,
            abilityTypeEnum.max,
            abilityTypeEnum.start
        )
    }

    protected constructor(abilityTypeEnum: AbilityTypeEnum, element: Element?, element2: Element?) {
        this._modes = null
        this.type = abilityTypeEnum
        this._modes = HashMap()
        this._modes!![BaseValueModeEnum.ACTUAL] = BaseValue.fromXml(element2)
        this._modes!![BaseValueModeEnum.INITIAL] = BaseValue.fromXml(element)
    }

    fun toXml(): String {
        val map: HashMap<String, String> = HashMap<String, String>()
        this.type.description?.let { map.put("type", it) }
        return XmlUtility.formatNode(
            "ability",
            this._modes!![BaseValueModeEnum.INITIAL]!!.toXml() + this._modes!!
                [BaseValueModeEnum.ACTUAL]!!.toXml(),
            map as MutableMap<String?, String?>?
        )
    }

    fun getBaseValue(baseValueModeEnum: BaseValueModeEnum?): BaseValue? {
        return this._modes!![baseValueModeEnum]
    }

    companion object {
        fun fromXml(element: Element): Ability {
            return Ability(
                AbilityTypeEnum.get(getElementAttribute(element, "type"))!!,
                getFirstSubNode(
                    element,
                    GameConstants.XML_NODE_BASEVALUE,
                    GameConstants.XML_NODE_BASEVALUE_ATTR_MODE,
                    BaseValueModeEnum.INITIAL.value
                ),
                getFirstSubNode(
                    element,
                    GameConstants.XML_NODE_BASEVALUE,
                    GameConstants.XML_NODE_BASEVALUE_ATTR_MODE,
                    BaseValueModeEnum.ACTUAL.value
                )
            )
        }
    }
}