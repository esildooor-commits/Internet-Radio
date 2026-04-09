package com.example.waveai

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waveai.ui.*
import com.example.waveai.ui.theme.*

@Composable
fun WaveAiApp(vm: WaveViewModel = viewModel()) {
    val state by vm.ui.collectAsStateWithLifecycle()

    val currentStations = when (state.appMode) {
        AppMode.RADIO   -> STATIONS
        AppMode.PODCAST -> PODCASTS
        AppMode.SPOTIFY -> SPOTIFY_MIXES
    }
    val activeIdx = when (state.appMode) {
        AppMode.RADIO   -> state.radioIdx
        AppMode.PODCAST -> state.podcastIdx
        AppMode.SPOTIFY -> state.spotifyIdx
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // ── Haupt-Layout ───────────────────────────────────────────────────────
        Column(Modifier.fillMaxSize()) {

            // Status-Bar-Spacer
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

            // ── App-Header ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Hamburger → Settings
                IconButton(
                    onClick  = { vm.setShowSettings(true) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(0.05f))
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Einstellungen", tint = TextSecond)
                }

                // App-Titel
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text  = "WAVE AI",
                        style = StyleTitle.copy(
                            fontSize = 18.sp,
                            color    = if (state.appMode == AppMode.SPOTIFY) Spotify else TextPrimary
                        )
                    )
                    if (state.sleepTimeLeftSec != null) {
                        Text(
                            text  = vm.formatTime(state.sleepTimeLeftSec!!),
                            style = StyleLabel.copy(color = Orange)
                        )
                    }
                }

                // Visualizer-Toggle
                IconButton(
                    onClick  = { vm.toggleVisualizer() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (state.showVisualizer)
                                if (state.appMode == AppMode.SPOTIFY) Spotify else Indigo
                            else Color.White.copy(0.05f)
                        )
                ) {
                    Icon(
                        Icons.Default.Bolt,
                        contentDescription = "Visualizer",
                        tint = if (state.showVisualizer)
                            if (state.appMode == AppMode.SPOTIFY) Color.Black else Color.White
                        else TextSecond
                    )
                }
            }

            // ── Pill-Navigation (Radio | Podcast | Spotify) ────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFF1C1C1E))
                    .padding(4.dp)
            ) {
                PillTab("RADIO",   state.appMode == AppMode.RADIO,   Indigo)  { vm.setAppMode(AppMode.RADIO)   }
                PillTab("PODCAST", state.appMode == AppMode.PODCAST, Purple)  { vm.setAppMode(AppMode.PODCAST) }
                PillTab("SPOTIFY", state.appMode == AppMode.SPOTIFY, Spotify) { vm.setAppMode(AppMode.SPOTIFY) }
            }

            Spacer(Modifier.height(8.dp))

            // ── HomeScreen (Carousel / Liste + AI-Panel) ───────────────────────
            HomeScreen(
                stations        = currentStations,
                activeIdx       = activeIdx,
                appMode         = state.appMode,
                viewMode        = state.viewMode,
                isPlaying       = state.isPlaying,
                streamQuality   = state.streamQuality,
                showAiPanel     = state.showAiPanel,
                isLoadingAi     = state.isLoadingAi,
                aiAnalysis      = state.aiAnalysis,
                showVisualizer  = state.showVisualizer,
                onSelectStation = { vm.selectStation(it) },
                onAiClick       = { vm.fetchAiInsights() },
                onHideAiPanel   = { vm.hideAiPanel() },
                onViewModeToggle = {
                    vm.setViewMode(
                        if (state.viewMode == ViewMode.CAROUSEL) ViewMode.LIST else ViewMode.CAROUSEL
                    )
                },
                modifier = Modifier.weight(1f)
            )

            // ── Player-Bar (Footer) ────────────────────────────────────────────
            PlayerBar(
                station           = vm.activeStation,
                appMode           = state.appMode,
                isPlaying         = state.isPlaying,
                streamQuality     = state.streamQuality,
                onPrev            = { vm.prevStation() },
                onPlayPause       = { vm.togglePlay() },
                onNext            = { vm.nextStation() },
                onVisualizerToggle = { vm.toggleVisualizer() },
                onModeChange      = { vm.setAppMode(it) },
                onSettingsOpen    = { vm.setShowSettings(true) },
                modifier          = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }

        // ── Settings-Overlay ──────────────────────────────────────────────────
        AnimatedVisibility(
            visible  = state.showSettings,
            enter    = slideInHorizontally { it } + fadeIn(),
            exit     = slideOutHorizontally { it } + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsScreen(
                streamQuality    = state.streamQuality,
                eqLevels         = state.eqLevels,
                activePreset     = state.activePreset,
                sleepTimerMinutes = state.sleepTimerMinutes,
                onQualityChange  = { vm.setQuality(it) },
                onPresetSelect   = { vm.applyPreset(it) },
                onEqBandChange   = { i, v -> vm.setEqBand(i, v) },
                onTimerSelect    = { vm.setSleepTimer(it) },
                onClose          = { vm.setShowSettings(false) }
            )
        }
    }
}

// ── Pill-Tab-Hilfselement ─────────────────────────────────────────────────────
@Composable
private fun RowScope.PillTab(
    label: String,
    active: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(50.dp))
            .background(if (active) activeColor else Color.Transparent)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = label,
            style = StyleLabel.copy(
                color = if (active) {
                    if (activeColor == Spotify) Color.Black else Color.White
                } else TextHint
            )
        )
    }
}

@Composable
fun PillTabFixed(
    label: String,
    active: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (active) activeColor else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = label,
            style = StyleLabel.copy(
                color = if (active) {
                    if (activeColor == Spotify) Color.Black else Color.White
                } else TextHint
            )
        )
    }
}
