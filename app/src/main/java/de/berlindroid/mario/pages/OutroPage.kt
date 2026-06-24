package de.berlindroid.mario.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 48.dp, end = 48.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.outro_settings_title),
                style = MaterialTheme.typography.displayMedium,
                fontFamily = fontFamily,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Keep Screen On Setting
                Text(
                    text = stringResource(R.string.outro_keep_screen_on_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = fontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.outro_keep_screen_on_desc),
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
                    text = stringResource(R.string.outro_auto_flip_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = fontFamily,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.outro_auto_flip_desc),
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
                        0 to stringResource(R.string.outro_label_off),
                        1 to stringResource(R.string.outro_label_1_min),
                        5 to stringResource(R.string.outro_label_5_min),
                        15 to stringResource(R.string.outro_label_15_min)
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
                    text = stringResource(R.string.outro_gallery_title),
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
                        60000L to stringResource(R.string.outro_label_1m),
                        300000L to stringResource(R.string.outro_label_5m),
                        1500000L to stringResource(R.string.outro_label_15m),
                        3000000L to stringResource(R.string.outro_label_30m),
                        6000000L to stringResource(R.string.outro_label_1h),
                        24000000L to stringResource(R.string.outro_label_4h),
                        72000000L to stringResource(R.string.outro_label_12h),
                        144000000L to stringResource(R.string.outro_label_24h),
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
                        text = stringResource(R.string.outro_start_slideshow),
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 48.dp, end = 48.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.outro_about_title),
                style = MaterialTheme.typography.displayMedium,
                fontFamily = fontFamily,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = stringResource(R.string.outro_about_desc),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current
            Button(
                onClick = {
                    val githubUrl = "https://github.com/louis993546/surprise"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.outro_view_github),
                    fontFamily = fontFamily,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.outro_contributors_title),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                items(contributors) { contributor ->
                    Text(
                        text = "- $contributor",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = fontFamily,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
