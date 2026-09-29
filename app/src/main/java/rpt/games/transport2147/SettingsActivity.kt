package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivitySettingsBinding
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.view.game.GameLogic

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "TERMINAL // OPZIONI DI SISTEMA"

        GameLogic.checkForAppRecovery(this, false)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(onAttach(context))
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            try {
                val activityContext = requireActivity()
                GameLogic.checkForAppRecovery(activityContext, true)
                setPreferencesFromResource(R.xml.preferences, rootKey)
                GameLogic.setPreferencesFragment(this)

                val prefTextSize = findPreference<androidx.preference.ListPreference>("pref_textSize")
                prefTextSize?.value = GameLogic.getFontSize().toString()

                prefTextSize?.setOnPreferenceChangeListener { _, newValue ->
                    val newSize = newValue.toString().toIntOrNull() ?: 3
                    GameLogic.setFontSize(newSize)
                    GameLogic.changeFontSize(activityContext, newSize)
                    GameLogic.setFontSizeLabel()
                    true
                }

                val prefLanguage = findPreference<androidx.preference.ListPreference>("pref_language")
                prefLanguage?.value = GameLogic.getLanguage(activityContext)

                prefLanguage?.setOnPreferenceChangeListener { _, newValue ->
                    val newLang = newValue.toString()
                    rpt.games.transport2147.utils.managers.SharedPreferencesManager.language = newLang
                    GameLogic.setLanguage(newLang)
                    GameLogic.setLanguageLabel(newLang)
                    true
                }

                val prefFontName = findPreference<androidx.preference.ListPreference>("pref_fontName")
                prefFontName?.value = rpt.games.transport2147.utils.managers.
                SharedPreferencesManager.coreFontName ?: getString(R.string.fnt_font00)

                prefFontName?.setOnPreferenceChangeListener { _, newValue ->
                    val newFont = newValue.toString()
                    rpt.games.transport2147.utils.managers.SharedPreferencesManager.coreFontName = newFont
                    GameLogic.setCoreFont(context, newFont)
                    GameLogic.setFontNameLabel()
                    true
                }

                GameLogic.setFontSizeLabel()
                GameLogic.setFontNameLabel()
                GameLogic.setLanguageLabel(null)

            } catch (e: Exception) {
                e(Throwable(e), "Errore caricamento impostazioni")
            }
        }
    }
}