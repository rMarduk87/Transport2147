package rpt.games.transport2147

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import rpt.games.transport2147.databinding.ActivityLaunchBinding
import rpt.games.transport2147.utils.managers.BookManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class LaunchActivity  : AppCompatActivity() {

    private lateinit var binding : ActivityLaunchBinding

    var handler: Handler? = null
    var runnable: Runnable? = null
    var millisecond: Int = 1100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("UnsafeIntentLaunch")
    override fun onResume() {
        super.onResume()


        runnable = Runnable {
            openBook()
            val  intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        handler = Handler(Looper.getMainLooper())
        handler!!.postDelayed(runnable!!, millisecond.toLong())
    }

    private fun openBook() {
        lifecycleScope.launch(Dispatchers.IO) {
            BookManager.openBook()
        }

    }

}