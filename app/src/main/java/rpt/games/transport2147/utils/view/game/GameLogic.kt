package rpt.games.transport2147.utils.view.game

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rpt.com.base.log.e
import rpt.games.transport2147.HistoryActivity
import rpt.games.transport2147.MainMenuActivity
import rpt.games.transport2147.NavigatorActivity
import rpt.games.transport2147.R
import rpt.games.transport2147.SettingsActivity
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.appmodels.complex.Enemy
import rpt.games.transport2147.utils.data.appmodels.complex.History
import rpt.games.transport2147.utils.data.appmodels.complex.PlayerObject
import rpt.games.transport2147.utils.data.appmodels.complex.Profile
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.managers.DiceRollerManager
import rpt.games.transport2147.utils.managers.ProfileManager
import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import java.util.Locale


class GameLogic {

    companion object{
        private lateinit var _pf: SettingsActivity.SettingsFragment
        var appContext: Context? = null
        var chapterFragment: NavigatorActivity.ChapterFragment? = null
        var diceRoller1: DiceRollerManager? = null
        var diceRoller2: DiceRollerManager? = null
        var enemy: Enemy? = null
        var history: HistoryActivity? = null
        var mainMenu: MainMenuActivity? = null
        var navigator: NavigatorActivity? = null
        var playerSheet: PlayerSheet? = null
        var profile: Profile? = null
        private var _TookObjects: HashMap<PlayerObject?, PlayerObject?>? = null
        var _fontDimension: Int = 2130968660
        var _fontDimensionIndex: Int = 2
        var _fontDimensionMaxIndex: Int = 12
        var _fontDimensionMinIndex: Int = 0
        var _fontName: String? = null
        var language: String? = null
            private set
            @JvmName("getLanguageProp") get
        var iconToggleHistory: ImageView? = null
        var iconToggleJustification: ImageView? = null
        var remoteToken: String? = null
        var remoteUser: String? = null
        var warning_changeInitialValue: Boolean = false
        var warning_surpassInitialValue: Boolean = false

        private val logicScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        fun initApp(context: Context?) {
            appContext = context
            changeFontSize(context!!, SharedPreferencesManager.fontSize)
            setCoreFont(context, SharedPreferencesManager.coreFontName)
            if (language == null) {
                setLanguage(context, SharedPreferencesManager.language)
            }
        }

        fun checkForAppRecovery(activity: Activity, z: Boolean) {
            try {
                if (History.requestedChapter == null || profile == null) {
                    initApp(activity.applicationContext)
                    logicScope.launch(Dispatchers.IO) {
                        val mostRecentProfile = BookManager.getMostRecentProfile()
                        if (!z || mostRecentProfile == null) {
                            return@launch
                        }
                        withContext(Dispatchers.Main) {
                            ProfileManager.loadProfile(activity, mostRecentProfile)
                        }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
        }

        fun setLanguage(context: Context?, str: String?) {
            language = str
        }

        fun setLanguage(str: String?) {
            language = str
        }

        fun setCoreFont(context: Context?, str: String?) {
            _fontName = str
        }

        fun getFontSize(): Int {
            val i = SharedPreferencesManager.fontSize
            var i2 = -1
            if (i != -1) {
                return i
            }
            when (AppUtils.getDeviceType(this.appContext!!)) {
                0 -> i2 = R.string.param_initialFontSizeSmartphone
                1 -> i2 = R.string.param_initialFontSizeTablet7
                2 -> i2 = R.string.param_initialFontSizeTablet10
            }
            val i3 = this.appContext!!.getString(i2).toInt()
            setFontSize(i3)
            return i3
        }

        fun setFontSize(i: Int) {
            SharedPreferencesManager.fontSize = i
        }

        fun changeFontSize(context: Context, view: View) {
            if (view.id == R.id.incFontChgr_btnFontIncrease) {
                if (_fontDimensionIndex == _fontDimensionMaxIndex) {
                    return
                } else {
                    _fontDimensionIndex++
                }
            } else if (_fontDimensionIndex == _fontDimensionMinIndex) {
                return
            } else {
                _fontDimensionIndex--
            }
            calculateFontSize(context)
            SharedPreferencesManager.fontSize = _fontDimensionIndex
        }

        fun changeFontSize(context: Context, i: Int) {
            _fontDimensionIndex = i
            calculateFontSize(context)
        }

        private fun calculateFontSize(context: Context) {
            val typedArrayObtainTypedArray =
                context.resources.obtainTypedArray(R.array.font_points)
            _fontDimension = typedArrayObtainTypedArray.getResourceId(_fontDimensionIndex, 0)
            typedArrayObtainTypedArray.recycle()
        }

        fun cleanTakenObjectList() {
            _TookObjects = HashMap<PlayerObject?, PlayerObject?>()
        }

        fun addTakenObject(playerObject: PlayerObject?) {
            _TookObjects!![playerObject] = playerObject
        }

        fun isObjectTaken(playerObject: PlayerObject?): Boolean {
            return _TookObjects!!.containsKey(playerObject)
        }

        fun getLanguage(): String {
            var string: String? = SharedPreferencesManager.language
            if (string == null) {
                string = Locale.getDefault().language
                if (string != this.appContext!!.getString(R.string.language_codeEN) &&
                    string != this.appContext!!.getString(
                        R.string.language_codeIT
                    )
                ) {
                    string = this.appContext!!.getString(R.string.language_codeIT)
                }
                setLanguage(string)
            }
            language = string
            return string
        }

        fun setLanguageLabel(str: String?) {
            var str = str
            if (str == null) {
                str = getLanguage()
            }
            val string: String? = when (str) {
                this.appContext!!.getString(R.string.language_codeIT) -> {
                    this.appContext!!.getString(R.string.language_it)
                }

                else -> {
                    if (str == this.appContext!!.getString(R.string.language_codeEN))
                        this.appContext!!.getString(
                        R.string.language_en
                    ) else null
                }
            }
            this._pf.findPreference(GameConstants.PREFERENCE_LANGUAGE).summary = string
        }
        fun setFontNameLabel() {
            val string: String?
            if (_fontName.equals(this.appContext!!.getString(R.string.fnt_font00))) {
                string = this.appContext!!.getString(R.string.fnt_fontName00)
            } else if (_fontName.equals(this.appContext!!.getString(R.string.fnt_font01))) {
                string = this.appContext!!.getString(R.string.fnt_fontName01)
            } else if (_fontName.equals(this.appContext!!.getString(R.string.fnt_font02))) {
                string = this.appContext!!.getString(R.string.fnt_fontName02)
            } else if (_fontName.equals(this.appContext!!.getString(R.string.fnt_font03))) {
                string = this.appContext!!.getString(R.string.fnt_fontName03)
            } else if (_fontName.equals(this.appContext!!.getString(R.string.fnt_font04))) {
                string = this.appContext!!.getString(R.string.fnt_fontName04)
            } else {
                string =
                    if (_fontName.equals(
                            this.appContext!!.getString(R.string.fnt_font05)))
                        this.appContext!!.getString(
                        R.string.fnt_fontName05
                    ) else null
            }
            this._pf.findPreference(GameConstants.PREFERENCE_FONT_NAME).summary = string
        }

        fun setFontSizeLabel() {
            val fontSize: Int = getFontSize()
            val typedArrayObtainTypedArray: TypedArray =
                this.appContext!!.resources.obtainTypedArray(R.array.font_dimensionText)
            this._pf.findPreference(GameConstants.PREFERENCE_TEXT_SIZE)
                .setSummary(typedArrayObtainTypedArray.getResourceId(fontSize, 0))
            typedArrayObtainTypedArray.recycle()
        }

        fun setPreferencesFragment(fragment: SettingsActivity.SettingsFragment) {
            this._pf = fragment
        }
    }

}
