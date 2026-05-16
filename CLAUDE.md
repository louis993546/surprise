# AI Agent Instructions for Mario's Thank You Card

This project is a collaborative digital thank you card. When assisting with this project, please adhere to the following guidelines to ensure consistency and compatibility with the target hardware.

## Core Mandates

- **Target Device:** The app is exclusively for a **TCL Nxtpaper 14** tablet (14.3", 2400x1600). Always prioritize large-screen layouts. **Minimum SDK is 28**.
- **Contribution Workflow:** 
    1. Create a new `object` implementing the `Page` interface in the `de.berlindroid.mario.pages` package.
    2. Add the `@ContributesIntoSet(AppScope::class)` annotation to your object.
    3. Use the `order` property to position your page. Lower numbers (e.g., 0-10) are for the beginning, default is 100.
- **Language:** English and German are the primary languages.
- **Philosophy:** This is a "frozen in time" gift. **BE CREATIVE.** 
    - The UI is a "Book" where each contributor gets a **full spread** (2 pages side-by-side).
    - Use `LeftContent()` for the left page and `RightContent()` for the right page. Each is ~1200x1600px on the target device.
- **Dependency Injection:** Use **Metro DI** (`dev.zacsweers.metro`). 
    - The root graph is `AppGraph` and is provided via `LocalAppGraph` CompositionLocal.
    - Use `@ContributesIntoSet(AppScope::class)` for auto-registration of pages.

## Implementation Details

### The `Page` Interface
Located in `de.berlindroid.mario.model.Page`:
```kotlin
interface Page {
    val title: String
    val author: String
    val order: Int get() = 100
    
    @Composable fun LeftContent()
    @Composable fun RightContent()
}
```

### Adding a New Page
- **DO:** Use rich aesthetics, animations, and graphics.
- **DO:** Use `@ContributesIntoSet(AppScope::class)`.
- **DON'T:** Change the core navigation structure or the "Book" logic in `MainActivity`.

## Style Preferences
- **Styling:** Use the project's `MarioTheme`. 
- **Icons:** Use `Icons.Default` or `Icons.Filled` from the extended material icons library.
- **Composition:** Prefer local state and simple compositions.
