package rpt.games.transport2147.utils.managers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.size
import org.w3c.dom.Element
import rpt.com.base.log.e
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate
import rpt.games.transport2147.R
import rpt.games.transport2147.ui.history.HistoryFragmentDirections
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.appmodels.complex.BaseValue
import rpt.games.transport2147.utils.data.appmodels.complex.History
import rpt.games.transport2147.utils.data.appmodels.complex.History.historyEnabled
import rpt.games.transport2147.utils.data.appmodels.complex.PlayerObject
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement


class DialogManager {
    companion object {
        fun showFastStartDialog(context: Context) {
            try {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val layoutInflater = context.getSystemService("layout_inflater") as LayoutInflater
                val scrollView = layoutInflater.inflate(
                    R.layout.dialog_faststart,
                    null as ViewGroup?
                ) as ScrollView
                builder.setView(scrollView)
                setCustomDialogTitle(context, scrollView, R.string.dialog_faststartTitle)
                ChapterFormatter().formatChapter(
                    scrollView.findViewById<View?>(R.id.dlgFastStart_layContent) as LinearLayout?,
                    context,
                    GameConstants.BOOK_CHAPTER_FASTSTART
                )
                val radioGroup =
                    scrollView.findViewById<View?>(R.id.dlgFastStart_rdgTemplates) as RadioGroup
                getSheetTemplateList(context, radioGroup, layoutInflater, true)
                val alertDialogCreate: AlertDialog = builder.create()
                alertDialogCreate.show()
                setCustomCancelButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_fastStartMenu
                ) { alertDialogCreate.dismiss() }
                setCustomOkButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_fastStartPlay,
                    object : View.OnClickListener {
                        override fun onClick(view: View?) {
                            if (radioGroup.checkedRadioButtonId == -1) {
                                ToastManager.showGenericToast(
                                    context,
                                    context.getString(R.string.toast_selectTemplate)
                                )
                                return
                            }
                            var radioButton: RadioButton? = null
                            for (i in 0..<radioGroup.size) {
                                radioButton = radioGroup.getChildAt(i) as RadioButton?
                                if (radioButton!!.isChecked) {
                                    break
                                }
                            }
                            ProfileManager.addProfile(
                                context,
                                radioButton!!.text
                                    .toString() + context.getString(R.string.txt_FastStart)
                                        + AppUtils.getTimestamp(),
                                radioButton.tag.toString()
                            )
                            History.requestedChapter = GameConstants.BOOK_CHAPTER_START
                            GameLogic.navigator?.safeNavController(
                                R.id.main_activity_nav_host_fragment)?.
                            safeNavigate(
                                HistoryFragmentDirections.actionHistoryFragmentToNavigatorFragment())
                            alertDialogCreate.dismiss()
                        }
                    })
                val onClickListener: View.OnClickListener = View.OnClickListener { view ->

                    GameLogic.changeFontSize(context, view)
                    val linearLayout: LinearLayout =
                        alertDialogCreate.findViewById(R.id.dlgFastStart_layContent)
                    linearLayout.removeAllViews()
                    try {
                        ChapterFormatter().formatChapter(
                            linearLayout,
                            context,
                            GameConstants.BOOK_CHAPTER_FASTSTART
                        )
                    } catch (e: Exception) {
                        e.message?.let { e(Throwable(e), it) }
                    }
                }
                (alertDialogCreate.findViewById<ImageView>(R.id.incFontChgr_btnFontIncrease)!!)
                    .setOnClickListener(
                    onClickListener
                )
                (alertDialogCreate.findViewById<ImageView>(R.id.incFontChgr_btnFontDecrease)!!)
                    .setOnClickListener(
                    onClickListener
                )
            } catch (e: Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }

        fun setCustomDialogTitle(context: Context, view: View, i: Int) {
            setCustomDialogTitle(context, view, context.getString(i))
        }

        fun setCustomDialogTitle(context: Context?, view: View, str: String?) {
            (view.findViewById<View?>(R.id.dialog_title) as TextView).text = str
        }

        private fun getSheetTemplateList(
            context: Context?,
            radioGroup: RadioGroup,
            layoutInflater: LayoutInflater,
            z: Boolean
        ) {
            val templates: ArrayList<String?> = BookManager.getTemplates()
            var view: View? = null
            for (i in templates.indices) {
                val rootElement: Element? = getRootElement(templates[i])
                val zEqualsIgnoreCase: Boolean = getElementAttribute(
                    rootElement,
                    GameConstants.XML_NODE_TEMPLATES_SHEET_ATTR_FASTSTART
                )?.equals(GameConstants.STRING_YES, ignoreCase = true) ?: false
                if (!z || zEqualsIgnoreCase) {
                    val radioButton = layoutInflater.inflate(
                        R.layout.component_listelementprofiletemplate,
                        null as ViewGroup?
                    ) as RadioButton
                    radioButton.id = AppUtils.generateViewId()
                    radioButton.text = getElementAttribute(rootElement, "id")
                    radioButton.tag = templates[i]
                    if (i == 0) {
                        radioButton.isChecked = true
                    }
                    if (z || zEqualsIgnoreCase) {
                        radioGroup.addView(radioButton)
                    } else {
                        view = radioButton
                    }
                }
            }
            if (view != null) {
                radioGroup.addView(view)
            }
        }

        fun setCustomCancelButton(
            context: Context,
            alertDialog: AlertDialog,
            i: Int,
            onClickListener: View.OnClickListener?
        ) {
            val textView = alertDialog.findViewById<View?>(R.id.dialog_buttonCancel) as TextView
            if (i != -1) {
                textView.text = context.getString(i)
            }
            if (onClickListener != null) {
                textView.setOnClickListener(onClickListener)
            } else {
                textView.setOnClickListener { alertDialog.dismiss() }
            }
        }

        fun setCustomOkButton(
            context: Context?,
            alertDialog: AlertDialog,
            str: String?,
            onClickListener: View.OnClickListener?
        ) {
            val textView = alertDialog.findViewById<View?>(R.id.dialog_buttonOk) as TextView
            if (str != null) {
                textView.text = str
            }
            if (onClickListener != null) {
                textView.setOnClickListener(onClickListener)
            } else {
                textView.setOnClickListener { alertDialog.dismiss() }
            }
        }

        fun setCustomOkButton(
            context: Context,
            alertDialog: AlertDialog,
            i: Int,
            onClickListener: View.OnClickListener?
        ) {
            val textView = alertDialog.findViewById<View?>(R.id.dialog_buttonOk) as TextView
            if (i != -1) {
                textView.text = context.getString(i)
            }
            if (onClickListener != null) {
                textView.setOnClickListener(onClickListener)
            } else {
                textView.setOnClickListener { alertDialog.dismiss() }
            }
        }

        fun showGlossaryEntry(context: Context, str: String?) {
            try {
                val builder = AlertDialog.Builder(context)
                val rootElement =
                    getRootElement(BookManager.getDictionaryEntry(str))
                val viewInflate: View =
                    (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                        R.layout.dialog_glossaryentry,
                        null as ViewGroup?
                    )
                builder.setView(viewInflate)
                setCustomDialogTitle(context, viewInflate, R.string.dialog_dictionaryEntryTitle)
                (viewInflate.findViewById<View?>(R.id.dlgGlossaryEntry_txtTitle) as TextView).text =
                    getElementAttribute(rootElement, "name")
                (viewInflate.findViewById<View?>(R.id.dlgGlossaryEntry_txtDescription) as TextView)
                    .text = rootElement!!.textContent
                val alertDialogCreate = builder.create()
                val onClickListener: View.OnClickListener = View.OnClickListener {
                    alertDialogCreate.dismiss() }
                alertDialogCreate.show()
                setCustomOkButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_buttonOk,
                    onClickListener
                )
            } catch (e: java.lang.Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }
        fun historyJump(context: Context, str: String?, i: Int) {
            try {
                val builder = AlertDialog.Builder(context)
                val viewInflate: View =
                    (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                        R.layout.dialog_historyjump,
                        null as ViewGroup?
                    )
                builder.setView(viewInflate)
                setCustomDialogTitle(context, viewInflate, R.string.dialog_historyOptionTitle)
                val alertDialogCreate = builder.create()
                alertDialogCreate.show()
                (viewInflate.findViewById<View?>(R.id.dlgHistoryJump_rdgOptions) as RadioGroup)
                    .setOnCheckedChangeListener { p0, p1 ->
                        when (p1) {
                            R.id.dlgHistoryJump_opt1 -> ActionMenuManager.actionLoadChapter(
                                context,
                                str!!
                            )
                            R.id.dlgHistoryJump_opt2 -> {
                                historyEnabled = true
                                ActionMenuManager.actionLoadChapter(context, str!!)
                            }

                            R.id.dlgHistoryJump_opt3 -> historyTrim(context, str!!, i)
                        }
                        alertDialogCreate.dismiss()
                    }
            } catch (e: java.lang.Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }

        fun historyTrim(context: Context, str: String, i: Int) {
            try {
                val builder = AlertDialog.Builder(context)
                val linearLayout =
                    (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                        R.layout.dialog_okcancel,
                        null as ViewGroup?
                    ) as LinearLayout
                builder.setView(linearLayout)
                setCustomDialogTitle(context, linearLayout, R.string.dialog_trimHistoryTitle)
                (linearLayout.findViewById<View?>(R.id.dlgOkCancel_message) as TextView).text =
                    String.format(context.getText(R.string.dialog_trimHistoryMsg).toString(), str)
                val alertDialogCreate = builder.create()
                alertDialogCreate.show()
                setCustomOkButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_buttonOk
                ) {
                    History.trimHistory(i)
                    ProfileManager.saveProfileHistory(context, GameLogic.Profile)
                    alertDialogCreate.dismiss()
                }
                setCustomCancelButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_buttonCancel,
                    null as View.OnClickListener?
                )
            } catch (e: java.lang.Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }

        fun takeMultiObject(context: Context, playerObject: PlayerObject) {
            try {
                val builder = AlertDialog.Builder(context)
                val viewInflate: View =
                    (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                        R.layout.dialog_multiobjects,
                        null as ViewGroup?
                    )
                builder.setView(viewInflate)
                setCustomDialogTitle(context, viewInflate, playerObject.name)
                val baseValue = BaseValue(1, 99, 1)
                val textView =
                    viewInflate.findViewById<View?>(R.id.dlgMultiobject_txtValue) as TextView
                textView.text = baseValue.value.toString()
                val onClickListener: View.OnClickListener = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.dlgMultiobject_btnAdd -> baseValue.add()
                        R.id.dlgMultiobject_btnSubtract -> baseValue.subtract()
                    }
                    textView.text = baseValue.value.toString()
                }
                (viewInflate.findViewById<View?>(R.id.dlgMultiobject_btnSubtract) as ImageView)
                    .setOnClickListener(
                    onClickListener
                )
                (viewInflate.findViewById<View?>(R.id.dlgMultiobject_btnAdd) as ImageView)
                    .setOnClickListener(
                    onClickListener
                )
                val radioButton =
                    viewInflate.findViewById<View?>(R.id.dlgMultiObjects_choiceYes) as RadioButton
                val radioButton2 =
                    viewInflate.findViewById<View?>(R.id.dlgMultiObjects_choiceNo) as RadioButton
                val viewFindViewById = viewInflate.findViewById<View?>(R.id.dlgMultiobject_layValue)
                val viewFindViewById2 =
                    viewInflate.findViewById<View?>(R.id.dlgMultiObjects_lblQuantity)
                radioButton2.isChecked = true
                viewFindViewById?.visibility = View.GONE
                viewFindViewById2?.visibility = View.GONE
                val onClickListener2: View.OnClickListener = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.dlgMultiObjects_choiceNo -> {
                            viewFindViewById?.visibility = View.GONE
                            viewFindViewById2?.visibility = View.GONE
                        }

                        R.id.dlgMultiObjects_choiceYes -> {
                            viewFindViewById?.visibility = View.VISIBLE
                            viewFindViewById2?.visibility = View.VISIBLE
                        }
                    }
                }
                radioButton.setOnClickListener(onClickListener2)
                radioButton2.setOnClickListener(onClickListener2)
                val alertDialogCreate = builder.create()
                alertDialogCreate.show()
                setCustomOkButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_buttonOk
                ) {
                    if (radioButton.isChecked) {
                        playerObject.quantity = baseValue.value
                        SheetManager.addObject(playerObject)
                    }
                    alertDialogCreate.dismiss()
                }
            } catch (e: java.lang.Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }
        fun showWarningOnChangeSheetValue(context: Context, i: Int) {
            try {
                val builder = AlertDialog.Builder(context)
                val viewInflate: View =
                    (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                        R.layout.dialog_ok,
                        null as ViewGroup?
                    )
                builder.setView(viewInflate)
                setCustomDialogTitle(context, viewInflate, R.string.dialog_alert)
                (viewInflate.findViewById<View?>(R.id.dlgOk_message) as TextView).text = context.getString(
                    i
                )
                val alertDialogCreate = builder.create()
                val onClickListener: View.OnClickListener = View.OnClickListener {
                    alertDialogCreate.dismiss() }
                alertDialogCreate.show()
                setCustomOkButton(
                    context,
                    alertDialogCreate,
                    R.string.dialog_buttonOk,
                    onClickListener
                )
            } catch (e: java.lang.Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }
        fun showGenericDialog(context: Context, str: String?, str2: String?) {
            try {
                val builder = AlertDialog.Builder(context)
                val viewInflate: View =
                    (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                        R.layout.dialog_ok,
                        null as ViewGroup?
                    )
                builder.setView(viewInflate)
                setCustomDialogTitle(context, viewInflate, str)
                (viewInflate.findViewById<View?>(R.id.dlgOk_message) as TextView).text = str2
                val alertDialogCreate = builder.create()
                alertDialogCreate.show()
                setCustomOkButton(context, alertDialogCreate, null, null)
            } catch (e: java.lang.Exception) {
                e.message?.let { e(Throwable(e), it) }
            }
        }

        fun showCombatExitAlert(context: android.content.Context?) {}
    }
}