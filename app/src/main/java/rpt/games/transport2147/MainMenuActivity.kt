package rpt.games.transport2147

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityMainMenuBinding
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper
import rpt.games.transport2147.utils.data.appmodels.complex.Profile
import rpt.games.transport2147.utils.data.appmodels.complex.History.lastChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.requestedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.size
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.managers.DialogManager
import rpt.games.transport2147.utils.managers.ProfileManager
import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import rpt.games.transport2147.utils.managers.SocialManager
import rpt.games.transport2147.utils.view.game.GameLogic


class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityMain)
        GameLogic.checkForAppRecovery(this, false)
        GameLogic.mainMenu = this
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val versione = getString(R.string.info_appVersion)
        binding.actMainTxtVersion.text = buildString {
            append(versione)
            append(" ")
            append(AppUtils.getAppVersion(this@MainMenuActivity))
            append(" ")
            append(getString(R.string.param_buildVariant))
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val mostRecentProfile: Profile? = BookManager.getMostRecentProfile()
            withContext(Dispatchers.Main) {
                if (mostRecentProfile != null) {
                    ProfileManager.loadProfile(this@MainMenuActivity, mostRecentProfile)
                } else {
                    profileLoaded(false, null)
                }
            }
        }

        if (SharedPreferencesManager.language
                .equals(getString(R.string.language_codeIT)) &&
            !SharedPreferencesManager.initialPopUpShow
                .equals(getString(R.string.transport1533islive))
        ) {
            SharedPreferencesManager.initialPopUpShow =
                getString(R.string.transport1533islive)
            DialogManager.showGenericOkCancelDialog(
                this@MainMenuActivity,
                getString(R.string.dialog_alert),
                getString(R.string.dialog_Transport1533IsLive),
                getString(R.string.dialog_noThanks),
                null,
                getString(R.string.dialog_goToTransport1533)
            ) {
                SocialManager.openGooglePlay(
                    this@MainMenuActivity,
                    this@MainMenuActivity.getString(
                        R.string.appid_transport1533
                    )
                )
            }
        }
        AppUtils.makeActionOverflowMenuShown(this@MainMenuActivity)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return true
        }
    }
    
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        try {
            when (menuItem.itemId) {
                R.id.mnuMainMenu_itmCredit -> startActivity(
                    Intent(
                        this,
                        CreditsActivity::class.java as Class<*>
                    )
                )

                R.id.mnuMainMenu_itmExit -> Process.killProcess(Process.myPid())
                R.id.mnuMainMenu_itmFastStart -> DialogManager.showFastStartDialog(this){}
                R.id.mnuMainMenu_itmGlossary -> {
                    val intent = Intent(this, GlossaryActivity::class.java as Class<*>)
                    intent.putExtra(GameConstants.GLOSSARY_HOME, "MAIN")
                    startActivity(intent)
                }

                R.id.mnuMainMenu_itmNewGame -> DialogManager.confirmNewGame(this)
                R.id.mnuMainMenu_itmSettings -> startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java as Class<*>
                    )
                )
            }
            return true
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return true
        }
    }

    override fun onDestroy() {
        try {
            GameLogic.mainMenu = null
            super.onDestroy()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    override fun onStart() {
        super.onStart()
        setStartButtonText()
    }


    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(context!!))
    }

    fun onClickMainActivity(view: View) {
        try {
            when (view.id) {
                R.id.actMain_btnIntro -> {
                    SharedPreferencesManager.showIntro = true
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java as Class<*>
                        )
                    )
                }

                R.id.actMain_btnMain2 -> startActivity(
                    Intent(
                        this,
                        MainActivity2::class.java as Class<*>
                    )
                )

                R.id.actMain_btnProfiles -> DialogManager.showProfilesInterface(this)
                R.id.actMain_btnRules -> {
                    requestedChapter = GameConstants.BOOK_CHAPTER_RULES
                    startActivity(Intent(this, NavigatorActivity::class.java as Class<*>))
                }

                R.id.actMain_btnStart -> {
                    requestedChapter =
                        if (size != 0) lastChapter!!.chapter else GameConstants.BOOK_CHAPTER_START
                    startActivity(Intent(this, NavigatorActivity::class.java as Class<*>))
                }

                R.id.actMain_btnTutorial -> {
                    val intent = Intent(this, TutorialActivity::class.java as Class<*>)
                    intent.putExtra(GameConstants.TUTORIAL_HOME, "MAIN")
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun profileLoaded(z: Boolean, str: String?) {
        var str = str
        if (!z) {
            try {
                str = getString(R.string.txt_noOne)
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
                return
            }
        }
        binding.actMainTxtProfile.text = str
        binding.actMainBtnStart.isEnabled = z
        binding.actMainBtnRules.isEnabled = z
        setStartButtonText()
    }

    fun setStartButtonText() {
        try {
            if (size == 0 || lastChapter!!.chapter == GameConstants.BOOK_CHAPTER_START) {
                binding.actMainBtnStart.text = getString(R.string.button_GoNavigatorVanilla)
            } else {
                binding.actMainBtnStart.text = getString(R.string.button_GoNavigator)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}
