# Implementation Plan - Extract Hardcoded Strings and Colors

Extract user-facing strings and hardcoded colors from the source code into Android resource files (`strings.xml` and `colors.xml`) for better maintainability and localization.

## User Review Required

> [!NOTE]
> Some strings like "WORD" and "HACK" in `StationTerminal.kt` are used as internal tags for `AnnotatedString` and will remain hardcoded as they are not UI text.

## Proposed Changes

### Resources

#### [MODIFY] [colors.xml](file:///C:/Users/Riccardo.Pezzolati/Transport2147/app/src/main/res/values/colors.xml)
- Add `terminal_green` (#00FF00).
- Add `terminal_background` (#000000).

#### [MODIFY] [strings.xml](file:///C:/Users/Riccardo.Pezzolati/Transport2147/app/src/main/res/values/strings.xml)
- Add English (default) versions of terminal strings.

#### [MODIFY] [strings.xml](file:///C:/Users/Riccardo.Pezzolati/Transport2147/app/src/main/res/values-it/strings.xml)
- Add Italian versions of terminal strings.

### UI Components

#### [MODIFY] [StationTerminal.kt](file:///C:/Users/Riccardo.Pezzolati/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/hacking/StationTerminal.kt)
- Replace hardcoded colors with `colorResource(R.color.terminal_green)` etc.
- Replace hardcoded strings with `stringResource(R.string.terminal_...)`.

#### [MODIFY] [TerminalHackingGame.kt](file:///C:/Users/Riccardo.Pezzolati/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/hacking/TerminalHackingGame.kt)
- Replace hardcoded strings in `makeGuess` and `applyBracketHack` using `context.getString(R.string.terminal_...)`.

## Verification Plan

### Automated Tests
- Run `:app:compileDebugKotlin` to ensure all resource references are correct.

### Manual Verification
- Deploy the app and navigate to the terminal screen to verify that colors and strings are displayed correctly in both English and Italian.
