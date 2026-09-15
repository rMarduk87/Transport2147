package rpt.games.transport2147.utils.view.hacking

import android.content.Context
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.AppUtils.Companion.calculateLikeness

class TerminalHackingGame(
    val words: List<String>,
    val targetWord: String,
    val maxAttempts: Int = 4,
    val context: Context
) {
    // Stato del gioco
    var attemptsLeft = maxAttempts
        private set

    var isGameOver = false
        private set

    var isGameWon = false
        private set

    // Storico dei tentativi: lista di coppie (Parola Provata, Punteggio di Somiglianza)
    val history = mutableListOf<Pair<String, Int>>()

    // Set che contiene le parole sbagliate rimosse dai "trucchi" (i dud)
    val removedDuds = mutableSetOf<String>()

    /**
     * Chiamata quando l'utente seleziona una parola dal terminale.
     * Restituisce il messaggio da mostrare a schermo.
     */
    fun makeGuess(guess: String): String {
        // Se il gioco è già finito, blocca ulteriori tentativi
        if (isGameOver) {
            return context.getString(R.string.errore_terminale_bloccato)
        }

        // Se l'utente clicca su una parola già rimossa da un trucco, la ignoriamo
        if (removedDuds.contains(guess)) {
            return context.getString(R.string.errore_parola_gi_scartata)
        }

        // Calcola il punteggio (Likeness)
        val likeness = calculateLikeness(guess, targetWord)
        history.add(Pair(guess, likeness))

        // Condizione di Vittoria
        if (likeness == targetWord.length) {
            isGameOver = true
            isGameWon = true
            return "ACCESSO CONSENTITO"
        }

        // Se sbagliamo, togliamo un tentativo
        attemptsLeft--

        // Condizione di Sconfitta
        if (attemptsLeft <= 0) {
            isGameOver = true
            return "ACCESSO NEGATO. Blocco di sicurezza attivato."
        }

        // Tentativo fallito ma gioco ancora aperto
        return "Accesso Negato ($likeness/${targetWord.length} corretti)"
    }

    /**
     * Chiamata quando l'utente clicca su un blocco di parentesi magiche (es: <#@%>)
     * Restituisce il risultato dell'hack da mostrare nel log.
     */
    fun applyBracketHack(): String {
        if (isGameOver) {
            return "ERRORE: Terminale bloccato."
        }

        // 20% di probabilità di ripristinare i tentativi, 80% di rimuovere una parola falsa
        val isReset = Math.random() < 0.2

        return if (isReset) {
            attemptsLeft = maxAttempts
            "TENTATIVI RIPRISTINATI."
        } else {
            removeRandomDud()
        }
    }

    /**
     * Rimuove una parola sbagliata casuale e la aggiunge all'elenco dei dud.
     */
    private fun removeRandomDud(): String {
        // Filtriamo per trovare le parole che NON sono la password e NON sono già state rimosse
        val availableDuds = words.filter { it != targetWord && !removedDuds.contains(it) }

        if (availableDuds.isEmpty()) {
            return "ERRORE: Nessuna parola falsa rimanente."
        }

        // Ne scegliamo una a caso e la aggiungiamo alla lista di quelle rimosse
        val dudToRemove = availableDuds.random()
        removedDuds.add(dudToRemove)

        return "DUD RIMOSSO."
    }

    /**
     * Calcola quanti caratteri sono uguali E nella stessa posizione
     * tra la parola tentata e la password bersaglio.
     */
    private fun calculateLikeness(guess: String, target: String): Int {
        if (guess.length != target.length) return 0

        var likeness = 0
        for (i in guess.indices) {
            if (guess[i].equals(target[i], ignoreCase = true)) {
                likeness++
            }
        }
        return likeness
    }
}