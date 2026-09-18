package rpt.games.transport2147

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityNavigatorBinding
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper
import rpt.games.transport2147.utils.data.appmodels.complex.History
import rpt.games.transport2147.utils.data.appmodels.complex.History.addVisitedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.lastChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.requestedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.size
import rpt.games.transport2147.utils.managers.ActionMenuManager
import rpt.games.transport2147.utils.managers.CombatManager
import rpt.games.transport2147.utils.managers.DiceRollerManager
import rpt.games.transport2147.utils.managers.ProfileManager
import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import rpt.games.transport2147.utils.managers.SheetManager
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.view.tab.SlidingTabLayoutImg
import rpt.games.transport2147.utils.view.text.RPTextView2
import java.util.Locale


class NavigatorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNavigatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityNavigator)
        GameLogic.checkForAppRecovery(this, true)
        binding = ActivityNavigatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GameLogic.Navigator = this
        this.mSectionsPagerAdapter = SectionsPagerAdapter(fragmentManager)
        this.mViewPager = findViewById<View?>(R.id.actNavigator_pgrNavigator) as ViewPager?
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter)
        val slidingTabLayoutImg =
            findViewById<View?>(R.id.actNavigator_tbsTabs) as SlidingTabLayoutImg
        slidingTabLayoutImg.setDistributeEvenly(true)
        slidingTabLayoutImg.setCustomTabView(R.layout.component_navigatortab)
        if (requestedChapter == GameConstants.BOOK_CHAPTER_RULES || requestedChapter == GameConstants.BOOK_CHAPTER_RULES1 || requestedChapter == GameConstants.BOOK_CHAPTER_RULES2) {
            slidingTabLayoutImg.setTabImages(
                intArrayOf(
                    R.drawable.tab_chapter_stateful,
                    R.drawable.tab_sheet_stateful
                )
            )
        } else {
            slidingTabLayoutImg.setTabImages(
                intArrayOf(
                    R.drawable.tab_chapter_stateful,
                    R.drawable.tab_sheet_stateful,
                    R.drawable.tab_fight_stateful
                )
            )
        }
        slidingTabLayoutImg.setViewPager(this.mViewPager)
        slidingTabLayoutImg.setOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(i: Int) {
                this@NavigatorActivity.updateActionBar(i)
                if (i != 2) {
                    CombatManager.checkForExitAlert(GameLogic.Navigator)
                }
                ProfileManager.saveProfileSheet(GameLogic.Navigator, GameLogic.Profile)
                SheetManager.initSheet()
            }
        })
        val actionBar: ActionBar? = getActionBar()
        actionBar.setTitle(getString(R.string.title_activityNavigator))
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)
        AppUtils.makeActionOverflowMenuShown(this)
    }

    fun updateActionBar(i: Int) {
        try {
            if (this.itemPreviousChapter == null) {
                return
            }
            val z =
                !(i != 0 || requestedChapter == GameConstants.BOOK_CHAPTER_RULES ||
                        requestedChapter == GameConstants.BOOK_CHAPTER_RULES1 || requestedChapter ==
                        GameConstants.BOOK_CHAPTER_RULES2)
            for (menuItem in arrayOf<MenuItem>(
                this.itemPreviousChapter,
                this.itemGoToChapter,
                this.itemHistory,
                this.itemEnableHistory
            )) {
                menuItem.isEnabled = z
                menuItem.icon!!.alpha = if (z) 255 else TransportMediator.KEYCODE_MEDIA_RECORD
            }
            ActionMenuManager.updateHistoryMenu(this.itemEnableHistory)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_navigator, menu)
            this.itemPreviousChapter = menu.findItem(R.id.mnuNavigator_itmPreviousChapter)
            this.itemGoToChapter = menu.findItem(R.id.mnuNavigator_itmGoChapter)
            this.itemHistory = menu.findItem(R.id.mnuNavigator_itmHistory)
            this.itemEnableHistory = menu.findItem(R.id.mnuNavigator_itmEnableDisableHistory)
            if (GameLogic.DiceRoller1 == null) {
                GameLogic.DiceRoller1 =
                    DiceRollerManager(menu.findItem(R.id.mnuNavigator_itmDiceRoller1), 1)
            } else {
                GameLogic.DiceRoller1.setMenuItem(menu.findItem(R.id.mnuNavigator_itmDiceRoller1))
            }
            if (GameLogic.DiceRoller2 == null) {
                GameLogic.DiceRoller2 =
                    DiceRollerManager(menu.findItem(R.id.mnuNavigator_itmDiceRoller2), 2)
            } else {
                GameLogic.DiceRoller2.setMenuItem(menu.findItem(R.id.mnuNavigator_itmDiceRoller2))
            }
            updateActionBar(this.mViewPager.getCurrentItem())
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
        return true
    }

    // android.app.Activity
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        return ActionMenuManager.OnActionMenuClicked(this, menuItem) || super.onOptionsItemSelected(
            menuItem
        )
    }

    // android.app.Activity
    public override fun onDestroy() {
        GameLogic.Navigator = null
        GameLogic.DiceRoller1 = null
        GameLogic.DiceRoller2 = null
        super.onDestroy()
    }

    // android.app.Activity
    public override fun onStart() {
        try {
            super.onStart()
            if (this.itemEnableHistory != null) {
                ActionMenuManager.updateHistoryMenu(this.itemEnableHistory)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(context))
    }

    fun onClickSheetView(view: View?) {
        SheetManager.OnClickView(view)
    }

    fun onClickCombatView(view: View?) {
        CombatManager.OnClickView(view!!)
    }

    fun changePagerPage(i: Int) {
        try {
            this.mViewPager.setCurrentItem(i)
            updateActionBar(i)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun onChangeFontSize(view: View?) {
        try {
            GameLogic.changeFontSize(this, view!!)
            GameLogic.ChapterFragment!!.loadChapter(true)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun onSwitchJustification(view: View?) {
        val z: Boolean = !SharedPreferencesManager.textJustification
        SharedPreferencesManager.textJustification = z
        GameLogic.iconToggleJustification!!.drawable.level = if (z) 2 else 1
        val linearLayout = findViewById<View?>(R.id.frgChapter_layContent) as LinearLayout
        for (i in 0..<linearLayout.childCount) {
            val childAt = linearLayout.getChildAt(i)
            if (childAt is RPTextView2) {
                (childAt as RPTextView2).justification = z
            }
        }
    }

    fun onToggleHistory(view: View?) {
        try {
            ActionMenuManager.actionEnableDisableHistory(this.itemEnableHistory)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    class SectionsPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {
        override fun getItem(i: Int): Fragment {
            if (i == 0) {
                return ChapterFragment.newInstance()
            }
            if (i == 1) {
                return SheetFragment.newInstance()
            }
            if (i == 2) {
                return CombatFragment.newInstance()
            }
            return ChapterFragment.newInstance()
        }
        
        override fun getCount(): Int {
            val requestedChapter = requestedChapter
            if (requestedChapter != null) {
                return if (requestedChapter == GameConstants.BOOK_CHAPTER_RULES || 
                    requestedChapter == GameConstants.BOOK_CHAPTER_RULES1 || 
                    requestedChapter == GameConstants.BOOK_CHAPTER_RULES2) 2 else 3
            }
            History.requestedChapter = GameConstants.BOOK_CHAPTER_RULES
            return 2
        }
        
        override fun getPageTitle(i: Int): CharSequence {
            val locale: Locale? = Locale.getDefault()
            when (i) {
                0 -> {
                    val requestedChapter = requestedChapter
                    if ((requestedChapter != GameConstants.BOOK_CHAPTER_RULES) &&
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES1) && 
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES2)) {
                        return this@NavigatorActivity.getString(
                            R.string.title_fragmentChapter)
                            .toUpperCase(locale)
                    }
                    return this@NavigatorActivity.getString(
                        R.string.title_fragmentRules)
                        .toUpperCase(locale)
                }

                1 -> return this@NavigatorActivity.getString(
                    R.string.title_fragmentSheet)
                    .toUpperCase(locale)

                2 -> return this@NavigatorActivity.getString(
                    R.string.title_fragmentCombat)
                    .toUpperCase(locale)

                else -> return null
            }
        }
    }
    
    class SheetFragment : Fragment() {
        override fun onCreateView(
            layoutInflater: LayoutInflater,
            viewGroup: ViewGroup?,
            bundle: Bundle?
        ): View? {
            return layoutInflater.inflate(R.layout.fragment_sheet, viewGroup, false)
        }

        override fun onStart() {
            try {
                super.onStart()
                SheetManager.initSheet()
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
        }

        override fun onPause() {
            ProfileManager.saveProfileSheet(GameLogic.Navigator, GameLogic.Profile)
            super.onPause()
        }

        companion object {
            fun newInstance(): SheetFragment {
                return SheetFragment()
            }
        }
    }


    class CombatFragment : Fragment() {

        override fun onCreateView(
            layoutInflater: LayoutInflater,
            viewGroup: ViewGroup?,
            bundle: Bundle?
        ): View? {
            return layoutInflater.inflate(R.layout.fragment_combat, viewGroup, false)
        }

        override fun onStart() {
            try {
                super.onStart()
                CombatManager.initCombat(false)
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
        }

        companion object {
            fun newInstance(): CombatFragment {
                return CombatFragment()
            }
        }
    }

    class ChapterFragment : Fragment() {
        var _renderedChapter: String = ""


        override fun onCreateView(
            layoutInflater: LayoutInflater,
            viewGroup: ViewGroup?,
            bundle: Bundle?
        ): View {
            val viewInflate: View =
                layoutInflater.inflate(R.layout.fragment_chapter, viewGroup, false)
            GameLogic.ChapterFragment = this
            GameLogic.iconToggleHistory =
                viewInflate.findViewById<View?>(R.id.incFontChgr_btnHistoryToggle) as ImageView?
            val requestedChapter = requestedChapter
            GameLogic.iconToggleHistory!!.visibility = if (requestedChapter ==
                GameConstants.BOOK_CHAPTER_RULES || requestedChapter ==
                GameConstants.BOOK_CHAPTER_RULES1 || requestedChapter ==
                GameConstants.BOOK_CHAPTER_RULES2) View.INVISIBLE else View.VISIBLE
            GameLogic.iconToggleJustification =
                viewInflate.findViewById<View?>(R.id.incFontChgr_btnSwitchJustification) as ImageView?
            GameLogic.iconToggleJustification!!.visibility = View.VISIBLE
            GameLogic.iconToggleJustification!!.drawable.level =
                if (SharedPreferencesManager.textJustification) 2 else 1
            ActionMenuManager.updateHistoryIcon()
            return viewInflate
        }

        override fun onStart() {
            try {
                super.onStart()
                loadChapter()
                ActionMenuManager.updateHistoryIcon()
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
        }

        override fun onStop() {
            this._renderedChapter = ""
            super.onStop()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            GameLogic.ChapterFragment = null
        }

        @JvmOverloads
        fun loadChapter(z: Boolean = false) {
            try {
                val requestedChapter: String = requestedChapter!!
                if (z || this._renderedChapter != requestedChapter) {
                    val linearLayout: LinearLayout =
                        requireActivity().findViewById(R.id.frgChapter_layContent)
                    linearLayout.removeAllViews()
                    val chapterFormatter = ChapterFormatter()
                    val boolValueOf = chapterFormatter.formatChapter(
                        linearLayout,
                        requireActivity(),
                        requestedChapter
                    )
                    val scrollView: ScrollView =
                        requireActivity().findViewById(R.id.frgChapter_scvScroll)
                    if (boolValueOf) {
                        scrollView.setBackgroundResource(R.drawable.bkg_chapter_body_dream)
                    } else {
                        scrollView.setBackgroundResource(R.drawable.bkg_chapter_body)
                    }
                    if ((requestedChapter != GameConstants.BOOK_CHAPTER_RULES) &&
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES1) &&
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES2)) {
                        val scrollView2: ScrollView =
                            requireActivity().findViewById(R.id.frgChapter_scvScroll)
                        scrollView2.post { scrollView2.fullScroll(33) }
                        scrollView2.fullScroll(33)
                    }
                    if ((requestedChapter != GameConstants.BOOK_CHAPTER_INTRO) &&
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES) &&
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES1) &&
                        (requestedChapter != GameConstants.BOOK_CHAPTER_RULES2) &&
                        (size == 0 || lastChapter!!.chapter != requestedChapter)) {
                        addVisitedChapter(requestedChapter, chapterFormatter.lastSummary)
                        ProfileManager.saveProfileHistory(requireActivity(), GameLogic.Profile)
                    }
                    GameLogic.CleanTakenObjectList()
                    this._renderedChapter = requestedChapter
                }
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e),it) }
            }
        }

        companion object {
            fun newInstance(): ChapterFragment {
                return ChapterFragment()
            }
        }
    }
}