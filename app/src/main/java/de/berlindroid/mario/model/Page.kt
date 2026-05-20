package de.berlindroid.mario.model

import androidx.compose.runtime.Composable

/**
 * Categories for pages to determine their relative order in the book.
 */
sealed class PageCategory(val weight: Int) {
    /** The very first page. Only one should exist. */
    data object Cover : PageCategory(0)

    /** Important introductory pages. */
    data object Intro : PageCategory(10)

    /** General pages contributed by the community. */
    data object Community : PageCategory(100)

    /** Closing pages. */
    data object Outro : PageCategory(1000)
}

/**
 * Represents a two-page spread in the thank you book.
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
     * Determines the broad category of the page for ordering.
     */
    val category: PageCategory get() = PageCategory.Community
    
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
