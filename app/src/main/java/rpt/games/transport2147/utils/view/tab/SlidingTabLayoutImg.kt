package rpt.games.transport2147.utils.view.tab

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class SlidingTabLayoutImg @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null,
    i: Int = 0
) : HorizontalScrollView(context, attributeSet, i) {
    private val mContentDescriptions: SparseArray<String?> = SparseArray<String?>()
    private var mDistributeEvenly = false
    private var mTabImages: IntArray? = null
    private val mTabStrip: SlidingTabStripImg
    private var mTabViewLayoutId = 0
    private val mTitleOffset: Int
    private var mViewPager: ViewPager? = null
    private var mViewPagerPageChangeListener: OnPageChangeListener? = null

    init {
        isHorizontalScrollBarEnabled = false
        isFillViewport = true
        this.mTitleOffset = (resources.displayMetrics.density * 24.0f).toInt()
        this.mTabStrip = SlidingTabStripImg(context)
        addView(this.mTabStrip, -1, -2)
    }

    fun setDistributeEvenly(z: Boolean) {
        this.mDistributeEvenly = z
    }

    fun setTabImages(iArr: IntArray?) {
        this.mTabImages = iArr
    }

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?) {
        this.mViewPagerPageChangeListener = onPageChangeListener
    }

    fun setCustomTabView(i: Int) {
        this.mTabViewLayoutId = i
    }

    fun setViewPager(viewPager: ViewPager?) {
        this.mTabStrip.removeAllViews()
        this.mViewPager = viewPager
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(InternalViewPagerListener())
            populateTabStrip()
        }
    }

    protected fun createDefaultTabView(context: Context?): ImageView {
        return ImageView(context)
    }

    private fun populateTabStrip() {
        val adapter = this.mViewPager!!.adapter
        val tabClickListener = TabClickListener()
        for (i in 0..<adapter!!.count) {
            var imageViewCreateDefaultTabView =
                if (this.mTabViewLayoutId != 0) LayoutInflater.from(context).inflate(
                    this.mTabViewLayoutId,
                    this.mTabStrip as ViewGroup?,
                    false
                ) as ImageView? else null
            if (imageViewCreateDefaultTabView == null) {
                imageViewCreateDefaultTabView = createDefaultTabView(context)
            }
            if (this.mTabImages != null) {
                imageViewCreateDefaultTabView.setImageResource(this.mTabImages!![i])
            }
            if (this.mDistributeEvenly) {
                val layoutParams =
                    imageViewCreateDefaultTabView.layoutParams as LinearLayout.LayoutParams
                layoutParams.width = 0
                layoutParams.weight = 1.0f
            }
            imageViewCreateDefaultTabView.setOnClickListener(tabClickListener)
            val str = this.mContentDescriptions.get(i, null)
            if (str != null) {
                imageViewCreateDefaultTabView.contentDescription = str
            }
            this.mTabStrip.addView(imageViewCreateDefaultTabView)
            if (i == this.mViewPager!!.currentItem) {
                imageViewCreateDefaultTabView.isSelected = true
            }
        }
    }

    fun setContentDescription(i: Int, str: String?) {
        this.mContentDescriptions.put(i, str)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (this.mViewPager != null) {
            scrollToTab(this.mViewPager!!.currentItem, 0)
        }
    }

    fun scrollToTab(i: Int, i2: Int) {
        val childCount: Int = this.mTabStrip.childCount
        val childAt = this.mTabStrip.getChildAt(i)
        if (childCount == 0 || i < 0 || i >= childCount || childAt == null) {
            return
        }
        var left = childAt.left + i2
        if (i > 0 || i2 > 0) {
            left -= this.mTitleOffset
        }
        scrollTo(left, 0)
    }

    private inner class InternalViewPagerListener : OnPageChangeListener {
        private var mScrollState = 0

        override fun onPageScrolled(i: Int, f: Float, i2: Int) {
            val childCount: Int = this@SlidingTabLayoutImg.mTabStrip.childCount
            if (childCount == 0 || i < 0 || i >= childCount) {
                return
            }
            this@SlidingTabLayoutImg.mTabStrip.onViewPagerPageChanged(i, f)
            val selectedChild = this@SlidingTabLayoutImg.mTabStrip.getChildAt(i)
            this@SlidingTabLayoutImg.scrollToTab(
                i,
                if (selectedChild != null) (selectedChild.width * f).toInt() else 0
            )
            if (this@SlidingTabLayoutImg.mViewPagerPageChangeListener != null) {
                this@SlidingTabLayoutImg.mViewPagerPageChangeListener!!
                    .onPageScrolled(i, f, i2)
            }
        }

        override fun onPageScrollStateChanged(i: Int) {
            this.mScrollState = i
            if (this@SlidingTabLayoutImg.mViewPagerPageChangeListener != null) {
                this@SlidingTabLayoutImg.mViewPagerPageChangeListener!!.onPageScrollStateChanged(i)
            }
        }

        override fun onPageSelected(i: Int) {
            if (this.mScrollState == 0) {
                this@SlidingTabLayoutImg.mTabStrip.onViewPagerPageChanged(i, 0.0f)
                this@SlidingTabLayoutImg.scrollToTab(i, 0)
            }
            var i2 = 0
            while (i2 < this@SlidingTabLayoutImg.mTabStrip.childCount) {
                this@SlidingTabLayoutImg.mTabStrip.getChildAt(i2).isSelected = i == i2
                i2++
            }
            if (this@SlidingTabLayoutImg.mViewPagerPageChangeListener != null) {
                this@SlidingTabLayoutImg.mViewPagerPageChangeListener!!.onPageSelected(i)
            }
        }
    }

    private inner class TabClickListener : OnClickListener {

        override fun onClick(view: View?) {
            for (i in 0..<this@SlidingTabLayoutImg.mTabStrip.childCount) {
                if (view === this@SlidingTabLayoutImg.mTabStrip.getChildAt(i)) {
                    this@SlidingTabLayoutImg.mViewPager!!.setCurrentItem(i)
                    return
                }
            }
        }
    }

    companion object {
        private const val TITLE_OFFSET_DIPS = 24
    }
}