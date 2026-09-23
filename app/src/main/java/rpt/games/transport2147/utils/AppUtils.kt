package rpt.games.transport2147.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import rpt.com.base.log.e
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor


class AppUtils {

    companion object{
        const val USERS_SHARED_PREF : String = "user_pref"
        const val SHOW_INTRO : String = "showIntro"
        const val FONT_SIZE : String = "fontSize"
        const val TEXT_JUSTIFICATION : String = "text_justification"
        const val CORE_FONT_NAME : String = "core_font_name"
        const val LANGUAGE : String = "language"
        const val INITIAL_POPUP_SHOW : String = "initial_popup_show"
        var FONT_NAME : String = ""


        fun execSingleRegex(str: String?, str2: String?): String? {
            val matcher: Matcher = Pattern.compile(str2, Pattern.DOTALL).matcher(str)
            return if (matcher.find()) matcher.group() else ""
        }

        fun execSingleRegex(str: String?, str2: String?, i: Int): String? {
            val matcher: Matcher = Pattern.compile(str2, Pattern.DOTALL).matcher(str)
            return if (matcher.find()) matcher.group(i) else ""
        }

        fun execMultiMatchRegularExpression(
            str: String,
            str2: String,
            str3: String
        ): MutableMap<String, String> {
            val matcher =
                Pattern.compile("<$str\\s+.*?$str2\\s*=\"(.*?)\"\\s*.*?>.*?</$str>", Pattern.DOTALL)
                    .matcher(str3)
            val map: HashMap<String, String> = HashMap<String, String>()
            while (matcher.find()) {
                map[matcher.group(1)] = matcher.group()
            }
            return map
        }

        @SuppressLint("SimpleDateFormat")
        fun getTimestamp(): String {
            return SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
        }

        @SuppressLint("NewApi")
        fun generateViewId(): Int {
            return View.generateViewId()
        }

        fun execReplace(str: String, str2: String, str3: String): String {
            return Pattern.compile(str2).matcher(str).replaceAll(str3)
        }

        fun getDisplayMetrics(context: Context): DisplayMetrics {
            val defaultDisplay =
                (context.getSystemService("window") as WindowManager).defaultDisplay
            val displayMetrics = DisplayMetrics()
            defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics
        }

        fun extendedTrim(str: String): String {
            var cCharAt: Char
            var length = str.length - 1
            var i = 0
            while (i < str.length && ((str[i].also {
                    cCharAt = it
                }) == ' ' || cCharAt == '\n' || cCharAt == '\r' || cCharAt == '\t')) {
                i++
            }
            if (i == str.length) {
                return ""
            }
            while (true) {
                val cCharAt2 = str[length]
                if (cCharAt2 != ' ' && cCharAt2 != '\n' && cCharAt2 != '\r' && cCharAt2 != '\t') {
                    return str.substring(i, length + 1)
                }
                length--
            }
        }

        fun getAppVersion(context: Context): String? {
            try {
                return context.packageManager
                    .getPackageInfo(context.packageName, 0).versionName
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
            return null
        }

        @SuppressLint("SoonBlockedPrivateApi")
        @Throws(java.lang.Exception::class)
        fun makeActionOverflowMenuShown(context: Context) {
            var declaredField: Field? = null
            val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
            if (!viewConfiguration.hasPermanentMenuKey() || (ViewConfiguration::class.java.getDeclaredField(
                    "sHasPermanentMenuKey"
                ).also { declaredField = it }) == null
            ) {
                return
            }
            declaredField!!.isAccessible = true
            declaredField.setBoolean(viewConfiguration, false)
        }

        fun random(i: Int, i2: Int): Int {
            return (floor(Math.random() * (((i2 - i) + 1).toDouble())).toInt()) + i
        }

        fun getDeviceType(context: Context): Int {
            val configuration: Configuration = context.resources.configuration
            if (configuration.screenWidthDp >= 720 || configuration.screenHeightDp >= 720) {
                return 2
            }
            return if (configuration.screenWidthDp >= 600 || configuration.screenHeightDp >= 600) 1
            else 0
        }

    }
    val hackRegex = Regex("""(\([^\w()]*\)|\[[^\w\[\]]*\]|\{[^\w{}]*\}|<[^\w<>]*>)""")


}