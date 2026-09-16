package rpt.games.transport2147.utils.view.chapter

import android.content.Context
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import org.w3c.dom.Element
import org.w3c.dom.Text
import rpt.com.base.log.e
import rpt.games.transport2147.utils.AppUtils
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.enums.GenderEnum
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.xml.XmlUtility
import java.io.IOException
import java.util.Locale
import kotlin.math.min
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.data.appmodels.Enemy
import rpt.games.transport2147.utils.managers.ActionMenuManager
import rpt.games.transport2147.utils.managers.CombatManager
import rpt.games.transport2147.utils.managers.DialogManager
import rpt.games.transport2147.utils.managers.ImageManager
import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import rpt.games.transport2147.utils.managers.SheetManager
import rpt.games.transport2147.utils.managers.ToastManager
import rpt.games.transport2147.utils.view.string.NeutralClickableSpan
import rpt.games.transport2147.utils.view.string.RPSpannableString
import rpt.games.transport2147.utils.view.text.RPTextView2

class ChapterFormatter {
    var lastSummary: String? = null

    @Throws(Exception::class)
    fun formatChapter(linearLayout: LinearLayout?, context: Context, str: String?): Boolean {
        try {
            val strExecReplace: String = AppUtils.execReplace(
                BookManager.getChapter(str),
                GameConstants.REGEX_GENDER_SEARCH,
                if (GameLogic.PlayerSheet.getGender() === GenderEnum.MALE) 
                    GameConstants.REGEX_GENDER_REPLACE_MALE 
                else GameConstants.REGEX_GENDER_REPLACE_FEMALE
            )
            val rootElement: Element? = XmlUtility.getRootElement(strExecReplace)
            var attribute = rootElement!!.getAttribute(GameConstants.XML_NODE_CHAPTER_ATTR_DREAM)
            if (attribute == null) {
                attribute = "false"
            }
            this.lastSummary =
                AppUtils.execSingleRegex(strExecReplace, GameConstants.REGEX_FIND_CHAPTER_SUMMARY)
            createChapter(linearLayout!!, context, rootElement)
            return attribute.toBoolean()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
        return false
    }

    @Throws(Exception::class)
    fun formatHistory(linearLayout: LinearLayout, context: Context) {
        try {
            val stringBuffer = StringBuffer()
            var i = 0
            for (i2 in 0..<History.getSize()) {
                val historyElement: HistoryElement = History.getVisitedChapters().get(i2)
                if (historyElement.tracked) {
                    val map: HashMap<*, *> = HashMap<Any?, Any?>()
                    map.put(GameConstants.XML_NODE_HLINK_ATTR_SEQUENCE, i.toString())
                    stringBuffer.append(
                        XmlUtility.formatNode(
                            GameConstants.XML_NODE_HLINK,
                            historyElement.chapter,
                            map
                        ) + " " + historyElement.summary + "\r\n"
                    )
                }
                i++
            }
            val str: String = XmlUtility.formatNode(
                "title",
                context.getString(R.string.history_Title),
                null
            ) + XmlUtility.formatNode(GameConstants.XML_NODE_P, stringBuffer.toString(), null)
            HashMap<Any?, Any?>()["name"] = context.getString(R.string.txt_History)
            createChapter(
                linearLayout,
                context,
                
                XmlUtility.getRootElement(XmlUtility.formatNode("chapter", 
                    str, null))
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    private fun getTextView(context: Context): TextView {
        val textView: TextView =
            (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                R.layout.component_paragraph,
                null as ViewGroup?
            ) as TextView
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setTextSize(2, context.resources.
        getDimension(AppUtils._fontDimension))
        textView.isEnabled = true
        return textView
    }

    private fun getTable(context: Context): TableLayout {
        return (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
            R.layout.component_table,
            null as ViewGroup?
        ) as TableLayout
    }
    
    @Throws(Exception::class, IOException::class)
    private fun createChapter(linearLayout: LinearLayout, context: Context, element: Element?) {
        var view: View?
        var z: Boolean
        var _list: View?
        val childNodes = element!!.childNodes
        val textJustification: Boolean = SharedPreferencesManager.textJustification
        var simpleNode: RPSpannableString? = null
        var view2: View? = null
        for (i in 0..<childNodes.length) {
            val nodeItem = childNodes.item(i)
            if (nodeItem is Element) {
                val element2 = nodeItem
                var z2 = true
                when (element2.tagName.lowercase(Locale.getDefault())) {
                    "title" -> {
                        simpleNode = formatSimpleNode(element2, context)
                        view = view2
                        z = false
                    }
                    "p" -> {
                        simpleNode = format_P(element2, context)
                        view = view2
                        z = false
                    }
                    "list" -> {
                        _list = format_LIST(element2, context)
                        view = _list
                        z = true
                        z2 = false
                    }
                    "enemyref" -> {
                        _list = format_ENEMYREF(element2, context)
                        view = _list
                        z = true
                        z2 = false
                    }
                    "image" -> {
                        _list = format_IMAGE(element2, context)
                        view = _list
                        z = true
                        z2 = false
                    }
                    "summary" -> {
                        view = view2
                        z = false
                        z2 = false
                    }
                    else -> {
                        view = view2
                        z = false
                        z2 = false
                    }
                }
                if (z2) {
                    val textView: TextView = getTextView(context)
                    textView.setText(simpleNode, TextView.BufferType.SPANNABLE)
                    textView.gravity = simpleNode!!.gravity
                    if (simpleNode.gravity == 8388611 && textJustification && (textView is RPTextView2)) {
                        (textView as RPTextView2).justification = true
                    }
                    linearLayout.addView(textView)
                }
                if (z) {
                    linearLayout.addView(view)
                }
                view2 = view
            }
        }
    }
    
    @Throws(Exception::class)
    private fun parseInnerTag(element: Element, context: Context): RPSpannableString? {
        return when (element.tagName.lowercase(Locale.getDefault())) {
            "bold", "italic", "quote" -> formatSimpleNode(element, context)
            "link" -> format_LINK(element, context)
            "hlink" -> format_HLINK(element, context)
            "smallcaps" -> format_SMALLCAPS(element, context)
            "objectref" -> format_OBJECTREF(element, context)
            "sidepiece" -> format_SIDEPIECE(element, context)
            "glossary" -> format_GLOSSARY(element, context)
            "br" -> format_BR(element, context)
            "xp" -> format_XP(element, context)
            "abilitychange" -> format_ABILITYCHANGE(element, context)
            "jump" -> format_JUMP(element, context)
            else -> RPSpannableString("")
        }
    }

    @Throws(Exception::class)
    private fun loopInnerElements(element: Element?, context: Context): RPSpannableString {
        var z: Boolean = false
        val childNodes = element!!.childNodes
        var spannableString: RPSpannableString? = null
        var spannableString: RPSpannableString = RPSpannableString("")
        for (i in 0..<childNodes.length) {
            val nodeItem = childNodes.item(i)
            if (nodeItem is Element) {
                spannableString = parseInnerTag(nodeItem, context)
            } else {
                if (nodeItem is Text) {
                    val nodeValue = nodeItem.nodeValue
                    z = nodeValue != ""
                    spannableString = RPSpannableString(nodeValue)
                }
                if (z) {
                    spannableString =
                        RPSpannableString(TextUtils.concat(spannableString,
                            spannableString))
                }
            }
            z = true
            spannableString =
                RPSpannableString(TextUtils.concat(spannableString,
                    spannableString))
        }
        return spannableString
    }

    @Throws(Exception::class)
    private fun format_BR(element: Element?, context: Context?): RPSpannableString {
        return RPSpannableString(GameConstants.STRING_NEWLINE)
    }

    @Throws(Exception::class, IOException::class)
    private fun format_IMAGE(element: Element, context: Context): View {
        val elementAttribute: String? = XmlUtility.getElementAttribute(element, "id")
        val z = element.getAttribute(GameConstants.XML_NODE_IMAGE_ATTR_ZOOM).toBoolean()
        val imageView = (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
            R.layout.component_image,
            null as ViewGroup?
        ) as ImageView
        imageView.setImageBitmap(
            ImageManager.getScaledBitmapFromAsset(
                context,
                java.lang.String.format(GameConstants.IMAGE_PARAGRAPH, elementAttribute),
                0,
                0
            )
        )
        if (z) {
            imageView.setOnClickListener {
                val intent: Intent = Intent(context, Activity_ImageZoom::class.java as Class<*>)
                intent.putExtra(
                    GameConstants.IMAGE_ZOOM_SOURCE,
                    java.lang.String.format(GameConstants.IMAGE_ZOOM, elementAttribute)
                )
                context.startActivity(intent)
            }
        }
        return imageView
    }

    @Throws(Exception::class)
    private fun format_LIST(element: Element, context: Context): View {
        var spannableString: RPSpannableString?
        val table: TableLayout = getTable(context)
        val elementsByTagName = element.getElementsByTagName(GameConstants.XML_NODE_LIST_ELEMENT)
        for (i in 0..<elementsByTagName.length) {
            val firstSubNode: Element? = XmlUtility.getFirstSubNode(
                elementsByTagName.item(i) as Element?,
                GameConstants.XML_NODE_LIST_ITEM
            )
            val firstSubNode2: Element? = XmlUtility.getFirstSubNode(
                elementsByTagName.item(i) as Element?,
                GameConstants.XML_NODE_LIST_DESCRIPTION
            )
            val spannableStringExLoopInnerElements: RPSpannableString =
                loopInnerElements(firstSubNode, context)
            if (firstSubNode2 != null) {
                spannableString = loopInnerElements(firstSubNode2, context)
            } else {
                spannableString = RPSpannableString("")
            }
            table.addView(
                setTableRow(
                    context,
                    true,
                    spannableStringExLoopInnerElements,
                    spannableString,
                    false
                )
            )
        }
        table.addView(setTableRow(context, true,
            RPSpannableString(""), null, false))
        return table
    }

    @Throws(Exception::class)
    private fun format_GLOSSARY(element: Element, context: Context): RPSpannableString {
        val spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        val attribute = element.getAttribute("id")
        val span: RPSpannableString =
            formatSpan(spannableStringExLoopInnerElements, GameConstants.XML_NODE_GLOSSARY)
        span.setSpan(object : NeutralClickableSpan() {
            override fun onClick(p0: View) {
                DialogManager.showGlossaryEntry(context, attribute)
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_ENEMYREF(element: Element, context: Context): View? {
        var spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        try {
            val enemy: Enemy = Enemy(context, XmlUtility.getElementAttribute(element, "id"))
            var spannableString: RPSpannableString = RPSpannableString("")
            if (enemy.getNoteNode() != null) {
                spannableString =
                    RPSpannableString(loopInnerElements(enemy.getNoteNode(), context))
            }
            val spannableString: RPSpannableString = spannableString
            if (spannableStringExLoopInnerElements.toString() == "") {
                spannableStringExLoopInnerElements = RPSpannableString(enemy.name)
            }
            val span: RPSpannableString = formatSpan(
                applySmallCapsFormatting(spannableStringExLoopInnerElements),
                GameConstants.XML_NODE_ENEMYREF
            )
            span.setSpan(object : NeutralClickableSpan() {

                override fun onClick(p0: View) {
                    CombatManager.activateEnemy(context, enemy)
                    ToastManager.showGenericToast(
                        context,
                        context.getString(R.string.toast_msgStartCombat)
                    )
                }
            }, 0, span.length, 0)
            val table: TableLayout = getTable(context)
            table.addView(setTableRow(context, false, span, null, false))
            table.addView(
                setTableRow(
                    context,
                    false,
                    RPSpannableString(
                        context.getString(
                            R.string.enemyref_valor,
                            Integer.valueOf(enemy.getValor())
                        )
                    ),
                    RPSpannableString(
                        context.getString(
                            R.string.enemyref_craft,
                            Integer.valueOf(enemy.getCraft())
                        )
                    ),
                    false
                )
            )
            table.addView(
                setTableRow(
                    context,
                    false,
                    RPSpannableString(
                        context.getString(
                            R.string.enemyref_pe,
                            Integer.valueOf(enemy.getPhisicalEnergy())
                        )
                    ),
                    RPSpannableString(
                        context.getString(
                            R.string.enemyref_me,
                            Integer.valueOf(enemy.getMentalEnergy())
                        )
                    ),
                    false
                )
            )
            table.addView(
                setTableRow(
                    context,
                    false,
                    RPSpannableString(
                        context.getString(
                            R.string.enemyref_damage,
                            Integer.valueOf(enemy.getDamage())
                        )
                    ),
                    RPSpannableString(
                        if (enemy.getProtection() === 0) "" else context.getString(
                            R.string.enemyref_protection,
                            Integer.valueOf(enemy.getProtection())
                        )
                    ),
                    false
                )
            )
            if (spannableString.toString() != "") {
                table.addView(setTableRow(context, false, 
                    spannableString, null, true))
            }
            table.addView(
                setTableRow(
                    context,
                    false,
                    formatSpan(
                        RPSpannableString(
                            context.getString(R.string.enemyref_addNote)),
                        GameConstants.XML_NODE_COMBATNOTES
                    ),
                    null,
                    false
                )
            )
            table.addView(setTableRow(context, false, 
                RPSpannableString(""), null, false))
            return table
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
        return null
    }

    private fun setTableRow(
        context: Context,
        z: Boolean,
        spannableString: RPSpannableString?,
        spannableString: RPSpannableString?,
        z2: Boolean
    ): TableRow {
        val i: Int
        val layoutInflater: LayoutInflater =
            context.getSystemService("layout_inflater") as LayoutInflater
        if (spannableString == null) {
            i = R.layout.component_tablerow1cell
        } else {
            i =
                if (z) R.layout.component_tablerow2cells_market else R.layout.component_tablerow2cells
        }
        val tableRow = layoutInflater.inflate(i, null as ViewGroup?) as TableRow
        val textView: TextView = tableRow.findViewById<View?>(R.id.cmpTableRow_txtCell1) as TextView
        if (z2 && (textView is RPTextView2) && SharedPreferencesManager.textJustification) {
            (textView as RPTextView2).justification = true
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        textView.setTextSize(2, context.resources.getDimension(
            AppUtils._fontDimension))
        if (spannableString != null) {
            val textView2: TextView =
                tableRow.findViewById<View?>(R.id.cmpTableRow_txtCell2) as TextView
            textView2.movementMethod = LinkMovementMethod.getInstance()
            textView2.setText(spannableString, TextView.BufferType.SPANNABLE)
            textView2.setTextSize(
                2,
                context.resources.getDimension(AppUtils._fontDimension)
            )
        }
        return tableRow
    }

    @Throws(Exception::class)
    private fun format_LINK(element: Element, context: Context): RPSpannableString {
        val string: String?
        val spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        if (element.hasAttribute("name")) {
            string = element.getAttribute("name")
        } else {
            string = spannableStringExLoopInnerElements.toString()
        }
        val span: RPSpannableString =
            formatSpan(spannableStringExLoopInnerElements, GameConstants.XML_NODE_LINK)
        span.setSpan(object : NeutralClickableSpan() {

            override fun onClick(p0: View) {
                ActionMenuManager.actionLoadChapter(context, string)
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_HLINK(element: Element, context: Context): RPSpannableString {
        val string: String?
        val spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        if (element.hasAttribute("name")) {
            string = element.getAttribute("name")
        } else {
            string = spannableStringExLoopInnerElements.toString()
        }
        val span: RPSpannableString =
            formatSpan(spannableStringExLoopInnerElements, GameConstants.XML_NODE_HLINK)
        val i =
            XmlUtility.getElementAttribute(element, 
                GameConstants.XML_NODE_HLINK_ATTR_SEQUENCE)!!.toInt()
        span.setSpan(object : NeutralClickableSpan() {
            override fun onClick(p0: View) {
                DialogManager.historyJump(context, string, i)
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_JUMP(element: Element, context: Context): RPSpannableString {
        val spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        val attribute = element.getAttribute("type")
        val attribute2 = element.getAttribute(GameConstants.XML_NODE_JUMP_ATTR_TARGET)
        val span: RPSpannableString =
            formatSpan(spannableStringExLoopInnerElements, GameConstants.XML_NODE_JUMP)
        span.setSpan(object : NeutralClickableSpan() {
            override fun onClick(p0: View) {
                if (attribute.equals(
                        GameConstants.JUMP_TYPE_ACTIVITY,
                        ignoreCase = true
                    ) && attribute2.equals(GameConstants.JUMP_TARGET_MAIN, ignoreCase = true)
                ) {
                    context.startActivity(
                        Intent(
                            context,
                            Activity_MainMenu::class.java as Class<*>
                        )
                    )
                    GameLogic.Navigator.finish()
                }
                if (attribute.equals(
                        GameConstants.JUMP_TYPE_ACTIVITY,
                        ignoreCase = true
                    ) && attribute2.equals(GameConstants.JUMP_TARGET_MAIN2, ignoreCase = true)
                ) {
                    context.startActivity(Intent(context, Activity_Main2::class.java as Class<*>))
                    GameLogic.Navigator.finish()
                }
                if (attribute.equals(GameConstants.JUMP_TYPE_PLAYSTORE, ignoreCase = true)) {
                    SocialMgr.openGooglePlay(context, attribute2)
                }
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_OBJECTREF(element: Element, context: Context): RPSpannableString {
        val span: RPSpannableString
        var spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        val elementAttribute: String? = XmlUtility.getElementAttribute(element, "id")
        var elementAttribute2: String? = XmlUtility.getElementAttribute(element, "quantity")
        if (elementAttribute2 == null) {
            elementAttribute2 = "1"
        }
        val elementAttribute3: String? =
            XmlUtility.getElementAttribute(element, GameConstants.XML_NODE_OBJECTREF_ATTR_PRICE)
        val boolValueOf = elementAttribute3 != null && elementAttribute3.equals(
            GameConstants.STRING_YES,
            ignoreCase = true
        )
        val playerObject: PlayerObject =
            PlayerObject(elementAttribute, elementAttribute2.toInt(), History.getRequestedChapter())
        if (playerObject.getOriginalType() === ObjectTypeEnum.ARMOR || playerObject.getOriginalType() === ObjectTypeEnum.WEAPON) {
            playerObject.setType(ObjectTypeEnum.ITEM)
        }
        if (spannableStringExLoopInnerElements.toString().equals("")) {
            spannableStringExLoopInnerElements = RPSpannableString(playerObject.getName())
        }
        if (playerObject.getType() === ObjectTypeEnum.CODE) {
            span = formatSpan(spannableStringExLoopInnerElements, GameConstants.XML_NODE_OBJECTREF_CODE)
        } else {
            span = formatSpan(spannableStringExLoopInnerElements, GameConstants.XML_NODE_OBJECTREF)
        }
        span.setSpan(object : NeutralClickableSpan() {
            override fun onClick(p0: View) {
                if (!GameLogic.isObjectTaken(playerObject)) {
                    SheetManager.addObject(playerObject)
                    GameLogic.AddTakenObject(playerObject)
                    ToastManager.showGenericToast(
                        context,
                        context.getString(
                            if (boolValueOf) R.string.toast_msgGotObjectWithPrice else R.string.toast_msgGotObject,
                            Integer.valueOf(playerObject.getQuantity()),
                            playerObject.getName()
                        )
                    )
                    return
                }
                DialogManager.takeMultiObject(context, playerObject.m4clone())
            }
        }, 0, span.length, 0)
        return span
    }

    /* JADX WARN: switch over string: strings are not added: [[left]] */
    @Throws(Exception::class)
    private fun format_P(element: Element, context: Context): RPSpannableString {
        val spannableStringExLoopInnerElements: RPSpannableString =
            loopInnerElements(element, context)
        val elementAttribute: String? =
            XmlUtility.getElementAttribute(element, GameConstants.XML_NODE_P_ATTR_ALIGN)
        var i: Int = GravityCompat.START
        if (elementAttribute != null) {
            when (elementAttribute) {
                "center" -> i = 17
                "right" -> i = GravityCompat.END
            }
        }
        spannableStringExLoopInnerElements.gravity = i
        return spannableStringExLoopInnerElements
    }

    @Throws(Exception::class)
    private fun formatSimpleNode(element: Element, context: Context): RPSpannableString {
        return formatSpan(
            loopInnerElements(element, context),
            element.nodeName,
            context = context
        )
    }

    @Throws(Exception::class)
    private fun format_SIDEPIECE(element: Element, context: Context): RPSpannableString {
        val span: RPSpannableString =
            formatSpan(
                loopInnerElements(element, context),
                GameConstants.XML_NODE_SIDEPIECE,
                context = context
            )
        span.setSpan(object : NeutralClickableSpan() {

            override fun onClick(p0: View) {
                Toast.makeText(context, context.getString(R.string.toast_msgTickAdded), 1).show()
                SheetManager.addTime(context)
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_ABILITYCHANGE(element: Element, context: Context): RPSpannableString {
        val span: RPSpannableString =
            formatSpan(
                loopInnerElements(element, context),
                GameConstants.XML_NODE_ABILITYCHANGE,
                context = context
            )
        val elementAttribute: String? = XmlUtility.getElementAttribute(element, "ability")
        val i = XmlUtility.getElementAttribute(element, "quantity")!!.toInt()
        val elementAttribute2: String? =
            XmlUtility.getElementAttribute(element,
                GameConstants.XML_NODE_ABILITYCHANGE_ATTR_OPERATION)
        span.setSpan(object : NeutralClickableSpan() {
            override fun onClick(p0: View) {
                ToastManager.showGenericToast(
                    context,
                    context.getString(R.string.toast_abilityModified)
                )
                SheetManager.changeAbility(context, elementAttribute, i, elementAttribute2)
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_XP(element: Element, context: Context): RPSpannableString {
        val span: RPSpannableString =
            formatSpan(
                loopInnerElements(element, context),
                GameConstants.XML_NODE_EXPERIENCE,
                context = context
            )
        var elementAttribute: String? =
            XmlUtility.getElementAttribute(element, 
                GameConstants.XML_NODE_EXPERIENCE_ATTR_AMOUNT)
        if (elementAttribute == null) {
            elementAttribute = "1"
        }
        val i = elementAttribute.toInt()
        span.setSpan(object : NeutralClickableSpan() {
  
            override fun onClick(p0: View) {
                ToastManager.showGenericToast(
                    context,
                    context.getString(R.string.toast_msgExperienceAdded, i)
                )
                SheetManager.addExperience(context, i)
            }
        }, 0, span.length, 0)
        return span
    }

    @Throws(Exception::class)
    private fun format_SMALLCAPS(element: Element, context: Context): RPSpannableString {
        return applySmallCapsFormatting(loopInnerElements(element, context))
    }

    private fun applySmallCapsFormatting(spannableString: RPSpannableString): RPSpannableString {
        val string: String = GameLogic.Navigator.getString(R.string.lowCaseChars)
        val string2: String = GameLogic.Navigator.getString(R.string.uppCaseChars)
        if (spannableString.toString() == "") {
            return spannableString
        }
        val spans: Array<Any?> =
            spannableString.getSpans(0,
                spannableString.length, Any::class.java)
        val charArray = spannableString.toString().toCharArray()
        val iArr = IntArray(charArray.size)
        val iArr2 = IntArray(charArray.size)
        var i = 0
        var z = false
        for (i2 in charArray.indices) {
            val iIndexOf = string.indexOf(charArray[i2])
            if (iIndexOf > -1) {
                if (!z) {
                    iArr[i] = i2
                    z = true
                }
                charArray[i2] = string2[iIndexOf]
            } else if (z) {
                iArr2[i] = i2
                i++
                z = false
            }
        }
        iArr2[i] = charArray.size
        val spannableString: RPSpannableString =
            RPSpannableString(String(charArray))
        for (i3 in spans.indices) {
            spannableString.setSpan(
                spans[i3],
                spannableString.getSpanStart(spans[i3]),
                spannableString.getSpanEnd(spans[i3]),
                spannableString.getSpanFlags(spans[i3])
            )
        }
        spannableString.setSpan(RelativeSizeSpan(1.1f), 0,
            spannableString.length, 0)
        for (i4 in 0..<min(iArr.size, iArr2.size)) {
            spannableString.setSpan(RelativeSizeSpan(0.9f),
                iArr[i4], iArr2[i4], 34)
        }
        return spannableString
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */ /* JADX WARN: Code duplicated, block: B:50:0x00b3  */
    private fun formatSpan(spannableString: RPSpannableString, str: String, context: Context): RPSpannableString {
        val arrayList: ArrayList<*> = ArrayList<Any?>()
        when (str) {
            "bold" -> arrayList.add(StyleSpan(1))
            "hlink", "jump", "link" -> {
                arrayList.add(UnderlineSpan())
                arrayList.add(StyleSpan(1))
                arrayList.add(
                    ForegroundColorSpan(
                        context.getResources().getColor(R.color.paragraph_link)
                    )
                )
            }

            "glossary" -> arrayList.add(StyleSpan(1))
            "quote" -> arrayList.add(RelativeSizeSpan(0.9f))
            "italic" -> arrayList.add(StyleSpan(2))
            "combatnotes" -> {
                arrayList.add(StyleSpan(2))
                arrayList.add(RelativeSizeSpan(0.8f))
            }

            "xp", "sidepiece" -> {
                arrayList.add(UnderlineSpan())
                arrayList.add(StyleSpan(1))
                arrayList.add(ForegroundColorSpan(ViewCompat.MEASURED_STATE_MASK))
            }

            "title" -> {
                arrayList.add(RelativeSizeSpan(1.5f))
                arrayList.add(StyleSpan(1))
                spannableString.gravity = 1
            }

            "objectref_code", "enemyref", "objectref", "abilitychange" -> {
                arrayList.add(UnderlineSpan())
                arrayList.add(StyleSpan(1))
                arrayList.add(ForegroundColorSpan(ViewCompat.MEASURED_STATE_MASK))
            }
        }
        for (i in arrayList.indices) {
            spannableString.setSpan(
                arrayList[i], 0,
                spannableString.length, 0)
        }
        return spannableString
    }
}