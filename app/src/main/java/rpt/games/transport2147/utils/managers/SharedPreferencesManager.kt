package rpt.games.transport2147.utils.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import rpt.games.transport2147.TransportApplication
import rpt.games.transport2147.utils.AppUtils

object SharedPreferencesManager {
    private val ctx: Context
        get() = TransportApplication.instance

    private fun createSharedPreferences(): SharedPreferences {
        return ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    private val sharedPreferences by lazy { createSharedPreferences() }

    var showIntro: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SHOW_INTRO, true)
        set(value) = sharedPreferences.edit { putBoolean(AppUtils.SHOW_INTRO, value) }

}