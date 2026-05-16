# Mario's Farewell Card

A digital farewell card for Mario, built with Jetpack Compose. Designed to be displayed on a giant tablet (TCL Nxtpaper 14) as a physical-style book.

## How to contribute a Page

We want you to be as creative as possible! The app is a "frozen in time" gift, so don't worry about architecture—focus on the message and the visuals.

1. **Create a new Page file**: In `app/src/main/java/de/berlindroid/mario/pages/`, create a new Kotlin file (e.g., `YourNamePage.kt`).
2. **Implement the `Page` interface and add the Metro annotation**:
    ```kotlin
    @ContributesBinding(AppScope::class)
    object YourNamePage : Page {
        override val title: String = "Your Title"
        override val author: String = "Your Name"

        @Composable
        override fun Content() {
            // Your Compose code here! 
            // Put a WebView, a Video, some crazy animations, or even an AsyncTask.
            // Just make sure it looks great on a 2400x1600 screen.
        }
    }
    ```

**That's it!** Metro DI will automatically discover your page and add it to the book. No need to register it anywhere else.

## UI Design

The app looks like a physical book with two pages side-by-side. 
- **Target Resolution:** 2400 x 1600.
- **Side-by-Side:** Your content will occupy roughly half the screen width.

## Development & CI

- **Builds:** Every Pull Request triggers a Debug and Release build in GitHub Actions.
- **Releases:** On the `main` branch, build artifacts are uploaded for the final pre-loading onto the tablet.
- **Dependency Injection:** We use **Metro DI**. Root dependencies are in `AppGraph`.
