package rpt.games.transport2147

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityMainMenuBinding
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper
import rpt.games.transport2147.utils.data.appmodels.Profile
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
        GameLogic.MainMenu = this
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (findViewById<View?>(R.id.actMain_txtVersion) as TextView).text = getString(
            R.string.info_appVersion,
            arrayOf<Any?>(AppUtils.getAppVersion(this),
                getString(R.string.param_buildVariant))
        )
        val mostRecentProfile: Profile = BookManager.getMostRecentProfile()
        ProfileManager.loadProfile(this, mostRecentProfile)
        if (SharedPreferencesManager.language
                .equals(getString(R.string.language_codeIT)) &&
            !SharedPreferencesManager.initialPopUpShow
                .equals(getString(R.string.transport1533islive))
        ) {
            SharedPreferencesManager.initialPopUpShow =
                getString(R.string.transport1533islive)
            DialogManager.showGenericOkCancelDialog(
                this,
                getString(R.string.dialog_alert),
                getString(R.string.dialog_Transport1533IsLive),
                getString(R.string.dialog_noThanks),
                null,
                getString(R.string.dialog_goToTransport1533),
                object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        SocialManager.openGooglePlay(
                            this,
                            this@MainMenuActivity.getString(
                                R.string.appid_transport1533)
                        )
                    }
                })
        }
        AppUtils.makeActionOverflowMenuShown(this)
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
                        Activity_Credits::class.java as Class<*>
                    )
                )

                R.id.mnuMainMenu_itmExit -> Process.killProcess(Process.myPid())
                R.id.mnuMainMenu_itmFastStart -> DialogManager.showFastStartDialog(this)
                R.id.mnuMainMenu_itmGlossary -> {
                    val intent = Intent(this, Activity_Glossary::class.java as Class<*>)
                    intent.putExtra(GameConstants.GLOSSARY_HOME, "MAIN")
                    startActivity(intent)
                }

                R.id.mnuMainMenu_itmNewGame -> DialogManager.confirmNewGame(this)
                R.id.mnuMainMenu_itmRemote -> startActivity(
                    Intent(
                        this,
                        Activity_Remote::class.java as Class<*>
                    )
                )

                R.id.mnuMainMenu_itmSettings -> startActivity(
                    Intent(
                        this,
                        Activity_Settings::class.java as Class<*>
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
            GameLogic.MainMenu = null
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
                R.id.actMain_btnIntro -> startActivity(
                    Intent(
                        this,
                        Activity_Intro::class.java as Class<*>
                    )
                )

                R.id.actMain_btnMain2 -> startActivity(
                    Intent(
                        this,
                        Activity_Main2::class.java as Class<*>
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
                    val intent = Intent(this, Activity_Tutorial::class.java as Class<*>)
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
        (GameLogic.MainMenu!!.findViewById<TextView>(R.id.actMain_txtProfile)!!).text = str
        findViewById<View?>(R.id.actMain_btnStart).isEnabled = z
        findViewById<View?>(R.id.actMain_btnRules).isEnabled = z
        setStartButtonText()
    }

    fun setStartButtonText() {
        try {
            val textView = findViewById<View?>(R.id.actMain_btnStart) as TextView
            if (size == 0 || lastChapter!!.chapter == GameConstants.BOOK_CHAPTER_START) {
                textView.text = getString(R.string.button_GoNavigatorVanilla)
            } else {
                textView.text = getString(R.string.button_GoNavigator)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}