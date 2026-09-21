package rpt.games.transport2147

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityMain2Binding
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.managers.SocialManager
import rpt.games.transport2147.utils.view.game.GameLogic


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityMain2);
        GameLogic.checkForAppRecovery(this, false)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(onAttach(context))
    }


    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
    
    override fun onStart() {
        super.onStart()
    }

    fun onClickMainActivity(view: View) {
        try {
            val id = view.id
            if (id != R.id.actMain_btnTutorial) {
                when (id) {
                    R.id.actMain2_btnBack -> finish()
                    R.id.actMain2_btnDonate -> SocialManager.openUrl(
                        this,
                        getString(R.string.url_donate)
                    )
                    R.id.actMain2_btnLike -> SocialManager.openUrl(this, getString(R.string.url_like))
                    R.id.actMain2_btnShare -> SocialManager.sendGeneric(
                        this,
                        getString(
                            R.string.msg_share,
                            arrayOf<Any?>(getString(R.string.url_playstore))
                        )
                    )
                    R.id.actMain2_btnShareWa -> SocialManager.sendToWhatsApp(
                        this,
                        getString(
                            R.string.msg_share,
                            arrayOf<Any?>(getString(R.string.url_playstore))
                        )
                    )
                }
            } else {
                val intent = Intent(this, TutorialActivity::class.java as Class<*>)
                intent.putExtra(GameConstants.TUTORIAL_HOME, "MAIN")
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}