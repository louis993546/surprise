# Mario's Farewell Card

A digital farewell card for Mario, built with Jetpack Compose. Designed to be displayed on a giant tablet (TCL Nxtpaper 14) as a physical-style book with realistic page-flip physics.

## How to contribute a Page

We want you to be as creative as possible! The app is a "frozen in time" gift, so don't worry about architecture—focus on the message and the visuals.

1. **Create a new Page file**: In `app/src/main/java/de/berlindroid/mario/pages/`, create a new Kotlin file (e.g., `YourNamePage.kt`).
2. **Implement the `Page` interface and add the Metro annotation**:
    ```kotlin
    @ContributesIntoSet(AppScope::class)
    object YourNamePage : Page {
        override val title: String = "Your Title"
        override val author: String = "Your Name"
        override val order: Int = 100 // Lower numbers come first (Cover is 0)

        @Composable
        override fun LeftContent() {
            // Your Compose code for the left page (~1200x1600px)
        }

        @Composable
        override fun RightContent() {
            // Your Compose code for the right page (~1200x1600px)
        }
    }
    ```

**That's it!** Metro DI will automatically discover your page and add it to the book. No need to register it anywhere else.

## UI Design

The app looks like a physical book with two pages side-by-side. 
- **Target Resolution:** 2400 x 1600.
- **Side-by-Side:** Every contributor gets a full spread (Left and Right pages).
- **Creativity:** Put a WebView, a Video, crazy animations, or whatever you like!

## Development

- **Builds:** Pull Requests trigger automated builds to ensure compatibility.
- **Dependency Injection:** We use **Metro DI**.
