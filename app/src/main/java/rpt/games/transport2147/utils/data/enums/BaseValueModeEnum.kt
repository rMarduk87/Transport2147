package rpt.games.transport2147.utils.data.enums

import rpt.games.transport2147.utils.GameConstants


enum class BaseValueModeEnum(str: String) {
    INITIAL(GameConstants.BV_MODE_INITIAL),
    ACTUAL(GameConstants.BV_MODE_ACTUAL);

    val value: String = str

    companion object {
        private val lookup = HashMap<String?, BaseValueModeEnum?>()

        init {
            for (baseValueModeEnum in entries) {
                lookup[baseValueModeEnum.value] = baseValueModeEnum
            }
        }

        fun get(str: String?): BaseValueModeEnum? {
            return lookup[str]
        }
    }
}