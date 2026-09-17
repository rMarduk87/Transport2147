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
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.xml.XmlUtility


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
                            /*History.setRequestedChapter(GamesConstants.BOOK_CHAPTER_START)
                            context.startActivity(
                                Intent(
                                    context,
                                    Activity_Navigator::class.java as Class<*>
                                )
                            )*/
                            alertDialogCreate.dismiss()
                        }
                    })
                val onClickListener: View.OnClickListener = View.OnClickListener { view ->

                    GameLogic.changeFontSize(context, view,)
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

        private fun setCustomDialogTitle(
            context: Context,
            view: ScrollView,
            i: Int
        ) {
            setCustomDialogTitle(context, view, context.getString(i));
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
                val rootElement: Element? = XmlUtility.getRootElement(templates[i])
                val zEqualsIgnoreCase: Boolean = XmlUtility.getElementAttribute(
                    rootElement,
                    GameConstants.XML_NODE_TEMPLATES_SHEET_ATTR_FASTSTART
                )?.equals(GameConstants.STRING_YES, ignoreCase = true) ?: false
                if (!z || zEqualsIgnoreCase) {
                    val radioButton = layoutInflater.inflate(
                        R.layout.component_listelementprofiletemplate,
                        null as ViewGroup?
                    ) as RadioButton
                    radioButton.id = AppUtils.generateViewId()
                    radioButton.text = XmlUtility.getElementAttribute(rootElement, "id")
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

        fun showGlossaryEntry(context: Context, attribute: String) {}
        fun historyJump(context: android.content.Context, string: String, i: Int) {}
        fun takeMultiObject(context: Context, m4clone: Any) {}
        fun showWarningOnChangeSheetValue(dialogCondbackpackoverloaded: Int) {}
        fun showGenericDialog(context: android.content.Context, string: String, string2: String) {}
    }
}