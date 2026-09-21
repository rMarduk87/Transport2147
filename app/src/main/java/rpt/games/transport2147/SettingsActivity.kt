package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivitySettingsBinding
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.view.game.GameLogic


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activitySettings)
        GameLogic.checkForAppRecovery(this, false)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment())
            .commit()
    }
    
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(onAttach(context))
    }
    
    class SettingsFragment : PreferenceFragment() {
        @Deprecated("Deprecated in Java")
        override fun onCreate(bundle: Bundle?) {
            try {
                super.onCreate(bundle)
                GameLogic.checkForAppRecovery(activity!!, true)
                addPreferencesFromResource(R.xml.preferences)
                GameLogic.setPreferencesFragment(this)
                GameLogic.setFontSizeLabel()
                GameLogic.setFontNameLabel()
                GameLogic.setLanguageLabel(null)
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
        }

    }
}