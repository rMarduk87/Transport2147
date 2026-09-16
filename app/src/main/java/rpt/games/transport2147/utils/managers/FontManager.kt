package rpt.games.transport2147.utils.managers

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.view.font.FontCache
import androidx.core.content.withStyledAttributes

object FontManager {
    fun setCustomFont(textView: TextView, context: Context?, attributeSet: AttributeSet?) {
        if (context == null || attributeSet == null) return

        context.withStyledAttributes(attributeSet, R.styleable.CustomFont) {

            val fontString = getString(R.styleable.CustomFont_fontName)

            setCustomFont(textView, fontString, context)
        }
    }

    fun setCustomFont(textView: TextView, str: String?, context: Context?) {
        if (str == null || context == null) return

        val typeface = FontCache.get(str, context)
        if (typeface != null) {
            textView.typeface = typeface
        }
    }
}