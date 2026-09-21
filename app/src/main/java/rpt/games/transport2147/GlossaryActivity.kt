package rpt.games.transport2147

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Element
import rpt.com.base.log.e
import rpt.games.transport2147.databinding.ActivityGlossaryBinding
import rpt.games.transport2147.databinding.ActivityMainMenuBinding
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.LocaleHelper.onAttach
import rpt.games.transport2147.utils.managers.BookManager
import rpt.games.transport2147.utils.view.game.GameLogic
import rpt.games.transport2147.utils.xml.XmlUtility.getElementAttribute
import rpt.games.transport2147.utils.xml.XmlUtility.getRootElement


class GlossaryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGlossaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_activityGlossary)
        GameLogic.checkForAppRecovery(this, true)
        binding = ActivityGlossaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // android.app.Activity
    override fun onResume() {
        try {
            super.onResume()
            loadGlossary()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    // android.app.Activity
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == 16908332) {
            val intent = Intent(
                this,
                (if (intent.extras!!
                        .getString(GameConstants.GLOSSARY_HOME) == "MAIN"
                ) MainMenuActivity::class.java else NavigatorActivity::class.java) as Class<*>
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(menuItem)
    }
    
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(onAttach(context))
    }

    fun onChangeFontSize(view: View?) {
        try {
            GameLogic.changeFontSize(this, view!!)
            loadGlossary()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun loadGlossary() {
        try {
            val linearLayout = findViewById<View?>(R.id.actGlossary_mainLayout) as LinearLayout
            linearLayout.removeAllViews()
            val dictionaryEntries: ArrayList<String?> =
                BookManager.getDictionaryEntries()
            val layoutInflater = getSystemService("layout_inflater") as LayoutInflater
            val it2 = dictionaryEntries.iterator()
            while (it2.hasNext()) {
                val rootElement: Element? = getRootElement(it2.next())
                val linearLayout2 = layoutInflater.inflate(
                    R.layout.component_glossaryentry,
                    null as ViewGroup?
                ) as LinearLayout
                val textView =
                    linearLayout2.findViewById<View?>(R.id.dlgGlossaryEntry_txtTitle) as TextView
                textView.text = getElementAttribute(rootElement, "name")
                val textView2 =
                    linearLayout2.findViewById<View?>(R.id.dlgGlossaryEntry_txtDescription) as TextView
                textView2.text = rootElement!!.textContent
                textView.setTextSize(
                    2,
                    resources.getDimension(GameLogic._fontDimension) + 4.0f
                )
                textView.setTypeface(textView.typeface, Typeface.BOLD)
                textView2.setTextSize(2, resources.getDimension(GameLogic._fontDimension))
                linearLayout.addView(linearLayout2)
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}