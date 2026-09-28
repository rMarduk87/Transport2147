package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.FragmentChapterBinding
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic
import androidx.core.view.size
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.view.text.RPTextView2


class IntroActivity : AppCompatActivity() {
    private lateinit var binding : FragmentChapterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityIntro);
        GameLogic.checkForAppRecovery(this, true);
        binding = FragmentChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    
    override fun onResume() {
        try {
            super.onResume()
            loadIntro()
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
            loadIntro()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun onSwitchJustification(view: View?) {
        val z: Boolean = !SharedPreferencesManager.textJustification
        SharedPreferencesManager.textJustification = z
        GameLogic.iconToggleJustification!!.getDrawable().setLevel(if (z) 2 else 1)
        val linearLayout = findViewById<View?>(R.id.frgChapter_layContent) as LinearLayout
        for (i in 0..<linearLayout.size) {
            val childAt = linearLayout.getChildAt(i)
            if (childAt is RPTextView2) {
                (childAt as RPTextView2).justification = z
            }
        }
    }

    /*fun loadIntro() {
        try {
            val linearLayout = findViewById<View?>(R.id.frgChapter_layContent) as LinearLayout
            linearLayout.removeAllViews()
            GameLogic.iconToggleJustification =
                findViewById<View?>(R.id.incFontChgr_btnSwitchJustification) as ImageView?
            GameLogic.iconToggleJustification!!.setVisibility(View.VISIBLE)
            GameLogic.iconToggleJustification!!.getDrawable()
                .setLevel(if (SharedPreferencesManager.textJustification) 2 else 1)
            ChapterFormatter().formatChapter(linearLayout, this,
                GameConstants.BOOK_CHAPTER_INTRO)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }*/

    fun loadIntro() {
        try {
            val linearLayout = findViewById<View?>(R.id.frgChapter_layContent) as LinearLayout
            linearLayout.removeAllViews()

            GameLogic.iconToggleJustification = findViewById<View?>(R.id.incFontChgr_btnSwitchJustification) as ImageView?
            GameLogic.iconToggleJustification?.visibility = View.VISIBLE

            // Usa il "?.let" per evitare il crash se il Drawable vettoriale non supporta i livelli
            try {
                GameLogic.iconToggleJustification?.drawable?.level = if (SharedPreferencesManager.textJustification) 2 else 1
            } catch (e: Exception) {
                android.util.Log.w("CYBER_DEBUG", "Impossibile impostare il livello dell'icona")
            }

            // Se il testo continua a tagliarsi, l'errore verrà stampato nel Logcat!
            ChapterFormatter().formatChapter(linearLayout, this, GameConstants.BOOK_CHAPTER_INTRO)

        } catch (e: Exception) {
            // STAMPA L'ERRORE REALE NEL LOGCAT INVECE DI NASCONDERLO
            android.util.Log.e("CYBER_DEBUG", "CRASH DURANTE IL CARICAMENTO DEL CAPITOLO:", e)
        }
    }
}