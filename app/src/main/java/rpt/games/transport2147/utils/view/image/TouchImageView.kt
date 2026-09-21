package rpt.games.transport2147.utils.view.image

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import rpt.com.base.log.d
import kotlin.math.abs
import kotlin.math.min


class TouchImageView : androidx.appcompat.widget.AppCompatImageView {
    var last: PointF
    lateinit var m: FloatArray
    var mScaleDetector: ScaleGestureDetector? = null
    var mTouchMatrix: Matrix? = null
    var maxScale: Float
    var minScale: Float
    var mode: Int
    var oldMeasuredHeight: Int = 0
    var oldMeasuredWidth: Int = 0
    protected var origHeight: Float = 0f
    protected var origWidth: Float = 0f
    var saveScale: Float
    var start: PointF
    var viewHeight: Int = 0
    var viewWidth: Int = 0

    fun getFixDragTrans(f: Float, f2: Float, f3: Float): Float {
        if (f3 <= f2) {
            return 0.0f
        }
        return f
    }

    fun getFixTrans(f: Float, f2: Float, f3: Float): Float {
        val f4: Float
        val f5: Float
        if (f3 <= f2) {
            f5 = f2 - f3
            f4 = 0.0f
        } else {
            f4 = f2 - f3
            f5 = 0.0f
        }
        if (f < f4) {
            return (-f) + f4
        }
        if (f > f5) {
            return (-f) + f5
        }
        return 0.0f
    }

    constructor(context: Context) : super(context) {
        this.mode = 0
        this.last = PointF()
        this.start = PointF()
        this.minScale = 1.0f
        this.maxScale = 3.0f
        this.saveScale = 1.0f
        sharedConstructing(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        this.mode = 0
        this.last = PointF()
        this.start = PointF()
        this.minScale = 1.0f
        this.maxScale = 3.0f
        this.saveScale = 1.0f
        sharedConstructing(context)
    }

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        this.mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        this.mTouchMatrix = Matrix()
        this.m = FloatArray(9)
        imageMatrix = this.mTouchMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener { view, motionEvent ->
            this@TouchImageView.mScaleDetector!!.onTouchEvent(motionEvent)
            val pointF = PointF(motionEvent.x, motionEvent.y)
            val action = motionEvent.action
            if (action != 6) {
                when (action) {
                    0 -> {
                        this@TouchImageView.last.set(pointF)
                        this@TouchImageView.start.set(this@TouchImageView.last)
                        this@TouchImageView.mode = 1
                    }

                    1 -> {
                        this@TouchImageView.mode = 0
                        val iAbs = abs(pointF.x - this@TouchImageView.start.x).toInt()
                        val iAbs2 = abs(pointF.y - this@TouchImageView.start.y).toInt()
                        if (iAbs < 3 && iAbs2 < 3) {
                            this@TouchImageView.performClick()
                        }
                    }

                    2 -> if (this@TouchImageView.mode == 1) {
                        this@TouchImageView.mTouchMatrix!!.postTranslate(
                            this@TouchImageView.getFixDragTrans(
                                pointF.x - this@TouchImageView.last.x,
                                this@TouchImageView.viewWidth.toFloat(),
                                this@TouchImageView.origWidth * this@TouchImageView.saveScale
                            ),
                            this@TouchImageView.getFixDragTrans(
                                pointF.y - this@TouchImageView.last.y,
                                this@TouchImageView.viewHeight.toFloat(),
                                this@TouchImageView.origHeight * this@TouchImageView.saveScale
                            )
                        )
                        this@TouchImageView.fixTrans()
                        this@TouchImageView.last.set(pointF.x, pointF.y)
                    }
                }
            } else {
                this@TouchImageView.mode = 0
            }
            this@TouchImageView.imageMatrix = this@TouchImageView.mTouchMatrix
            this@TouchImageView.invalidate()
            true
        }
    }

    fun setMaxZoom(f: Float) {
        this.maxScale = f
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            this@TouchImageView.mode = 2
            return true
        }

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            var scaleFactor = scaleGestureDetector.scaleFactor
            val f = this@TouchImageView.saveScale
            this@TouchImageView.saveScale *= scaleFactor
            if (this@TouchImageView.saveScale > this@TouchImageView.maxScale) {
                this@TouchImageView.saveScale = this@TouchImageView.maxScale
                scaleFactor = this@TouchImageView.maxScale / f
            } else if (this@TouchImageView.saveScale < this@TouchImageView.minScale) {
                this@TouchImageView.saveScale = this@TouchImageView.minScale
                scaleFactor = this@TouchImageView.minScale / f
            }
            if (this@TouchImageView.origWidth * this@TouchImageView.saveScale <=
                this@TouchImageView.viewWidth || this@TouchImageView.origHeight *
                this@TouchImageView.saveScale <= this@TouchImageView.viewHeight) {
                this@TouchImageView.mTouchMatrix!!.postScale(
                    scaleFactor,
                    scaleFactor,
                    (this@TouchImageView.viewWidth / 2).toFloat(),
                    (this@TouchImageView.viewHeight / 2).toFloat()
                )
            } else {
                this@TouchImageView.mTouchMatrix!!.postScale(
                    scaleFactor,
                    scaleFactor,
                    scaleGestureDetector.focusX,
                    scaleGestureDetector.focusY
                )
            }
            this@TouchImageView.fixTrans()
            return true
        }
    }

    fun fixTrans() {
        this.mTouchMatrix!!.getValues(this.m)
        val f = this.m[2]
        val f2 = this.m[5]
        val fixTrans = getFixTrans(f, this.viewWidth.toFloat(), this.origWidth * this.saveScale)
        val fixTrans2 = getFixTrans(f2, this.viewHeight.toFloat(), this.origHeight * this.saveScale)
        if (fixTrans == 0.0f && fixTrans2 == 0.0f) {
            return
        }
        this.mTouchMatrix!!.postTranslate(fixTrans, fixTrans2)
    }

    // android.widget.ImageView, android.view.View
    override fun onMeasure(i: Int, i2: Int) {
        super.onMeasure(i, i2)
        this.viewWidth = MeasureSpec.getSize(i)
        this.viewHeight = MeasureSpec.getSize(i2)
        if ((this.oldMeasuredHeight == this.viewWidth && this.oldMeasuredHeight == this.viewHeight)
            || this.viewWidth == 0 || this.viewHeight == 0) {
            return
        }
        this.oldMeasuredHeight = this.viewHeight
        this.oldMeasuredWidth = this.viewWidth
        if (this.saveScale == 1.0f) {
            val drawable = getDrawable()
            if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
                return
            }
            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight
            d("bmSize", "bmWidth: $intrinsicWidth bmHeight : $intrinsicHeight")
            val f = intrinsicWidth.toFloat()
            val f2 = intrinsicHeight.toFloat()
            val fMin = min((this.viewWidth.toFloat()) / f, (this.viewHeight.toFloat()) / f2)
            this.mTouchMatrix!!.setScale(fMin, fMin)
            val f3 = ((this.viewHeight.toFloat()) - (f2 * fMin)) / 2.0f
            val f4 = (this.viewWidth - (fMin * f)) / 2.0f
            this.mTouchMatrix!!.postTranslate(f4, f3)
            this.origWidth = this.viewWidth - (f4 * 2.0f)
            this.origHeight = this.viewHeight - (f3 * 2.0f)
            imageMatrix = this.mTouchMatrix
        }
        fixTrans()
    }

    companion object {
        const val CLICK: Int = 3
        const val DRAG: Int = 1
        const val NONE: Int = 0
        const val ZOOM: Int = 2
    }
}