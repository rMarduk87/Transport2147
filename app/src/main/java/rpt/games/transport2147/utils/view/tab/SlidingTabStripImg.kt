package rpt.games.transport2147.utils.view.tab

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout


internal class SlidingTabStripImg @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {
    private var mSelectedPosition = 0
    private var mSelectionOffset = 0f

    init {
        val f = resources.displayMetrics.density
    }

    fun onViewPagerPageChanged(i: Int, f: Float) {
        this.mSelectedPosition = i
        this.mSelectionOffset = f
        invalidate()
    }
}