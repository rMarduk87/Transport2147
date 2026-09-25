package rpt.games.transport2147.utils.view.text

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Layout
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import rpt.games.transport2147.R

class RPTextView2 : RPTextView {

    private var _justify: Boolean = false

    constructor(context: Context) : super(context) {
        customInit(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet?) :
            super(context, attributeSet) {
        customInit(context, attributeSet)
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attributeSet, defStyleAttr) {
        customInit(context, attributeSet)
    }

    @SuppressLint("CustomViewStyleable")
    private fun customInit(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        if (attributeSet != null) {
            context.withStyledAttributes(
                attributeSet,
                R.styleable.CustomJustification
            ) {
                _justify = getBoolean(0, false)
            }
        }

        super.setMovementMethod(LinkMovementMethod())

        applyJustification()
    }

    var justification: Boolean
        get() = _justify
        set(value) {
            _justify = value
            applyJustification()
        }

    private fun applyJustification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            justificationMode =
                if (_justify) {
                    Layout.JUSTIFICATION_MODE_INTER_WORD
                } else {
                    Layout.JUSTIFICATION_MODE_NONE
                }
        }
    }
}