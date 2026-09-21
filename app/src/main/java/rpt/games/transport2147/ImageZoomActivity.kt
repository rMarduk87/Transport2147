package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityImageZoomBinding
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.managers.ImageManager
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.view.image.TouchImageView


class ImageZoomActivity : AppCompatActivity() {
    private lateinit var binding : ActivityImageZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityImageZoom)
        GameLogic.checkForAppRecovery(this, true)
        binding = ActivityImageZoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val touchImageView: TouchImageView =
            findViewById<View?>(R.id.dlgZoom_imgDisplay) as TouchImageView
        touchImageView.setImageDrawable(
            intent.extras!!.getString(GameConstants.IMAGE_ZOOM_SOURCE)?.let {
                ImageManager.getDrawableFromAsset(
                    this,
                    it
                )
            }
        )
        touchImageView.setMaxZoom(8.0f)
    }

    // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(onAttach(context))
    }

    fun onCloseImage(view: View?) {
        try {
            finish()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}