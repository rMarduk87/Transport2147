package rpt.games.transport2147.utils.data.enums

import rpt.games.transport2147.utils.GameConstants


enum class AbilityTypeEnum(val min: Int, val max: Int, val start: Int, str: String) {
    VALOR(0, 99, 7, GameConstants.ABILITY_TYPE_VALOR),
    KNOWLEDGE(0, 99, 7, GameConstants.ABILITY_TYPE_KNOWLEDGE),
    CRAFT(0, 99, 6, "craft"),
    MENTAL_ENERGY(0, 30, 15, GameConstants.ABILITY_TYPE_MENTALENERGY),
    PHYSICAL_ENERGY(0, 30, 15, GameConstants.ABILITY_TYPE_PHYSICALENERGY),
    FATE(0, 9999, 0, GameConstants.ABILITY_TYPE_FATE),
    EXPERIENCE(0, 9999, 0, GameConstants.ABILITY_TYPE_EXPERIENCE),
    FATE_TOT(0, 9999, 0, GameConstants.ABILITY_TYPE_FATE_TOTAL),
    EXPERIENCE_TOT(0, 9999, 0, GameConstants.ABILITY_TYPE_EXPERIENCE_TOTAL),
    MONEY(0, 9999, 0, "money"),
    FOOD(0, 99, 0, "food"),
    PROTECTION(0, 99, 0, "protection"),
    DAMAGE(0, 99, 0, "damage"),
    ITEMS(0, 99, 0, GameConstants.ABILITY_TYPE_ITEMS);

    val description: String? = str

    companion object {
        private val lookup = HashMap<String?, AbilityTypeEnum?>()

        init {
            for (abilityTypeEnum in entries) {
                lookup[abilityTypeEnum.description] = abilityTypeEnum
            }
        }

        fun get(str: String?): AbilityTypeEnum? {
            return lookup[str]
        }
    }
}