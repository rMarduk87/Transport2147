package rpt.games.transport2147.utils.view.text

import android.content.Context
import android.graphics.Typeface
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.MetricAffectingSpan
import android.util.AttributeSet
import android.util.SparseArray
import rpt.games.transport2147.R
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import androidx.core.content.withStyledAttributes
import androidx.core.util.size
import rpt.com.base.log.e


class RPTextView2 : RPTextView {
    private var SCALE_ZERO: Float
    private var _justify: Boolean?
    private var _justifySpans: SparseArray<JustifySpan>?
    private var _justifySpansEnds: IntArray?
    private var _justifySpansStarts: IntArray?
    private var _originalText: CharSequence?
    private var _prevFakeBold: Boolean
    private var _prevTextScaleX: Float
    private var _prevTextSize: Float
    private var _prevTextWidth: Int
    private var _prevTypeface: Typeface?
    protected var _textChangingForJustification: Boolean
    protected var _textMeasuring: Boolean

    constructor(context: Context) : super(context) {
        this._justify = false
        this._justifySpans = null
        this._justifySpansStarts = null
        this._justifySpansEnds = null
        this.SCALE_ZERO = 0.0f
        this._prevTypeface = null
        this._prevTextSize = 0.0f
        this._prevTextScaleX = 0.0f
        this._prevFakeBold = false
        this._prevTextWidth = 0
        this._originalText = null
        this._textChangingForJustification = false
        this._textMeasuring = false
        customInit(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        this._justify = false
        this._justifySpans = null
        this._justifySpansStarts = null
        this._justifySpansEnds = null
        this.SCALE_ZERO = 0.0f
        this._prevTypeface = null
        this._prevTextSize = 0.0f
        this._prevTextScaleX = 0.0f
        this._prevFakeBold = false
        this._prevTextWidth = 0
        this._originalText = null
        this._textChangingForJustification = false
        this._textMeasuring = false
        customInit(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    ) {
        this._justify = false
        this._justifySpans = null
        this._justifySpansStarts = null
        this._justifySpansEnds = null
        this.SCALE_ZERO = 0.0f
        this._prevTypeface = null
        this._prevTextSize = 0.0f
        this._prevTextScaleX = 0.0f
        this._prevFakeBold = false
        this._prevTextWidth = 0
        this._originalText = null
        this._textChangingForJustification = false
        this._textMeasuring = false
        customInit(context, attributeSet)
    }

    private fun customInit(context: Context, attributeSet: AttributeSet?) {
        this.SCALE_ZERO = if (isInEditMode) 1.0E-4f else 0.0f
        if (attributeSet != null) {
            context.withStyledAttributes(attributeSet, R.styleable.CustomJustification) {
                _justify = getBoolean(0, false)
            }
        }
        super.setMovementMethod(LinkMovementMethod())
    }

    var justification: Boolean?
        get() = this._justify
        set(bool) {
            this._justify = bool
            if (_justify == true) {
                setText(this.text)
            } else if (this._originalText != null) {
                setText(this._originalText)
            }
        }

    var text: CharSequence? = null
        get() = if (this._justify == null || !this._justify!! ||
            this._originalText == null) super.getText() else this._originalText


    protected override fun onTextChanged(charSequence: CharSequence?, i: Int, i2: Int, i3: Int) {
        super.onTextChanged(charSequence, i, i2, i3)
        if (this._justify == null || !this._justify!! || isInEditMode ||
            this._textChangingForJustification) {
            return
        }
        this._textChangingForJustification = true
        this._originalText = charSequence
        initJustificationSpans(charSequence)
        if (layout != null) {
            justifyText()
        }
        this._textChangingForJustification = false
    }

    private fun initJustificationSpans(charSequence: CharSequence?) {
        val spannableStringBuilder = SpannableStringBuilder(charSequence)
        val string = spannableStringBuilder.toString()
        val justifySpanArr = spannableStringBuilder.getSpans<JustifySpan?>(
            0,
            spannableStringBuilder.length,
            JustifySpan::class.java
        ) as Array<JustifySpan?>?
        if (!justifySpanArr.isNullOrEmpty()) {
            for (justifySpan in justifySpanArr) {
                spannableStringBuilder.removeSpan(justifySpan)
            }
        }
        var i = 0
        var z = false
        for (i2 in spannableStringBuilder.indices) {
            val cCharAt = string[i2]
            if (cCharAt == ' ' && !z) {
                i++
                z = true
            } else if (cCharAt != ' ' && z) {
                z = false
            }
        }
        this._justifySpans = SparseArray<JustifySpan>(i)
        this._justifySpansStarts = IntArray(i)
        this._justifySpansEnds = IntArray(i)
        var z2 = false
        var i3 = 0
        for (i4 in spannableStringBuilder.indices) {
            val cCharAt2 = string[i4]
            if (cCharAt2 == ' ' && !z2) {
                i3++
                this._justifySpansStarts!![i3 - 1] = i4
                z2 = true
            } else if (cCharAt2 != ' ' && z2) {
                val i5 = i3 - 1
                this._justifySpansEnds!![i5] = i4
                val justifySpan2 = JustifySpan(1.0f)
                this._justifySpans!!.put(this._justifySpansStarts!![i5], justifySpan2)
                spannableStringBuilder.setSpan(
                    justifySpan2,
                    this._justifySpansStarts!![i5],
                    this._justifySpansEnds!![i5],
                    17
                )
                z2 = false
            }
        }
        text = spannableStringBuilder
    }

    // android.widget.TextView, android.view.View
    protected override fun onMeasure(i: Int, i2: Int) {
        var size: Int = 0
        super.onMeasure(i, i2)
        if (!this._justify!! || this._textMeasuring) {
            return
        }
        val typeface: Typeface? = getTypeface()
        val textSize: Float = getTextSize()
        val textScaleX: Float = getTextScaleX()
        val zIsFakeBoldText: Boolean = paint.isFakeBoldText
        if ((this._prevTypeface === typeface && this._prevTextSize == textSize &&
                    this._prevTextScaleX == textScaleX && this._prevFakeBold == zIsFakeBoldText) ||
            (MeasureSpec.getSize(
                i
            ).also { size = it }) <= 0 || size == this._prevTextWidth
        ) {
            return
        }
        this._prevTypeface = typeface
        this._prevTextSize = textSize
        this._prevTextScaleX = textScaleX
        this._prevFakeBold = zIsFakeBoldText
        this._prevTextWidth = size
        this._textMeasuring = true
        try {
            justifyText()
        } finally {
            this._textMeasuring = false
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x005b  */
    private fun justifyText() {
        var f: Float
        var lineVisibleEnd: Int = 0
        var i: Int = 0
        var i2: Int = 0
        var i3: Int = 0
        var z: Boolean
        var i4: Int = 0
        var desiredWidth: Float
        var c: Char
        var i5: Int = 0
        var iArr: IntArray
        var desiredWidth2: Float
        var c2: Char
        var justifySpan: JustifySpan? = null
        var textViewEx2 = this
        var iArr2 = IntArray(64)
        try {
            val spannable = super.getText() as Spannable
            var length = spannable.length
            if (length == 0) {
                return
            }
            val size = textViewEx2._justifySpans!!.size
            var i6 = 0
            while (true) {
                f = 1.0f
                if (i6 >= size) {
                    break
                }
                textViewEx2._justifySpans!!.valueAt(i6).updateProportion(1.0f)
                i6++
            }
            val layout: Layout = getLayout()
            var lineCount: Int = layout.lineCount
            if (lineCount < 2) {
                return
            }
            var measuredWidth: Int =
                ((measuredWidth - compoundPaddingLeft) - compoundPaddingRight) - 1
            var i7 = 0
            while (i7 < lineCount) {
                val lineStart: Int = layout.getLineStart(i7)
                val lineEnd = if (i7 == lineCount + (-1)) length else layout.getLineEnd(i7)
                if (lineEnd == lineStart || lineEnd == length || spannable[lineEnd - 1] == '\n'
                    || (layout.getLineVisibleEnd(
                        i7
                    ).also { lineVisibleEnd = it }) == lineStart
                ) {
                    iArr = iArr2
                    i = length
                    i2 = lineCount
                    i3 = measuredWidth
                    i5 = i7
                } else {
                    var desiredWidth3: Float = Layout.getDesiredWidth(
                        spannable,
                        lineStart,
                        lineVisibleEnd,
                        layout.paint
                    )
                    val f2 = measuredWidth * f
                    val fFloor = floor((f2 - desiredWidth3).toDouble()).toFloat()
                    if (fFloor > 0.5f) {
                        if (lineVisibleEnd < lineEnd && (textViewEx2._justifySpans!!.get(
                                lineVisibleEnd,
                                null
                            ).also { justifySpan = it }) != null
                        ) {
                            justifySpan!!.updateProportion(textViewEx2.SCALE_ZERO)
                        }
                        var i8 = 0
                        while (i8 < textViewEx2._justifySpansStarts!!.size &&
                            textViewEx2._justifySpansStarts!![i8] < lineStart) {
                            i8++
                        }
                        i = length
                        var i9 = 0
                        var fMeasureText = 0.0f
                        while (textViewEx2._justifySpansEnds!![i8] < lineVisibleEnd) {
                            if (textViewEx2._justifySpansStarts!![i8] != lineStart) {
                                fMeasureText += layout.paint.measureText(
                                    spannable,
                                    textViewEx2._justifySpansStarts!![i8],
                                    textViewEx2._justifySpansEnds!![i8]
                                )
                                iArr2[i9] = textViewEx2._justifySpansStarts!![i8]
                                i9++
                                i8++
                            }
                        }
                        i2 = lineCount
                        i3 = measuredWidth
                        val f3 = (fMeasureText + fFloor) / fMeasureText
                        if (f3 <= SCALE_MAX) {
                            var i10 = 0
                            while (true) {
                                z = true
                                if (i10 < i9) {
                                    textViewEx2._justifySpans!!.get(iArr2[i10])
                                        .updateProportion(f3)
                                    desiredWidth = Layout.getDesiredWidth(
                                        spannable,
                                        lineStart,
                                        lineVisibleEnd,
                                        layout.paint
                                    )
                                    if (desiredWidth > f2) {
                                        if (desiredWidth - f2 <= f2 - desiredWidth3) {
                                            i4 = i10
                                            c = 1.toChar()
                                            break
                                        } else {
                                            textViewEx2._justifySpans!!.get(iArr2[i10])
                                                .updateProportion(1.0f)
                                            i4 = i10 - 1
                                        }
                                    } else if (f2 - desiredWidth < 0.5f) {
                                        i4 = i10
                                        c = 3.toChar()
                                        break
                                    } else {
                                        i10++
                                        desiredWidth3 = desiredWidth
                                    }
                                } else {
                                    i4 = i10
                                    desiredWidth = desiredWidth3
                                }
                                c = 2.toChar()
                                break
                            }
                            if (c.code == 1 || c.code == 2) {
                                if (i4 == i9) {
                                    i4--
                                }
                                var f4 = desiredWidth
                                var c3 = c
                                var i11 = 1
                                while (true) {
                                    var f5 = f4
                                    i5 = i7
                                    val fPow =
                                        (if (c3 == z) -1.0f else 1.0f) * (0.5.pow(i11.toDouble())
                                            .toFloat())
                                    var i12 = 0
                                    while (true) {
                                        if (i12 <= i4) {
                                            val justifySpan2 =
                                                textViewEx2._justifySpans!!.get(iArr2[i4])
                                            val proportion =
                                                justifySpan2.proportion
                                            justifySpan2.updateProportion(proportion +
                                                    ((proportion - 1.0f) * fPow))
                                            desiredWidth2 = Layout.getDesiredWidth(
                                                spannable,
                                                lineStart,
                                                lineVisibleEnd,
                                                layout.paint
                                            )
                                            val f6 = f2 - desiredWidth2
                                            if (f6 !in 0.0f..0.5f) {
                                                iArr = iArr2
                                                if (abs(f6) > abs(f2 - f5)) {
                                                    justifySpan2.updateProportion(proportion)
                                                    c2 = 3.toChar()
                                                } else if (c3.code != 1 || f6 <= 0.5f) {
                                                    if (c3.code == 2 && f6 < 0.0f) {
                                                        c2 = 3.toChar()
                                                        c3 = 1.toChar()
                                                    }
                                                    i12++
                                                    f5 = desiredWidth2
                                                    iArr2 = iArr
                                                    textViewEx2 = this
                                                } else {
                                                    c2 = 3.toChar()
                                                    c3 = 2.toChar()
                                                }
                                            } else {
                                                iArr = iArr2
                                                c2 = 3.toChar()
                                                c3 = 3.toChar()
                                            }
                                        } else {
                                            iArr = iArr2
                                            desiredWidth2 = f5
                                            c2 = 3.toChar()
                                        }
                                        if (c3 != c2) {
                                            i11++
                                            f4 = desiredWidth2
                                            i7 = i5
                                            iArr2 = iArr
                                            textViewEx2 = this
                                            z = true
                                        }
                                    }
                                }
                            }
                        }
                        iArr = iArr2
                    } else {
                        iArr = iArr2
                        i = length
                        i2 = lineCount
                        i3 = measuredWidth
                    }
                    i5 = i7
                }
                i7 = i5 + 1
                length = i
                lineCount = i2
                measuredWidth = i3
                iArr2 = iArr
                textViewEx2 = this
                f = 1.0f
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e), it) }
        }
    }

    internal class JustifySpan(var proportion: Float) : MetricAffectingSpan() {
        // android.text.style.CharacterStyle
        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.textScaleX *= this.proportion
        }

        // android.text.style.MetricAffectingSpan
        override fun updateMeasureState(textPaint: TextPaint) {
            textPaint.textScaleX *= this.proportion
        }

        fun updateProportion(f: Float) {
            this.proportion = f
        }
    }

    companion object {
        private const val CHAR_SPACE = ' '
        private const val FF = 1.0f
        private const val JUSTIFY_TOLERANCE = 0.5f
        private const val MAX_SPACES_IN_LINE = 64
        private const val RIGHT_SECURITY_MARGIN = 1
        private const val SCALE_EQUAL = 1.0f
        private const val SCALE_MAX = 10.0f
        private const val STATUS_WIDTH_LARGER = 1
        private const val STATUS_WIDTH_NOTHING = 0
        private const val STATUS_WIDTH_OK = 3
        private const val STATUS_WIDTH_SMALLER = 2
        private const val TUNING_SCALE = 0.5f
        private const val TUNING_STEPS = 5
    }
}