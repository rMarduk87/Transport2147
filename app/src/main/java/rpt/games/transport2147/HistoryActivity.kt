package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityHistoryBinding
import rpt.games.transport2147.utils.LocaleHelper
import rpt.games.transport2147.utils.data.appmodels.complex.History
import rpt.games.transport2147.utils.data.appmodels.complex.History.historyEnabled
import rpt.games.transport2147.utils.managers.ToastManager
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic


class HistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityHistory);
        GameLogic.checkForAppRecovery(this, true);
        GameLogic.history = this;
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar!!.setDisplayHomeAsUpEnabled(true);
    }

    override fun onDestroy() {
        try {
            GameLogic.history = null
            super.onDestroy()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    override fun onResume() {
        try {
            super.onResume()
            loadHistory()
            if (historyEnabled) {
                historyEnabled = false
                ToastManager.showGenericToast(this, getString(
                    R.string.toast_historyDisabled))
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }


    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(context!!))
    }

    fun onChangeFontSize(view: View?) {
        try {
            GameLogic.changeFontSize(this, view!!)
            loadHistory()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun loadHistory() {
        try {
            val linearLayout = findViewById<View?>(R.id.frgChapter_layContent) as LinearLayout
            linearLayout.removeAllViews()
            ChapterFormatter().formatHistory(linearLayout, this)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}