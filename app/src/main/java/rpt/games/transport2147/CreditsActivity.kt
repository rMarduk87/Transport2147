package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityCreditsBinding
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic


class CreditsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreditsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityCredits)
        GameLogic.checkForAppRecovery(this, true)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        try {
            super.onResume()
            loadCredits()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
    
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(onAttach(context))
    }

    fun onChangeFontSize(view: View?) {
        try {
            GameLogic.changeFontSize(this, view!!)
            loadCredits()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun loadCredits() {
        try {
            val linearLayout = findViewById<View?>(R.id.frgChapter_layContent) as LinearLayout
            linearLayout.removeAllViews()
            ChapterFormatter().formatChapter(linearLayout, this,
                GameConstants.BOOK_CHAPTER_CREDITS)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}