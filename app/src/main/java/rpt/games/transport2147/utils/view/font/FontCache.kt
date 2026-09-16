package rpt.games.transport2147.utils.view.font

import android.content.Context
import android.graphics.Typeface
import rpt.com.base.log.e
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.AppUtils
import java.util.Hashtable


object FontCache {
    private val fontCache: Hashtable<String?, Typeface?> = Hashtable<String?, Typeface?>()

    fun get(str: String, context: Context?): Typeface? {
        var str = str
        if (str == context?.getString(R.string.fnt_FontCore)) {
            str = AppUtils.FONT_NAME
        }
        var typefaceCreateFromAsset: Typeface? = fontCache[str]
        if (typefaceCreateFromAsset == null) {
            try {
                typefaceCreateFromAsset = Typeface.createFromAsset(context?.assets, str)
                fontCache[str] = typefaceCreateFromAsset
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
                return null
            }
        }
        return typefaceCreateFromAsset
    }
}