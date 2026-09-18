package rpt.games.transport2147.utils.managers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import rpt.com.base.log.e
import rpt.games.transport2147.NavigatorActivity
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.appmodels.complex.Profile
import rpt.games.transport2147.utils.data.appmodels.complex.BaseValue
import rpt.games.transport2147.utils.data.appmodels.complex.History.historyEnabled
import rpt.games.transport2147.utils.data.appmodels.complex.History.requestedChapter
import rpt.games.transport2147.utils.data.appmodels.complex.History.trimHistory
import rpt.games.transport2147.utils.data.appmodels.complex.PlayerObject
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement
import kotlin.Any
import kotlin.Array
import kotlin.Boolean
import kotlin.CharSequence
import kotlin.Exception
import kotlin.Int
import kotlin.arrayOfNulls
import kotlin.text.equals
import kotlin.text.format


object DialogManager {
    
    fun createSingleChoiceAlert(
        context: Context,
        str: String?,
        charSequenceArr: Array<CharSequence?>?,
        i: Int,
        onClickListener: DialogInterface.OnClickListener?,
        z: Boolean
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(str)
            if (z) {
                builder.setPositiveButton(
                    context.getText(R.string.dialog_buttonOk),
                    null as DialogInterface.OnClickListener?
                )
            }
            builder.setSingleChoiceItems(charSequenceArr, i, onClickListener)
            builder.create().show()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun setCustomDialogTitle(context: Context, view: View, i: Int) {
        setCustomDialogTitle(context, view, context.getString(i))
    }

    fun setCustomDialogTitle(context: Context, view: View, str: String?) {
        (view.findViewById<View?>(R.id.dialog_title) as TextView).text = str
    }

    fun setCustomOkButtonEx(
        context: Context?,
        alertDialog: AlertDialog,
        str: String?,
        onClickListener: View.OnClickListener?
    ) {
        val textView = alertDialog.findViewById<View?>(R.id.dialog_buttonOk) as TextView
        if (str != null) {
            textView.text = str
        }
        textView.setOnClickListener { view ->
            
            onClickListener?.onClick(view)
            alertDialog.dismiss()
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

    fun setCustomCancelButton(
        context: Context?,
        alertDialog: AlertDialog,
        str: String?,
        onClickListener: View.OnClickListener?
    ) {
        val textView = alertDialog.findViewById<View?>(R.id.dialog_buttonCancel) as TextView
        if (str != null) {
            textView.text = str
        }
        if (onClickListener != null) {
            textView.setOnClickListener(onClickListener)
        } else {
            textView.setOnClickListener { alertDialog.dismiss() }
        }
    }

    fun setCustomCancelButtonEx(
        context: Context?,
        alertDialog: AlertDialog,
        str: String?,
        onClickListener: View.OnClickListener?
    ) {
        val textView = alertDialog.findViewById<View?>(R.id.dialog_buttonCancel) as TextView
        if (str != null) {
            textView.text = str
        }
        textView.setOnClickListener { view ->
            
            onClickListener?.onClick(view)
            alertDialog.dismiss()
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

    fun showGlossaryEntry(context: Context, str: String?) {
        try {
            val builder = AlertDialog.Builder(context)
            val rootElement =
                getRootElement(BookManager.getDictionaryEntry(str as kotlin.String?))
            val viewInflate: View =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_glossaryentry,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            setCustomDialogTitle(context, viewInflate, R.string.dialog_dictionaryEntryTitle)
            (viewInflate.findViewById<View?>(R.id.dlgGlossaryEntry_txtTitle) as TextView).text =
                getElementAttribute(rootElement, "name")
            (viewInflate.findViewById<View?>(R.id.dlgGlossaryEntry_txtDescription) as TextView).text =
                rootElement!!.textContent
            val alertDialogCreate = builder.create()
            val onClickListener: View.OnClickListener = View.OnClickListener { alertDialogCreate.dismiss() }
            alertDialogCreate.show()
            setCustomOkButton(context, alertDialogCreate, R.string.dialog_buttonOk, onClickListener)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showLanguageChange(context: Context, str: String?) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(str)
            builder.setPositiveButton(
                R.string.dialog_buttonOk,
                null as DialogInterface.OnClickListener?
            )
            builder.create().show()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun historyTrim(context: Context, str: String?, i: Int) {
        try {
            val builder = AlertDialog.Builder(context)
            val linearLayout =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_okcancel,
                    null as ViewGroup?
                ) as LinearLayout
            builder.setView(linearLayout)
            setCustomDialogTitle(context, linearLayout, R.string.dialog_trimHistoryTitle)
            (linearLayout.findViewById<View?>(R.id.dlgOkCancel_message) as TextView).text = String.format(
                context.getText(R.string.dialog_trimHistoryMsg).toString(),
                str
            )
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            setCustomOkButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonOk
            ) {
                trimHistory(i)
                GameLogic.History!!.loadHistory()
                ProfileManager.saveProfileHistory(context, GameLogic.Profile)
                alertDialogCreate.dismiss()
            }
            setCustomCancelButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonCancel,
                null as View.OnClickListener?
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
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

                        R.id.dlgHistoryJump_opt3 -> historyTrim(context, str, i)
                    }
                    alertDialogCreate.dismiss()
                }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
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
            val textView = viewInflate.findViewById<View?>(R.id.dlgMultiobject_txtValue) as TextView
            textView.text = baseValue.value.toString()
            val onClickListener: View.OnClickListener = View.OnClickListener { view ->
                
                when (view.id) {
                    R.id.dlgMultiobject_btnAdd -> baseValue.add()
                    R.id.dlgMultiobject_btnSubtract -> baseValue.subtract()
                }
                textView.text = baseValue.value.toString()
            }
            (viewInflate.findViewById<View?>(R.id.dlgMultiobject_btnSubtract) as ImageView).setOnClickListener(
                onClickListener
            )
            (viewInflate.findViewById<View?>(R.id.dlgMultiobject_btnAdd) as ImageView).setOnClickListener(
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
            viewFindViewById.visibility = android.view.View.GONE
            viewFindViewById2.visibility = View.GONE
            val onClickListener2: View.OnClickListener = View.OnClickListener { view ->
                
                when (view.id) {
                    R.id.dlgMultiObjects_choiceNo -> {
                        viewFindViewById.visibility = View.GONE
                        viewFindViewById2.visibility = View.GONE
                    }

                    R.id.dlgMultiObjects_choiceYes -> {
                        viewFindViewById.visibility = View.VISIBLE
                        viewFindViewById2.visibility = View.VISIBLE
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
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showFastStartDialog(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val layoutInflater = context.getSystemService("layout_inflater") as LayoutInflater
            val scrollView =
                layoutInflater.inflate(R.layout.dialog_faststart, null as ViewGroup?) as ScrollView
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
            val alertDialogCreate = builder.create()
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
                        for (i in 0..<radioGroup.childCount) {
                            radioButton = radioGroup.getChildAt(i) as RadioButton?
                            if (radioButton!!.isChecked) {
                                break
                            }
                        }
                        ProfileManager.addProfile(
                            context,
                            radioButton!!.text
                                .toString() + context.getString(R.string.txt_FastStart) +
                                    AppUtils.getTimestamp(),
                            radioButton.tag.toString()
                        )
                        requestedChapter = GameConstants.BOOK_CHAPTER_START
                        context.startActivity(
                            Intent(
                                context,
                                NavigatorActivity::class.java as Class<*>
                            )
                        )
                        alertDialogCreate.dismiss()
                    }
                })
            val onClickListener: View.OnClickListener = View.OnClickListener { view ->

                GameLogic.changeFontSize(context, view!!)
                val linearLayout =
                    alertDialogCreate.findViewById<View?>(R.id.dlgFastStart_layContent) as LinearLayout
                linearLayout.removeAllViews()
                try {
                    ChapterFormatter().formatChapter(
                        linearLayout,
                        context,
                        GameConstants.BOOK_CHAPTER_FASTSTART
                    )
                } catch (e: Exception) {
                    e.message?.let { e(Throwable(e),it) }
                }
            }
            (alertDialogCreate.findViewById<View?>(R.id.incFontChgr_btnFontIncrease) as ImageView).setOnClickListener(
                onClickListener
            )
            (alertDialogCreate.findViewById<View?>(R.id.incFontChgr_btnFontDecrease) as ImageView).setOnClickListener(
                onClickListener
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
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
            val rootElement = getRootElement(templates[i])
            val zEqualsIgnoreCase = getElementAttribute(
                rootElement,
                GameConstants.XML_NODE_TEMPLATES_SHEET_ATTR_FASTSTART
            ).equals(GameConstants.STRING_YES, ignoreCase = true)
            if (!z || zEqualsIgnoreCase) {
                val radioButton = layoutInflater.inflate(
                    R.layout.component_listelementprofiletemplate,
                    null as ViewGroup?
                ) as RadioButton
                radioButton.id = AppUtils.generateViewId()
                radioButton.text = getElementAttribute(rootElement, "id")
                radioButton.tag = templates.get(i)
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

    fun showProfilesInterface(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val layoutInflater = context.getSystemService("layout_inflater") as LayoutInflater
            val linearLayout =
                layoutInflater.inflate(R.layout.dialog_profiles, null as ViewGroup?) as LinearLayout
            builder.setView(linearLayout)
            setCustomDialogTitle(context, linearLayout, R.string.dialog_profilesTitle)
            val radioGroup =
                linearLayout.findViewById<View?>(R.id.dlgProfiles_rdgTemplates) as RadioGroup
            getSheetTemplateList(context, radioGroup, layoutInflater, false)
            val alertDialogCreate = builder.create()
            val linearLayout2 =
                linearLayout.findViewById<View?>(R.id.dlgProfiles_layProfiles) as LinearLayout
            linearLayout2.removeAllViews()
            val profiles: ArrayList<Profile> = BookManager.getProfiles() as ArrayList<Profile>
            for (i in profiles.indices) {
                val profile: Profile = profiles[i]
                val linearLayout3 = layoutInflater.inflate(
                    R.layout.component_listelementprofile,
                    null as ViewGroup?
                ) as LinearLayout
                val textView =
                    linearLayout3.findViewById<View?>(R.id.cmpListElementProfile_txtProfile) as TextView
                textView.text = profile.name
                textView.tag = profile
                (linearLayout3.findViewById<View?>(R.id.cmpListElementProfile_btnDelete) as ImageView).setOnClickListener {
                    confirmProfileRemove(
                        context,
                        profile,
                        linearLayout3
                    )
                }
                (linearLayout3.findViewById<View?>(R.id.cmpListElementProfile_btnRename) as ImageView).setOnClickListener {
                    profileRename(
                        context,
                        profile,
                        textView
                    )
                }
                textView.setOnClickListener {
                    ProfileManager.loadProfile(context as Activity, profile)
                    alertDialogCreate.cancel()
                }
                linearLayout2.addView(linearLayout3)
            }
            val editText = linearLayout.findViewById<View?>(R.id.dlgProfiles_txtName) as EditText
            (linearLayout.findViewById<View?>(R.id.dlgProfiles_btnAddProfile) as TextView).setOnClickListener(
                object : View.OnClickListener {

                    override fun onClick(view: View?) {
                        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
                        if (editText.text.toString() == "") {
                            ToastManager.showGenericToast(
                                context,
                                context.getString(R.string.toast_profileNotEmpty)
                            )
                            return
                        }
                        if (checkedRadioButtonId == -1) {
                            ToastManager.showGenericToast(
                                context,
                                context.getString(R.string.toast_selectTemplate)
                            )
                            return
                        }
                        var radioButton: RadioButton? = null
                        for (i2 in 0..<radioGroup.childCount) {
                            radioButton = radioGroup.getChildAt(i2) as RadioButton?
                            if (radioButton!!.isChecked) {
                                break
                            }
                        }
                        ProfileManager.addProfile(
                            context,
                            editText.text.toString(),
                            radioButton!!.tag.toString()
                        )
                        alertDialogCreate.dismiss()
                    }
                })
            alertDialogCreate.show()
            setCustomOkButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonCancel
            ) { alertDialogCreate.dismiss() }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun profileRename(context: Context, profile: Profile, textView: TextView) {
        try {
            val builder = AlertDialog.Builder(context)
            val linearLayout =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_changename,
                    null as ViewGroup?
                ) as LinearLayout
            builder.setView(linearLayout)
            setCustomDialogTitle(context, linearLayout, R.string.dialog_profileRenameTitle)
            val textView2 = linearLayout.findViewById<View?>(R.id.dlgChangeName_txtName) as TextView
            textView2.text = profile.name
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            setCustomOkButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonOk
            ) {
                val string = textView2.text.toString()
                if (string == "") {
                    ToastManager.showGenericToast(
                        context,
                        context.getString(R.string.toast_profileNotEmpty)
                    )
                } else if (ProfileManager.renameProfile(context, profile, string)) {
                    textView.text = string
                } else {
                    ToastManager.showGenericToast(
                        context,
                        context.getString(R.string.toast_profileRenameExisting)
                    )
                }
                alertDialogCreate.dismiss()
            }
            setCustomCancelButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonCancel,
                null as View.OnClickListener?
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun confirmProfileRemove(context: Context, profile: Profile, linearLayout: LinearLayout) {
        try {
            val builder = AlertDialog.Builder(context)
            val linearLayout2 =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_okcancel,
                    null as ViewGroup?
                ) as LinearLayout
            builder.setView(linearLayout2)
            setCustomDialogTitle(context, linearLayout2, R.string.dialog_confirmProfileRemoveTitle)
            (linearLayout2.findViewById<View?>(R.id.dlgOkCancel_message) as TextView).text = String.format(
                context.getText(R.string.dialog_confirmProfileRemoveMessage).toString(),
                profile.name
            )
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            setCustomOkButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonOk
            ) {
                ProfileManager.deleteProfile(context, profile)
                linearLayout.visibility = View.GONE
                alertDialogCreate.dismiss()
            }
            setCustomCancelButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonCancel,
                null as View.OnClickListener?
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun confirmNewGame(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val linearLayout =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_okcancel,
                    null as ViewGroup?
                ) as LinearLayout
            builder.setView(linearLayout)
            setCustomDialogTitle(context, linearLayout, R.string.dialog_confirmNewGameTitle)
            (linearLayout.findViewById<View?>(R.id.dlgOkCancel_message) as TextView).text = String.format(
                context.getText(R.string.dialog_confirmNewGameMessage).toString(),
                *arrayOfNulls<Any>(0)
            )
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            setCustomOkButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonOk
            ) {
                alertDialogCreate.dismiss()
                showProfilesInterface(context)
            }
            setCustomCancelButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonCancel,
                null as View.OnClickListener?
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showCombatNotes(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val viewInflate: View =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_combatnotes,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            setCustomDialogTitle(context, viewInflate, R.string.dialog_alert)
            (viewInflate.findViewById<View?>(R.id.dlgCombatNotes_txtNote) as TextView).text =
                context.getString(R.string.dialog_combatNotes)
            val alertDialogCreate = builder.create()
            val onClickListener: View.OnClickListener = View.OnClickListener {
                alertDialogCreate.dismiss() }
            alertDialogCreate.show()
            setCustomOkButton(context, alertDialogCreate, R.string.dialog_buttonOk, onClickListener)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showCombatExitAlert(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val viewInflate: View =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_combatnotes,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            setCustomDialogTitle(context, viewInflate, R.string.dialog_alert)
            (viewInflate.findViewById<View?>(R.id.dlgCombatNotes_txtNote) as TextView).text =
                context.getString(R.string.dialog_combatExitAlert)
            val alertDialogCreate = builder.create()
            val onClickListener: View.OnClickListener = View.OnClickListener {
                alertDialogCreate.dismiss() }
            alertDialogCreate.show()
            setCustomOkButton(context, alertDialogCreate, R.string.dialog_buttonOk, onClickListener)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showCharacterIsDead(context: Context, str: kotlin.String?) {
        try {
            val builder = AlertDialog.Builder(context)
            val viewInflate: View =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_combatnotes,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            setCustomDialogTitle(context, viewInflate, R.string.dialog_alert)
            (viewInflate.findViewById<View?>(R.id.dlgCombatNotes_txtNote) as TextView).text = str
            val alertDialogCreate = builder.create()
            val onClickListener: View.OnClickListener = View.OnClickListener {
                alertDialogCreate.dismiss() }
            alertDialogCreate.show()
            setCustomOkButton(context, alertDialogCreate, R.string.dialog_buttonOk, onClickListener)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
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
            setCustomOkButton(context, alertDialogCreate, R.string.dialog_buttonOk, onClickListener)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showCombatTriggerWarning(context: NavigatorActivity?, str: String?) {
        try {
            val builder = AlertDialog.Builder(context)
            val viewInflate: View =
                (context!!.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_ok,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            setCustomDialogTitle(context, viewInflate, R.string.dialog_alert)
            (viewInflate.findViewById<View?>(R.id.dlgOk_message) as TextView).text = str
            val alertDialogCreate = builder.create()
            val onClickListener: View.OnClickListener = View.OnClickListener {
                alertDialogCreate.dismiss() }
            alertDialogCreate.show()
            setCustomOkButton(context, alertDialogCreate, R.string.dialog_buttonOk, onClickListener)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showGenericOkCancelDialog(
        context: Context,
        str: kotlin.String?,
        str2: kotlin.String?,
        str3: kotlin.String?,
        onClickListener: View.OnClickListener?,
        str4: kotlin.String?,
        onClickListener2: View.OnClickListener?
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            val linearLayout =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_okcancel,
                    null as ViewGroup?
                ) as LinearLayout
            builder.setView(linearLayout)
            setCustomDialogTitle(context, linearLayout, str)
            (linearLayout.findViewById<View?>(R.id.dlgOkCancel_message) as TextView).text = str2
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            setCustomOkButtonEx(context, alertDialogCreate, str3, onClickListener)
            setCustomCancelButtonEx(context, alertDialogCreate, str4, onClickListener2)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun showGenericDialog(context: Context, str: kotlin.String?, str2: kotlin.String?) {
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
            setCustomOkButtonEx(context, alertDialogCreate, null, null)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}