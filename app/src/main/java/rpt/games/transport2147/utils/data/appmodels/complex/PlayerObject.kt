package rpt.games.transport2147.utils.data.appmodels.complex

import org.w3c.dom.Element
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.enums.ObjectTypeEnum
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.xml.XmlUtility
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement


class PlayerObject {
    val chapter: String?
    var descVisible: Boolean
    private var _description: String?
    var id: String?
        private set
    private var _name: String?
    val originalType: ObjectTypeEnum?
    var quantity: Int
    var type: ObjectTypeEnum?

    constructor(str: String?, i: Int, str2: String?) {
        this.descVisible = false
        var rootElement: Element? = null
        try {
            rootElement = getRootElement(BookManager.getObject(str))
        } catch (unused: Exception) {
        }
        val elementAttribute = getElementAttribute(rootElement, "type")
        this.id = str
        this.type = ObjectTypeEnum.get(elementAttribute)
        this.originalType = ObjectTypeEnum.get(elementAttribute)
        this._description = AppUtils.extendedTrim(rootElement!!.textContent)
        this._name = getElementAttribute(rootElement, "name")
        this.quantity = i
        this.chapter = str2
    }

    constructor(element: Element?) {
        this.descVisible = false
        this.id = getElementAttribute(element, "id")
        this.type = ObjectTypeEnum.get(getElementAttribute(element, "type"))
        this.originalType = ObjectTypeEnum.get(
            getElementAttribute(
                element,
                GameConstants.XML_NODE_OBJECT_ATTR_ORIGINALTYPE
            )
        )
        this._description = AppUtils.extendedTrim(element!!.textContent)
        this._name = getElementAttribute(element, "name")
        val elementAttribute = getElementAttribute(element, "quantity")
        this.quantity = (elementAttribute ?: "1").toInt()
        this.chapter = getElementAttribute(element, "chapter")
    }

    constructor(
        str: String?,
        objectTypeEnum: ObjectTypeEnum?,
        str2: String?,
        str3: String?,
        i: Int,
        str4: String?
    ) {
        this.descVisible = false
        this.id = str
        this.type = objectTypeEnum
        this.originalType = objectTypeEnum
        this._description = str3
        this._name = str2
        this.quantity = i
        this.chapter = str4
        if (this.id == "") {
            generateUniqueId()
        }
    }

    fun m4clone(): PlayerObject {
        return PlayerObject(
            "",
            this.type,
            this._name,
            this._description,
            this.quantity,
            this.chapter
        )
    }

    fun toXml(): String {
        val map: HashMap<String, String> = HashMap<String, String>()
        this.id?.let { map.put("id", it) }
        this.name?.let { map.put("name", it) }
        map["quantity"] = this.quantity.toString()
        if (this.chapter != null) {
            map["chapter"] = this.chapter
        }
        map["type"] = this.type!!.value
        map[GameConstants.XML_NODE_OBJECT_ATTR_ORIGINALTYPE] = this.originalType!!.value
        return XmlUtility.formatNode(
            GameConstants.XML_NODE_OBJECT,
            this.description!!.replace("&", "&amp;")
                .replace("<", "&lt;").replace(">", "&gt;"),
            map as MutableMap<String?, String?>?
        )
    }

    constructor(objectTypeEnum: ObjectTypeEnum?, str: String?, str2: String?) : this(
        "",
        objectTypeEnum,
        str,
        str2,
        1,
        null
    ) {
        generateUniqueId()
    }

    private fun generateUniqueId() {
        if (this.id == "" || this.id!!.length <= RANDOM_ID_PREFIX.length ||
            (RANDOM_ID_PREFIX != this.id!!.substring(
                0,
                RANDOM_ID_PREFIX.length
            ))
        ) {
            this.id = RANDOM_ID_PREFIX + System.currentTimeMillis().toString()
        }
    }

    var description: String?
        get() = this._description
        set(str) {
            setDescription(str, true)
        }

    fun setDescription(str: String?, z: Boolean) {
        this._description = str
        if (z) {
            generateUniqueId()
        }
    }

    var name: String?
        get() = this._name
        set(str) {
            setName(str, true)
        }

    fun setName(str: String?, z: Boolean) {
        this._name = str
        if (z) {
            generateUniqueId()
        }
    }

    companion object {
        private const val RANDOM_ID_PREFIX = "rnd_obj_"
    }
}