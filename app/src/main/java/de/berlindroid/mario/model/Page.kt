package de.berlindroid.mario.model

import androidx.compose.runtime.Composable

/**
 * Represents a two-page spread in the farewell book.
 * 
 * Each [Page] implementation is automatically discovered via Metro DI if annotated with 
 * `@ContributesBinding(AppScope::class)`.
 * 
 * On the target TCL Nxtpaper 14 tablet (2400x1600px), each content area (Left and Right) 
 * will occupy exactly half of the screen width.
 */
interface Page {
    val title: String
    val author: String

    /**
     * Determines the position in the book. Lower numbers come first.
     * Default is 100. Use 0 for Cover, 10 for Intro.
     */
    val order: Int get() = 100
    
    /**
     * Content for the left-hand side of the spread.
     * Roughly 1200px width on the target device.
     */
    @Composable
    fun LeftContent()

    /**
     * Content for the right-hand side of the spread.
     * Roughly 1200px width on the target device.
     */
    @Composable
    fun RightContent()
}
