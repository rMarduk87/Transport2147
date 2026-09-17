package rpt.games.transport2147.utils.data.enums

import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.R


enum class ObjectTypeEnum(str: String, i: Int, z: Boolean, z2: Boolean, i2: Int) {
    ARMOR(GameConstants.OBJECT_TYPE_ARMOR, R.id.frgSheet_layArmor, true, true, R.string.txt_Armor),
    CODE(GameConstants.OBJECT_TYPE_CODE, R.id.frgSheet_layCodes, true, false, R.string.txt_Code),
    FOOD("food", R.id.frgSheet_layFood, false, false, -1),
    MONEY("money", R.id.frgSheet_layMoney, false, false, -1),
    ITEM(GameConstants.OBJECT_TYPE_ITEM, R.id.frgSheet_layItems, true, true, R.string.txt_Item),
    NOTE("note", R.id.frgSheet_layNotes, true, false, R.string.txt_Note),
    WEAPON(GameConstants.OBJECT_TYPE_WEAPON, R.id.frgSheet_layWeapons, true, true, R.string.txt_Weapon);

    val container: Int = i
    val iterable: Boolean = z
    val label: Int = i2
    val quantityEnabled: Boolean = z2
    val value: String = str

    companion object {
        private val lookup = HashMap<String?, ObjectTypeEnum?>()

        init {
            for (objectTypeEnum in entries) {
                lookup[objectTypeEnum.value] = objectTypeEnum
            }
        }

        fun get(str: String?): ObjectTypeEnum? {
            return lookup[str]
        }
    }
}