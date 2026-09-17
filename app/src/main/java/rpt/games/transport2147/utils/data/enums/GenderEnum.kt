package rpt.games.transport2147.utils.data.enums

import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.R


enum class GenderEnum(var textId: Int, str: String) {
    MALE(R.string.txt_Male, GameConstants.GENDER_MALE),
    FEMALE(R.string.txt_Female, GameConstants.GENDER_FEMALE);

    var value: String? = str

    companion object {
        private val lookup = HashMap<String?, GenderEnum?>()

        init {
            for (genderEnum in entries) {
                lookup[genderEnum.value] = genderEnum
            }
        }

        fun get(str: String?): GenderEnum? {
            return lookup[str]
        }
    }
}