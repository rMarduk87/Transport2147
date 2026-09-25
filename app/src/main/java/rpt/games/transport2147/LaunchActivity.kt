package rpt.games.transport2147

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rpt.games.transport2147.databinding.ActivityLaunchBinding
import rpt.games.transport2147.utils.managers.BookManager

@SuppressLint("CustomSplashScreen")
class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private var handler: Handler? = null
    private var millisecond: Int = 1100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nasconde la barra di stato
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("UnsafeIntentLaunch")
    override fun onResume() {
        super.onResume()

        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            openBook()
        }, millisecond.toLong())
    }

    private fun openBook() {
        lifecycleScope.launch(Dispatchers.IO) {
            BookManager.openBook()
            withContext(Dispatchers.Main) {
                val intent = Intent(this@LaunchActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}