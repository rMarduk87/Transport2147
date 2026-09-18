package rpt.games.transport2147

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import rpt.games.transport2147.databinding.ActivityMainMenuBinding
import rpt.games.transport2147.utils.data.appmodels.complex.History.lastChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.requestedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.size


class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityMain);
        SharedObjects.checkForAppRecovery(this, false);
        SharedObjects.MainMenu = this;
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (findViewById<View?>(R.id.actMain_txtVersion) as TextView).setText(
            getString(
                R.string.info_appVersion,
                arrayOf<Any?>(Util.getAppVersion(this), getString(R.string.param_buildVariant))
            )
        )
        val mostRecentProfile: Profile? = SharedObjects.getDbMgr(this).getMostRecentProfile()
        if (mostRecentProfile == null) {
            profileLoaded(false, null)
            DialogMgr.showFastStartDialog(this)
        } else {
            ProfileMgr.loadProfile(this, mostRecentProfile)
            if (SharedObjects.Preference.getLanguage()
                    .equals(getString(R.string.language_codeIT)) && !SharedObjects.Preference.getInitialPopupShown()
                    .equals("MEGERAISLIVE")
            ) {
                SharedObjects.Preference.setInitialPopupShown("MEGERAISLIVE")
                DialogMgr.showGenericOkCancelDialog(
                    this,
                    getString(R.string.dialog_alert),
                    getString(R.string.dialog_MegeraIsLive),
                    getString(R.string.dialog_noThanks),
                    null,
                    getString(R.string.dialog_goToMegera),
                    object : View.OnClickListener {
                        // from class: it.tenebraeabisso.tenebra1.Activity_MainMenu.1
                        // android.view.View.OnClickListener
                        override fun onClick(view: View?) {
                            SocialMgr.openGooglePlay(
                                this,
                                this@Activity_MainMenu.getString(R.string.appid_megera)
                            )
                        }
                    })
            }
        }
        UiUtil.makeActionOverflowMenuShown(this)
    }

    // android.app.Activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            getMenuInflater().inflate(R.menu.menu_main, menu)
            return true
        } catch (e: Exception) {
            ExceptionMgr.genericException(
                getString(R.string.error_navigator),
                Util.getClassMethod(),
                e,
                this
            )
            return true
        }
    }

    // android.app.Activity
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        try {
            when (menuItem.getItemId()) {
                R.id.mnuMainMenu_itmCredit -> startActivity(
                    Intent(
                        this,
                        Activity_Credits::class.java as Class<*>
                    )
                )

                R.id.mnuMainMenu_itmExit -> Process.killProcess(Process.myPid())
                R.id.mnuMainMenu_itmFastStart -> DialogMgr.showFastStartDialog(this)
                R.id.mnuMainMenu_itmGlossary -> {
                    val intent = Intent(this, Activity_Glossary::class.java as Class<*>)
                    intent.putExtra(Constants.GLOSSARY_HOME, "MAIN")
                    startActivity(intent)
                }

                R.id.mnuMainMenu_itmNewGame -> DialogMgr.confirmNewGame(this)
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
            ExceptionMgr.genericException(
                getString(R.string.error_actionmenu),
                Util.getClassMethod(),
                e,
                this
            )
            return true
        }
    }

    // android.app.Activity
    override fun onDestroy() {
        try {
            SharedObjects.MainMenu = null
            super.onDestroy()
        } catch (e: Exception) {
            ExceptionMgr.genericException(
                getString(R.string.error_mainmenu),
                Util.getClassMethod(),
                e,
                this
            )
        }
    }

    // android.app.Activity
    override fun onStart() {
        super.onStart()
        setStartButtonText()
    }

    // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(context))
    }

    fun onClickMainActivity(view: View) {
        try {
            when (view.getId()) {
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

                R.id.actMain_btnProfiles -> DialogMgr.showProfilesInterface(this)
                R.id.actMain_btnRules -> {
                    requestedChapter = Constants.BOOK_CHAPTER_RULES
                    startActivity(Intent(this, Activity_Navigator::class.java as Class<*>))
                }

                R.id.actMain_btnStart -> {
                    requestedChapter =
                        if (size != 0) lastChapter!!.chapter else Constants.BOOK_CHAPTER_START
                    startActivity(Intent(this, Activity_Navigator::class.java as Class<*>))
                }

                R.id.actMain_btnTutorial -> {
                    val intent = Intent(this, Activity_Tutorial::class.java as Class<*>)
                    intent.putExtra(Constants.TUTORIAL_HOME, "MAIN")
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            ExceptionMgr.genericException(
                getString(R.string.error_mainmenu),
                Util.getClassMethod(),
                e,
                this
            )
        }
    }

    fun profileLoaded(z: Boolean, str: String?) {
        var str = str
        if (!z) {
            try {
                str = getString(R.string.txt_noOne)
            } catch (e: Exception) {
                ExceptionMgr.genericException(
                    getString(R.string.error_mainmenu),
                    Util.getClassMethod(),
                    e,
                    this
                )
                return
            }
        }
        (SharedObjects.MainMenu.findViewById(R.id.actMain_txtProfile) as TextView).setText(str)
        findViewById<View?>(R.id.actMain_btnStart).setEnabled(z)
        findViewById<View?>(R.id.actMain_btnRules).setEnabled(z)
        setStartButtonText()
    }

    fun setStartButtonText() {
        try {
            val textView = findViewById<View?>(R.id.actMain_btnStart) as TextView
            if (size == 0 || lastChapter!!.chapter == Constants.BOOK_CHAPTER_START) {
                textView.setText(getString(R.string.button_GoNavigatorVanilla))
            } else {
                textView.setText(getString(R.string.button_GoNavigator))
            }
        } catch (e: Exception) {
            ExceptionMgr.genericException(
                getString(R.string.error_mainmenu),
                Util.getClassMethod(),
                e,
                this
            )
        }
    }
}