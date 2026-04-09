package com.example.waveai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WaveUiState(
    val appMode: AppMode       = AppMode.RADIO,
    val viewMode: ViewMode     = ViewMode.CAROUSEL,
    val radioIdx: Int          = 0,
    val podcastIdx: Int        = 0,
    val spotifyIdx: Int        = 0,
    val isPlaying: Boolean     = false,
    val streamQuality: StreamQuality = StreamQuality.HIGH,
    val eqLevels: List<Float>  = EQ_PRESETS[0].levels,
    val activePreset: String   = "Normal",
    val sleepTimerMinutes: Int = 0,
    val sleepTimeLeftSec: Int? = null,
    val showAiPanel: Boolean   = false,
    val isLoadingAi: Boolean   = false,
    val aiAnalysis: String     = "",
    val showVisualizer: Boolean = false,
    val showSettings: Boolean  = false,
)

class WaveViewModel : ViewModel() {

    private val _ui = MutableStateFlow(WaveUiState())
    val ui = _ui.asStateFlow()

    private fun update(block: WaveUiState.() -> WaveUiState) {
        _ui.value = _ui.value.block()
    }

    val activeStation: Station get() {
        val s = _ui.value
        return when (s.appMode) {
            AppMode.RADIO   -> STATIONS[s.radioIdx]
            AppMode.PODCAST -> PODCASTS[s.podcastIdx]
            AppMode.SPOTIFY -> SPOTIFY_MIXES[s.spotifyIdx]
        }
    }

    private fun currentList() = when (_ui.value.appMode) {
        AppMode.RADIO   -> STATIONS
        AppMode.PODCAST -> PODCASTS
        AppMode.SPOTIFY -> SPOTIFY_MIXES
    }

    private fun activeIdx() = when (_ui.value.appMode) {
        AppMode.RADIO   -> _ui.value.radioIdx
        AppMode.PODCAST -> _ui.value.podcastIdx
        AppMode.SPOTIFY -> _ui.value.spotifyIdx
    }

    fun setAppMode(mode: AppMode) = update { copy(appMode = mode) }
    fun setViewMode(mode: ViewMode) = update { copy(viewMode = mode) }

    fun selectStation(index: Int) {
        update {
            when (appMode) {
                AppMode.RADIO   -> copy(radioIdx   = index, aiAnalysis = "")
                AppMode.PODCAST -> copy(podcastIdx = index, aiAnalysis = "")
                AppMode.SPOTIFY -> copy(spotifyIdx = index, aiAnalysis = "")
            }
        }
    }

    fun nextStation() {
        val size = currentList().size
        selectStation((activeIdx() + 1) % size)
    }

    fun prevStation() {
        val size = currentList().size
        selectStation((activeIdx() - 1 + size) % size)
    }

    fun togglePlay() = update { copy(isPlaying = !isPlaying) }
    fun setQuality(q: StreamQuality) = update { copy(streamQuality = q) }
    fun applyPreset(preset: EqPreset) = update { copy(eqLevels = preset.levels, activePreset = preset.name) }

    fun setEqBand(index: Int, value: Float) {
        val levels = _ui.value.eqLevels.toMutableList()
        levels[index] = value
        update { copy(eqLevels = levels, activePreset = "Custom") }
    }

    private var timerJob: Job? = null

    fun setSleepTimer(minutes: Int) {
        timerJob?.cancel()
        val seconds = if (minutes > 0) minutes * 60 else null
        update { copy(sleepTimerMinutes = minutes, sleepTimeLeftSec = seconds) }

        if (minutes > 0) {
            timerJob = viewModelScope.launch {
                var left = minutes * 60
                while (left > 0 && _ui.value.isPlaying) {
                    delay(1000)
                    left--
                    update { copy(sleepTimeLeftSec = left) }
                }
                if (left <= 0) {
                    update { copy(isPlaying = false, sleepTimeLeftSec = null, sleepTimerMinutes = 0) }
                }
            }
        }
    }

    fun fetchAiInsights() {
        val state   = _ui.value
        val station = activeStation
        update { copy(showAiPanel = true, isLoadingAi = true, aiAnalysis = "") }

        viewModelScope.launch {
            val prompt = GeminiService.buildPrompt(state.appMode, station)
            val result = GeminiService.callGemini(prompt)
            update { copy(aiAnalysis = result, isLoadingAi = false) }
        }
    }

    fun hideAiPanel() = update { copy(showAiPanel = false) }
    fun toggleVisualizer() = update { copy(showVisualizer = !showVisualizer) }
    fun setShowSettings(show: Boolean) = update { copy(showSettings = show) }

    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "$m:${if (s < 10) "0$s" else "$s"}"
    }
}
