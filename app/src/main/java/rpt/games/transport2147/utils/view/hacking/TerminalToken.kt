package rpt.games.transport2147.utils.view.hacking

sealed class TerminalToken {
    // Una parola giocabile
    data class Word(val text: String) : TerminalToken()

    // Un trucco di parentesi cliccabile. L'ID serve per disattivarlo dopo l'uso.
    data class HackBracket(val text: String, val id: Int) : TerminalToken()

    // Testo casuale che non fa nulla
    data class Garbage(val text: String) : TerminalToken()
}