package rpt.games.transport2147.utils.view.hacking

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily

@Composable
fun StationTerminalScreen(game: TerminalHackingGame, screenTokens: List<TerminalToken>) {

    // Stati per aggiornare la UI quando cambiano
    var usedHacks by remember { mutableStateOf(setOf<Int>()) }
    var outputMessage by remember { mutableStateOf("INSERIRE PASSWORD...") }

    // I classici colori del Pip-Boy
    val terminalGreen = Color(0xFF00FF00)
    val styleBase = SpanStyle(color = terminalGreen, fontFamily = FontFamily.Monospace)
    val styleDim = SpanStyle(color = terminalGreen.copy(alpha = 0.5f), fontFamily = FontFamily.Monospace)

    // 1. Costruiamo il testo formattato
    val annotatedString = buildAnnotatedString {
        screenTokens.forEach { token ->
            when (token) {
                is TerminalToken.Word -> {
                    // Se la parola è stata rimossa, disegniamo i puntini
                    val isRemoved = game.removedDuds.contains(token.text)
                    val displayText = if (isRemoved) ".".repeat(token.text.length) else token.text

                    if (!isRemoved) {
                        // Creiamo una "zona cliccabile" chiamata "WORD"
                        pushStringAnnotation(tag = "WORD", annotation = token.text)
                        withStyle(styleBase) { append(displayText) }
                        pop()
                    } else {
                        // Parola rimossa: opaca e non cliccabile
                        withStyle(styleDim) { append(displayText) }
                    }
                }

                is TerminalToken.HackBracket -> {
                    val isUsed = usedHacks.contains(token.id)

                    if (!isUsed) {
                        // Trucco attivo: zona cliccabile "HACK"
                        pushStringAnnotation(tag = "HACK", annotation = token.id.toString())
                        withStyle(styleBase) { append(token.text) }
                        pop()
                    } else {
                        // Trucco usato: opaco e non cliccabile
                        withStyle(styleDim) { append(token.text) }
                    }
                }

                is TerminalToken.Garbage -> {
                    // Semplice testo di contorno
                    withStyle(styleBase) { append(token.text) }
                }
            }
        }
    }

    // 2. Disegniamo lo schermo e gestiamo i click
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {

        // Log del terminale (Es: "Accesso Negato (2/7)")
        Text(
            text = outputMessage,
            color = terminalGreen,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Tentativi rimanenti: ${"█ ".repeat(game.attemptsLeft)}",
            color = terminalGreen,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Il corpo principale del terminale
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                if (game.isGameOver) return@ClickableText

                // Controlla se abbiamo cliccato su una PAROLA
                annotatedString.getStringAnnotations(tag = "WORD", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        outputMessage = "> ${annotation.item}\n" + game.makeGuess(annotation.item)
                    }

                // Controlla se abbiamo cliccato su un TRUCCO
                annotatedString.getStringAnnotations(tag = "HACK", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val hackId = annotation.item.toInt()
                        usedHacks = usedHacks + hackId // Aggiungiamo l'ID ai trucchi usati
                        outputMessage = "> TRUCCO TROVATO\n" + game.applyBracketHack()
                    }
            }
        )
    }
}