package rpt.games.transport2147.utils.view.hacking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import rpt.games.transport2147.R

@Composable
fun StationTerminalScreen(game: TerminalHackingGame, screenTokens: List<TerminalToken>) {

    // Stati per aggiornare la UI quando cambiano
    var usedHacks by remember { mutableStateOf(setOf<Int>()) }
    val initialPrompt = stringResource(R.string.terminal_prompt)
    var outputMessage by remember { mutableStateOf(initialPrompt) }

    // I classici colori del Pip-Boy
    val terminalGreen = colorResource(R.color.terminal_green)
    val styleBase = SpanStyle(color = terminalGreen, fontFamily = FontFamily.Monospace)
    val styleDim = SpanStyle(color = terminalGreen.copy(alpha = 0.5f), fontFamily = FontFamily.Monospace)

    val hackFoundText = stringResource(R.string.terminal_hack_found)

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
    Column(modifier = Modifier.fillMaxSize().background(colorResource(R.color.terminal_background)).padding(16.dp)) {

        // Log del terminale (Es: "Accesso Negato (2/7)")
        Text(
            text = outputMessage,
            color = terminalGreen,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.terminal_attempts_left) + "█ ".repeat(game.attemptsLeft),
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
                        outputMessage = "> $hackFoundText\n" + game.applyBracketHack()
                    }
            }
        )
    }
}