package rpt.games.transport2147.utils.view.game

import android.content.Context
import androidx.collection.arrayMapOf
import org.w3c.dom.Element
import rpt.com.base.log.e
import rpt.games.transport2147.utils.data.appmodels.complex.Ability
import rpt.games.transport2147.utils.data.appmodels.complex.PlayerObject
import rpt.games.transport2147.utils.data.appmodels.complex.Talent
import rpt.games.transport2147.utils.data.enums.AbilityTypeEnum
import rpt.games.transport2147.utils.data.enums.BaseValueModeEnum
import rpt.games.transport2147.utils.data.enums.GenderEnum
import rpt.games.transport2147.utils.data.enums.ObjectTypeEnum
import rpt.games.transport2147.utils.data.enums.RulesModeEnum
import rpt.games.transport2147.utils.view.collection.ConditionalArrayList
import rpt.games.transport2147.utils.xml.XmlUtility
import rpt.games.transport2147.utils.xml.XmlUtility.formatNode
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getFirstSubNode
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement
import rpt.games.transport2147.utils.GameConstants
import java.util.EnumMap


class PlayerSheet {
    private var _abilities: MutableMap<AbilityTypeEnum, Ability>
    var gender: GenderEnum? = null
    var language: String?
        private set
    var name: String?
    private var _objects: MutableMap<ObjectTypeEnum, ConditionalArrayList<PlayerObject>>
    var rulesMode: RulesModeEnum? = null
    var talents: ArrayList<Talent>?
        private set
    var tickElapsed: Int

    private fun sheetInitiator(context: Context) {
        this._abilities = EnumMap(AbilityTypeEnum::class.java)
        this.talents = Talent.getTalents(context)
        this._objects = EnumMap(ObjectTypeEnum::class.java)
        for (objectTypeEnum in ObjectTypeEnum.entries) {
            this._objects[objectTypeEnum] =
                ConditionalArrayList<PlayerObject>(context, this,
                    objectTypeEnum, true)
        }
    }

    constructor(context: Context) {
        this.name = ""
        this._abilities = arrayMapOf()
        this.talents = null
        this._objects = arrayMapOf()
        this.tickElapsed = 0
        this.language = ""
        sheetInitiator(context)
        this.name = "Arcturus"
        this.gender = GenderEnum.MALE
        this.rulesMode = RulesModeEnum.MANUAL
        for (abilityTypeEnum in AbilityTypeEnum.entries) {
            this._abilities[abilityTypeEnum] = Ability(abilityTypeEnum)
        }
    }

    constructor(context: Context, element: Element) {
        val z: Boolean
        var `object`: String?
        this.name = ""
        this._abilities = arrayMapOf()
        this.talents = null
        this._objects = arrayMapOf()
        this.tickElapsed = 0
        this.language = ""
        sheetInitiator(context)
        try {
            this.name = getElementAttribute(element, "name")
            this.gender =
                GenderEnum.get(getElementAttribute(element,
                    GameConstants.XML_NODE_SHEET_ATTR_GENDER))
            this.tickElapsed =
                getElementAttribute(element, GameConstants.XML_NODE_SHEET_ATTR_TICKS)!!.toInt()
            this.language = getElementAttribute(element, "language")
            if (this.language == null || this.language != GameLogic.language) {
                this.language = GameLogic.language
                z = true
            } else {
                z = false
            }
            val elementAttribute = getElementAttribute(element,
                GameConstants.XML_NODE_SHEET_ATTR_RULES)
            if (elementAttribute == null) {
                this.rulesMode = RulesModeEnum.MANUAL
            } else {
                this.rulesMode = RulesModeEnum.get(elementAttribute)
            }
            val elementsByTagName =
                getFirstSubNode(element, GameConstants
                    .XML_NODE_SHEET_ABILITIES)!!.getElementsByTagName(
                    "ability"
                )
            for (i in 0..<elementsByTagName.length) {
                val element2 = elementsByTagName.item(i) as Element?
                this._abilities[AbilityTypeEnum.get(getElementAttribute(element2, "type")) as
                        AbilityTypeEnum] =
                    Ability.fromXml(element2!!)
            }
            if (!this._abilities.containsKey(AbilityTypeEnum.FATE_TOT)) {
                val ability: Ability = this._abilities[AbilityTypeEnum.FATE]!!
                val ability2 = Ability(AbilityTypeEnum.FATE_TOT)
                ability2.getBaseValue(BaseValueModeEnum.ACTUAL)!!
                    .set(ability.getBaseValue(BaseValueModeEnum.ACTUAL)!!.value)
                this._abilities[AbilityTypeEnum.FATE_TOT] = ability2
                val ability3: Ability = this._abilities[AbilityTypeEnum.EXPERIENCE]!!
                val ability4 = Ability(AbilityTypeEnum.EXPERIENCE_TOT)
                ability4.getBaseValue(BaseValueModeEnum.ACTUAL)!!
                    .set(ability3.getBaseValue(BaseValueModeEnum.ACTUAL)!!.value)
                this._abilities[AbilityTypeEnum.EXPERIENCE_TOT] = ability4
            }
            val elementsByTagName2 =
                element.getElementsByTagName(GameConstants.XML_NODE_SHEET_OBJECTLIST)
            for (i2 in 0..<elementsByTagName2.length) {
                val element3 = elementsByTagName2.item(i2) as Element
                val conditionalArrayList: ConditionalArrayList<PlayerObject> =
                    this._objects[ObjectTypeEnum.get(
                    getElementAttribute(element3, "id")
                )]!!
                conditionalArrayList.setConditionCheckFlag(false)
                val elementsByTagName3 = element3.getElementsByTagName(GameConstants.XML_NODE_OBJECT)
                for (i3 in 0..<elementsByTagName3.length) {
                    val playerObject = PlayerObject(elementsByTagName3.item(i3) as Element?)
                    if (z
                    ) {
                        val playerObject2 = PlayerObject(getRootElement(`object`))
                        playerObject.setName(playerObject2.name, false)
                        playerObject.setDescription(playerObject2.description, false)
                    }
                    conditionalArrayList.add(playerObject)
                }
                conditionalArrayList.setConditionCheckFlag(true)
            }
            val elementsByTagName4 =
                getFirstSubNode(element, GameConstants.XML_NODE_SHEET_TALENTS)!!
                    .getElementsByTagName(
                    GameConstants.XML_NODE_SHEET_TALENT
                )
            for (i4 in 0..<elementsByTagName4.length) {
                val element4 = elementsByTagName4.item(i4) as Element?
                var elementAttribute2 = getElementAttribute(element4, "id")
                elementAttribute2 = elementAttribute2
                    ?: getElementAttribute(
                        element4,
                        "name"
                    )
                for (i5 in this.talents!!.indices) {
                    val talent = this.talents!![i5]
                    if (talent.id.equals(elementAttribute2)) {
                        talent.usage = true
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun toXml(context: Context): String? {
        try {
            val stringBuffer = StringBuffer()
            stringBuffer.append(abilitiesToXml())
            stringBuffer.append(specialtiesToXml())
            stringBuffer.append(objectsToXml())
            val map: HashMap<String, String> = HashMap<String, String>()
            this.name?.let { map.put("name", it) }
            this.gender!!.value?.let { map.put(GameConstants.XML_NODE_SHEET_ATTR_GENDER, it) }
            map[GameConstants.XML_NODE_SHEET_ATTR_TICKS] = this.tickElapsed.toString()
            this.rulesMode!!.value?.let { map.put(GameConstants.XML_NODE_SHEET_ATTR_RULES, it) }
            this.language?.let { map.put("language", it) }
            return XmlUtility.formatNode("sheet", stringBuffer.toString(),
                map as MutableMap<String?, String?>?
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return null
        }
    }

    private fun specialtiesToXml(): String {
        val stringBuffer = StringBuffer()
        for (i in this.talents!!.indices) {
            val talent = this.talents!![i]
            if (talent.usage) {
                val map: HashMap<String, String> = HashMap<String, String>()
                talent.id?.let { map.put("id", it) }
                stringBuffer.append(
                    XmlUtility.formatNode(
                        GameConstants.XML_NODE_SHEET_TALENT,
                        null,
                        map as MutableMap<String?, String?>?
                    )
                )
            }
        }
        return formatNode(GameConstants.XML_NODE_SHEET_TALENTS,
            stringBuffer.toString(), null)
    }

    private fun abilitiesToXml(): String {
        val stringBuffer = StringBuffer()
        for (abilityTypeEnum in AbilityTypeEnum.entries) {
            stringBuffer.append(this._abilities!![abilityTypeEnum]!!.toXml())
        }
        return formatNode(GameConstants.XML_NODE_SHEET_ABILITIES,
            stringBuffer.toString(), null)
    }

    private fun objectsToXml(): String {
        val stringBuffer = StringBuffer()
        for (objectTypeEnum in ObjectTypeEnum.entries) {
            val conditionalArrayList: ConditionalArrayList<PlayerObject> =
                this._objects[objectTypeEnum]!!
            val stringBuffer2 = StringBuffer()
            for (i in conditionalArrayList.indices) {
                stringBuffer2.append(conditionalArrayList[i]!!.toXml())
            }
            val map: HashMap<String, String> = HashMap<String, String>()
            map["id"] = objectTypeEnum.value
            stringBuffer.append(
                XmlUtility.formatNode(
                    GameConstants.XML_NODE_SHEET_OBJECTLIST,
                    stringBuffer2.toString(),
                    map as MutableMap<String?, String?>?
                )
            )
        }
        return stringBuffer.toString()
    }

    fun getAbility(abilityTypeEnum: AbilityTypeEnum?): Ability? {
        return this._abilities[abilityTypeEnum]
    }

    fun getItemsList(objectTypeEnum: ObjectTypeEnum?): ArrayList<PlayerObject?>? {
        return this._objects[objectTypeEnum]
    }
}