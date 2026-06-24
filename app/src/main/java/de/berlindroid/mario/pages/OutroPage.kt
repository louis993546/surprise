package de.berlindroid.mario.pages

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.berlindroid.mario.LocalAppGraph
import de.berlindroid.mario.R
import de.berlindroid.mario.di.AppScope
import de.berlindroid.mario.gallery.GalleryActivity
import de.berlindroid.mario.model.Page
import de.berlindroid.mario.model.PageCategory
import de.berlindroid.mario.model.SettingsRepository
import de.berlindroid.mario.model.rememberSettingsState
import de.berlindroid.mario.model.rememberSlideshowDelayState
import dev.zacsweers.metro.ContributesIntoSet

/**
 * TODO
 *   * Link to repo
 *   * Auto-flip?
 *   * Keep screen on?
 *   * Full on photo album mode?
 */
@ContributesIntoSet(AppScope::class)
object OutroPage : Page {

    override val title: String = "Contributors"
    override val author: String = "The Maintainers"
    override val category: PageCategory = PageCategory.Outro

    private val fontFamily = FontFamily(
        Font(R.font.courier_prime)
    )

    @Composable
    override fun LeftContent() {
        val context = LocalContext.current
        val (autoFlipState, keepScreenOnState) = rememberSettingsState()
        val autoFlipIntervalMinutes by autoFlipState
        val keepScreenOn by keepScreenOnState

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(360.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.displayMedium,
                    fontFamily = fontFamily,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Keep Screen On Setting
                Text(
                    text = "Keep Screen On",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = fontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Prevent device from sleeping while reading",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = keepScreenOn,
                        onCheckedChange = { isChecked ->
                            SettingsRepository.setKeepScreenOn(isChecked)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Auto Flip Setting
                Text(
                    text = "Auto Flip Pages",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = fontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Automatically turn page after interval:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = fontFamily,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val options = listOf(
                        0 to "Off",
                        1 to "1 Min",
                        5 to "5 Min",
                        15 to "15 Min"
                    )
                    options.forEach { (minutes, label) ->
                        val isSelected = autoFlipIntervalMinutes == minutes
                        OutlinedButton(
                            onClick = {
                                SettingsRepository.setAutoFlip(minutes)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = fontFamily,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Gallery Mode Setting
                Text(
                    text = "Gallery Slideshow",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = fontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))

                val delayState = rememberSlideshowDelayState()
                val currentDelayMs = delayState.value

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val options = listOf(
                        60000L to "1m",
                        300000L to "5m",
                        1500000L to "15m",
                        3000000L to "30m",
                        6000000L to "1h",
                        24000000L to "4h",
                        72000000L to "12h",
                        144000000L to "24h",
                    )
                    options.forEach { (delayMs, label) ->
                        val isSelected = currentDelayMs == delayMs
                        OutlinedButton(
                            onClick = {
                                SettingsRepository.setSlideshowDelay(delayMs)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = fontFamily,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, GalleryActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Start Photo Slideshow",
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }

    @Composable
    override fun RightContent() {
        val appGraph = LocalAppGraph.current
        val contributors = remember(appGraph) {
            appGraph.pages
                .map { it.author }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contributors",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily,
                )
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(contributors) { contributor ->
                        Text(
                            text = contributor,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            fontFamily = fontFamily,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
