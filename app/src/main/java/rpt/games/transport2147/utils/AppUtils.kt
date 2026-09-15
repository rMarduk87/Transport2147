package rpt.games.transport2147.utils

import rpt.games.transport2147.TransportApplication
import rpt.games.transport2147.utils.view.hacking.TerminalHackingGame
import rpt.games.transport2147.utils.view.hacking.TerminalToken

class AppUtils {

    companion object{
        const val USERS_SHARED_PREF : String = "user_pref"
        const val SHOW_INTRO : String = "showIntro"



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