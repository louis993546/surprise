package de.berlindroid.mario.model

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

object SettingsRepository {
    private const val PREFS_NAME = "mario_settings"
    private const val KEY_AUTO_FLIP = "auto_flip_interval"
    private const val KEY_KEEP_SCREEN_ON = "keep_screen_on"
    private const val KEY_SLIDESHOW_DELAY = "slideshow_delay_ms"

    private lateinit var prefs: SharedPreferences

    private val _autoFlipFlow = MutableStateFlow(0)
    val autoFlipFlow: StateFlow<Int> = _autoFlipFlow.asStateFlow()

    private val _keepScreenOnFlow = MutableStateFlow(false)
    val keepScreenOnFlow: StateFlow<Boolean> = _keepScreenOnFlow.asStateFlow()

    private val _slideshowDelayFlow = MutableStateFlow(30000L)
    val slideshowDelayFlow: StateFlow<Long> = _slideshowDelayFlow.asStateFlow()

    fun init(context: Context) {
        if (::prefs.isInitialized) return
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _autoFlipFlow.value = prefs.getInt(KEY_AUTO_FLIP, 0)
        _keepScreenOnFlow.value = prefs.getBoolean(KEY_KEEP_SCREEN_ON, false)
        _slideshowDelayFlow.value = prefs.getLong(KEY_SLIDESHOW_DELAY, 30000L)
    }

    fun setAutoFlip(intervalMinutes: Int) {
        check(::prefs.isInitialized) { "SettingsRepository must be initialized by calling init(context)" }
        prefs.edit {
            putInt(KEY_AUTO_FLIP, intervalMinutes)
        }
        _autoFlipFlow.value = intervalMinutes
    }

    fun setKeepScreenOn(keepOn: Boolean) {
        check(::prefs.isInitialized) { "SettingsRepository must be initialized by calling init(context)" }
        prefs.edit {
            putBoolean(KEY_KEEP_SCREEN_ON, keepOn)
        }
        _keepScreenOnFlow.value = keepOn
    }

    fun setSlideshowDelay(delayMs: Long) {
        check(::prefs.isInitialized) { "SettingsRepository must be initialized by calling init(context)" }
        prefs.edit {
            putLong(KEY_SLIDESHOW_DELAY, delayMs)
        }
        _slideshowDelayFlow.value = delayMs
    }
}

@Composable
fun rememberSettingsState(): Pair<State<Int>, State<Boolean>> {
    val autoFlip = SettingsRepository.autoFlipFlow.collectAsState()
    val keepScreenOn = SettingsRepository.keepScreenOnFlow.collectAsState()
    return autoFlip to keepScreenOn
}

@Composable
fun rememberSlideshowDelayState(): State<Long> {
    return SettingsRepository.slideshowDelayFlow.collectAsState()
}
