package rpt.games.transport2147.utils.data.appmodels.complex

class Profile(
    var id: String?,
    var name: String?,
    var lastUsed: String?,
    private var _sheetData: String?,
    private var _historyData: String?
) {
    private fun setLastUsed() {
        this.lastUsed = now
    }

    var sheetData: String?
        get() = this._sheetData
        set(str) {
            this._sheetData = str
            setLastUsed()
        }

    var historyData: String?
        get() = this._historyData
        set(str) {
            this._historyData = str
            setLastUsed()
        }

    fun setRandomProfileId() {
        this.id = RANDOM_ID_PREFIX + System.currentTimeMillis().toString()
    }

    companion object {
        private const val RANDOM_ID_PREFIX = "rnd_prf_"
        fun getProfile(
            str: String?,
            str2: String?,
            str3: String?,
            str4: String?,
            str5: String?
        ): Profile {
            return Profile(str, str2, str3, str4, str5)
        }

        fun newProfile(str: String?, str2: String?): Profile {
            return getProfile(
                RANDOM_ID_PREFIX + System.currentTimeMillis().toString(), str,
                now, str2, ""
            )
        }

        fun newProfile(str: String?, str2: String?, str3: String?): Profile {
            return getProfile(
                RANDOM_ID_PREFIX + System.currentTimeMillis().toString(), str,
                now, str2, str3
            )
        }

        private val now: String
            get() = System.currentTimeMillis().toString()
    }
}