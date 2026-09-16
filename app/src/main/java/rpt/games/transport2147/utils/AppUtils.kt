package rpt.games.transport2147.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import rpt.games.transport2147.R
import rpt.games.transport2147.TransportApplication
import rpt.games.transport2147.utils.managers.SharedPreferencesManager
import rpt.games.transport2147.utils.view.hacking.TerminalHackingGame
import rpt.games.transport2147.utils.view.hacking.TerminalToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern


class AppUtils {

    companion object{

        var _fontDimension: Int = 2130968660
        var _fontDimensionIndex: Int = 2
        var _fontDimensionMaxIndex: Int = 12
        var _fontDimensionMinIndex: Int = 0
        const val USERS_SHARED_PREF : String = "user_pref"
        const val SHOW_INTRO : String = "showIntro"
        const val FONT_SIZE : String = "fontSize"
        const val TEXT_JUSTIFICATION : String = "text_justification"
        var FONT_NAME : String = ""


        fun setCoreFont(context: Context?, str: String?) {
            if (str != null) {
                FONT_NAME = str
            }
        }



        fun calculateLikeness(guess: String, target: String): Int {
            if (guess.length != target.length) return 0

            var likeness = 0
            for (i in guess.indices) {
                if (guess[i].equals(target[i], ignoreCase = true)) {
                    likeness++
                }
            }
            return likeness
        }

        fun setupGame(): TerminalHackingGame {
            // 1. Definisci un dizionario
            val dictionary = listOf(
                "FALLOUT", "TESTING", "WARNING", "HACKING",
                "PLAYING", "TERMINA", "RADIANT", "NUCLEAR"
            )

            // 2. Seleziona N parole casuali (es. 8 parole)
            val selectedWords = dictionary.shuffled().take(8)

            // 3. Scegli la password vincente tra quelle selezionate
            val target = selectedWords.random()

            // 4. Avvia la partita
            return TerminalHackingGame(
                words = selectedWords,
                targetWord = target,
                maxAttempts = 4,
                context = TransportApplication.instance
            )
        }

        fun execSingleRegex(str: String?, str2: String?): String? {
            val matcher: Matcher = Pattern.compile(str2).matcher(str)
            return if (matcher.find()) matcher.group() else ""
        }

        fun execSingleRegex(str: String?, str2: String?, i: Int): String? {
            val matcher: Matcher = Pattern.compile(str2).matcher(str)
            return if (matcher.find()) matcher.group(i) else ""
        }

        fun execMultiMatchRegularExpression(
            str: String,
            str2: String,
            str3: String
        ): MutableMap<String, String> {
            val matcher =
                Pattern.compile("<$str\\s+.*?$str2\\s*=\"(.*?)\"\\s*.*?>.*?</$str>")
                    .matcher(str3)
            val map: HashMap<String, String> = HashMap<String, String>()
            while (matcher.find()) {
                map[matcher.group(1)] = matcher.group()
            }
            return map
        }

        fun changeFontSize(context: Context?, view: View) {
            if (view.id == R.id.incFontChgr_btnFontIncrease) {
                if (_fontDimensionIndex == _fontDimensionMaxIndex) {
                    return
                } else {
                    _fontDimensionIndex++
                }
            } else if (_fontDimensionIndex == _fontDimensionMinIndex) {
                return
            } else {
                _fontDimensionIndex--
            }
            calculateFontSize(context)
            SharedPreferencesManager.fontSize = _fontDimensionIndex
        }

        fun changeFontSize(context: Context?, i: Int) {
            _fontDimensionIndex = i
            calculateFontSize(context)
        }

        private fun calculateFontSize(context: Context?) {
            val typedArrayObtainTypedArray =
                context!!.resources.obtainTypedArray(R.array.font_points)
            _fontDimension = typedArrayObtainTypedArray.getResourceId(_fontDimensionIndex,
                0)
            typedArrayObtainTypedArray.recycle()
        }

        @SuppressLint("SimpleDateFormat")
        fun getTimestamp(): String {
            return SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
        }

        @SuppressLint("NewApi")
        fun generateViewId(): Int {
            return View.generateViewId()
        }

        fun execReplace(str: String, str2: String, str3: String): String {
            return Pattern.compile(str2).matcher(str).replaceAll(str3)
        }
    }

    fun generateTrickBracket(): String {
        val brackets = listOf(
            Pair('<', '>'),
            Pair('(', ')'),
            Pair('[', ']'),
            Pair('{', '}')
        )

        val garbageChars = "!@#$%%^&*_+-=,.;:|~"
        val chosenBracket = brackets.random()

        // Generiamo da 1 a 5 caratteri spazzatura da mettere dentro le parentesi
        val innerGarbageLength = (1..5).random()
        val innerGarbage = (1..innerGarbageLength)
            .map { garbageChars.random() }
            .joinToString("")

        return "${chosenBracket.first}$innerGarbage${chosenBracket.second}"
    }

    val hackRegex = Regex("""(\([^\w()]*\)|\[[^\w\[\]]*\]|\{[^\w{}]*\}|<[^\w<>]*>)""")

    fun findClickableHacks(terminalText: String) {
        val matches = hackRegex.findAll(terminalText)

        for (match in matches) {
            val startPos = match.range.first
            val endPos = match.range.last
            val matchedString = match.value

            println("Trovato trucco: $matchedString da indice $startPos a $endPos")
            // Qui dovrai applicare un ClickableSpan (in XML)
            // o un'annotazione di stringa (in Jetpack Compose) per renderlo cliccabile
        }
    }

    fun generateTerminalScreen(words: List<String>): List<TerminalToken> {
        val screen = mutableListOf<TerminalToken>()
        var hackIdCounter = 0

        // Logica semplificata: alterniamo spazzatura, parole e trucchi
        for (word in words) {
            // Aggiungiamo spazzatura casuale
            screen.add(TerminalToken.Garbage("!@#%^&* "))

            // Ogni tanto aggiungiamo un trucco
            if (Math.random() > 0.5) {
                val bracket = generateTrickBracket() // La funzione che abbiamo visto prima
                screen.add(TerminalToken.HackBracket(bracket, hackIdCounter++))
                screen.add(TerminalToken.Garbage(" "))
            }

            // Aggiungiamo la parola
            screen.add(TerminalToken.Word(word))
            screen.add(TerminalToken.Garbage("\n0xF4B2 ")) // A capo e nuovo indirizzo di memoria
        }

        return screen
    }


}