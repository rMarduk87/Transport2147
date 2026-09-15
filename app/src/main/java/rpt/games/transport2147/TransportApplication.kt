package rpt.games.transport2147

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class TransportApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    companion object {

        private lateinit var _instance: TransportApplication

        val instance: TransportApplication
            get() {
                return _instance
            }
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
        //Init log
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        }
    }
}