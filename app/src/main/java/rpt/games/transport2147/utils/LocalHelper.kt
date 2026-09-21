package rpt.games.transport2147.utils


import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import rpt.games.transport2147.utils.view.game.GameLogic
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale


object LocaleHelper {
    fun onAttach(context: Context): Context? {
        val language: String = GameLogic.getLanguage()
        return setLocale(context, language)
    }

    fun onAttach(context: Context, str: String?): Context? {
        val language: String = GameLogic.getLanguage()
        return setLocale(context, language)
    }

    private fun getPersistedData(context: Context?, str: String?): String {
        SharedPreferencesManager.language = str
        return str!!
    }

    private fun setLocale(context: Context, str: String?): Context? {
        return updateResources(context, str)
    }

    private fun updateResources(context: Context, str: String?): Context? {
        val locale: Locale = Locale(str!!)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, str: String?): Context {
        val locale: Locale = Locale(str!!)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}