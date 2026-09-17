package rpt.games.transport2147.utils.view.game

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import rpt.com.base.log.e
import rpt.games.transport2147.utils.data.appmodels.Profile
import rpt.games.transport2147.utils.data.appmodels.complex.Enemy
import rpt.games.transport2147.utils.data.appmodels.complex.History
import rpt.games.transport2147.utils.data.appmodels.complex.PlayerObject
import rpt.games.transport2147.R
import rpt.games.transport2147.ui.navigator.NavigatorFragment
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.managers.ProfileManager
import rpt.games.transport2147.utils.managers.SharedPreferencesManager

object GameLogic {
    var AppContext: Context? = null
    //var ChapterFragment: Activity_Navigator.ChapterFragment? = null
    //var DiceRoller1: DiceRollerMgr? = null
    //var DiceRoller2: DiceRollerMgr? = null
    var Enemy: Enemy? = null
    //var History: Activity_History? = null
    //var MainMenu: Activity_MainMenu? = null
    var navigator: NavigatorFragment? = null
    var PlayerSheet: PlayerSheet? = null
    var Profile: Profile? = null
    private var _TookObjects: HashMap<PlayerObject?, PlayerObject?>? = null

    var _fontDimension: Int = 2130968660
    var _fontDimensionIndex: Int = 2
    var _fontDimensionMaxIndex: Int = 12
    var _fontDimensionMinIndex: Int = 0
    var _fontName: String? = null
    var language: String? = null
        private set
    var iconToggleHistory: ImageView? = null
    var iconToggleJustification: ImageView? = null
    var remoteToken: String? = null
    var remoteUser: String? = null
    var warning_changeInitialValue: Boolean = false
    var warning_surpassInitialValue: Boolean = false

    fun initApp(context: Context?) {
        AppContext = context
        changeFontSize(context!!, SharedPreferencesManager.fontSize)
        setCoreFont(context, SharedPreferencesManager.coreFontName)
        if (language == null) {
            setLanguage(context, SharedPreferencesManager.language)
        }
    }

    fun checkForAppRecovery(activity: Activity, z: Boolean) {
        var mostRecentProfile: Profile? = null
        try {
            if (History.requestedChapter == null || Profile == null) {
                initApp(activity.getApplicationContext())
                if (!z || (BookManager.getMostRecentProfile()
                        .also { mostRecentProfile = it }) == null
                ) {
                    return
                }
                ProfileManager.loadProfile(activity, mostRecentProfile)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun setLanguage(context: Context?, str: String?) {
        language = str
    }

    fun setCoreFont(context: Context?, str: String?) {
        _fontName = str
    }

    fun changeFontSize(context: Context, view: View) {
        if (view.getId() == R.id.incFontChgr_btnFontIncrease) {
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
            context.getResources().obtainTypedArray(R.array.font_points)
        _fontDimension = typedArrayObtainTypedArray.getResourceId(_fontDimensionIndex, 0)
        typedArrayObtainTypedArray.recycle()
    }

    fun CleanTakenObjectList() {
        _TookObjects = HashMap<PlayerObject?, PlayerObject?>()
    }

    fun AddTakenObject(playerObject: PlayerObject?) {
        _TookObjects!!.put(playerObject, playerObject)
    }

    fun isObjectTaken(playerObject: PlayerObject?): Boolean {
        return _TookObjects!!.containsKey(playerObject)
    }
}