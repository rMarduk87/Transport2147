# Fix Build Errors and Logic Issues

The project currently has several build errors and logical bugs across multiple files, primarily related to null safety, uninitialized variables, and incorrect collection handling.

## Proposed Changes

### [ChapterFormatter.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/chapter/ChapterFormatter.kt)

#### [MODIFY] [ChapterFormatter.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/chapter/ChapterFormatter.kt)
- Update `createChapter` and `formatHistory` to accept a nullable `LinearLayout?` to match the `formatChapter` signature.
- Use safe calls (`linearLayout?.addView`) when adding views to the layout.
- Fix `safeNavController` calls by using `GameLogic.navigator?.safeNavController(...)` to avoid receiver type mismatches.
- Fix `formatSpan` by using `ArrayList<Any>` instead of `ArrayList<*>` to allow adding spans.
- Refactor `loopInnerElements` to eliminate double-appending of nodes and improve readability.
- Update `applySmallCapsFormatting` to accept a `Context` and avoid crashing on null `GameLogic.navigator`.

### [PlayerSheet.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/game/PlayerSheet.kt)

#### [MODIFY] [PlayerSheet.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/game/PlayerSheet.kt)
- Initialize the `` `object` `` variable in the XML constructor by fetching the object definition from `BookManager` when the language changes.

### [ConditionalArrayList.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/collection/ConditionalArrayList.kt)

#### [MODIFY] [ConditionalArrayList.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/collection/ConditionalArrayList.kt)
- Fix the `remove` override signature to match `ArrayList<E?>`'s requirement (`remove(element: E?): Boolean`).

### [RPTextView2.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/text/RPTextView2.kt)

#### [MODIFY] [RPTextView2.kt](file:///C:/Transport2147/app/src/main/java/rpt/games/transport2147/utils/view/text/RPTextView2.kt)
- Initialize variables `c` and `c2` in `justifyText` to satisfy the Kotlin compiler's initialization requirements.

## Verification Plan

### Automated Tests
- Execute `./gradlew :app:compileDebugKotlin` to ensure all reported build errors are resolved.

### Manual Verification
- Verify that fragments now open correctly with their respective bindings.
- Check that the history and chapter formatting work as expected without crashes or double-text issues.
