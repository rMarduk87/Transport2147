package rpt.games.transport2147.utils.data.enums

import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.R


enum class RulesModeEnum(var textId: Int, str: String) {
    MANUAL(R.string.txt_RulesManual, GameConstants.SHEET_RULES_MANUAL),
    MIXED(R.string.txt_RulesMixed, GameConstants.SHEET_RULES_MIXED),
    ENFORCED(R.string.txt_RulesEnforced, GameConstants.SHEET_RULES_ENFORCED);

    var value: String? = str

    companion object {
        private val lookup = HashMap<String?, RulesModeEnum?>()

        init {
            for (rulesModeEnum in entries) {
                lookup[rulesModeEnum.value] = rulesModeEnum
            }
        }

        fun get(str: String?): RulesModeEnum? {
            return lookup[str]
        }
    }
}