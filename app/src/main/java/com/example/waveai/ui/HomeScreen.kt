package com.example.waveai.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waveai.*
import com.example.waveai.ui.theme.*
import kotlin.math.abs

@Composable
fun HomeScreen(
    stations: List<Station>,
    activeIdx: Int,
    appMode: AppMode,
    viewMode: ViewMode,
    isPlaying: Boolean,
    streamQuality: StreamQuality,
    showAiPanel: Boolean,
    isLoadingAi: Boolean,
    aiAnalysis: String,
    showVisualizer: Boolean,
    onSelectStation: (Int) -> Unit,
    onAiClick: () -> Unit,
    onHideAiPanel: () -> Unit,
    onViewModeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeStation = stations[activeIdx]
    val accent        = accentFor(appMode)

    Box(modifier = modifier.fillMaxSize()) {

        // ── Visualizer-Overlay ─────────────────────────────────────────────────
        AnimatedVisibility(
            visible = showVisualizer && isPlaying,
            enter   = fadeIn(),
            exit    = fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.6f)),
                contentAlignment = Alignment.Center
            ) {
                VisualizerBars(color = Color(activeStation.gradientStart))
            }
        }

        Column(Modifier.fillMaxSize()) {

            // ── Header-Zeile: Titel + View-Toggle ──────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = when (appMode) {
                            AppMode.RADIO   -> "LIVE"
                            AppMode.PODCAST -> "SHOWS"
                            AppMode.SPOTIFY -> "MIXES"
                        },
                        style = StyleTitle.copy(fontSize = 28.sp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            tint = TextHint,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${activeStation.listeners} ZUHÖRER",
                            style = StyleLabel.copy(color = TextHint)
                        )
                    }
                }

                // View-Toggle (Carousel ↔ Liste)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(0.05f))
                        .padding(4.dp)
                ) {
                    val btnMod = @Composable { active: Boolean, icon: @Composable () -> Unit ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (active) Color(0xFF2A2A2C) else Color.Transparent)
                                .clickable(onClick = onViewModeToggle),
                            contentAlignment = Alignment.Center
                        ) { icon() }
                    }
                    btnMod(viewMode == ViewMode.CAROUSEL) {
                        Icon(Icons.Default.AutoAwesome, null, tint = if (viewMode == ViewMode.CAROUSEL) Color.White else TextHint, modifier = Modifier.size(16.dp))
                    }
                    btnMod(viewMode == ViewMode.LIST) {
                        Icon(Icons.Default.Menu, null, tint = if (viewMode == ViewMode.LIST) Color.White else TextHint, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // ── Carousel oder Liste ────────────────────────────────────────────
            if (viewMode == ViewMode.CAROUSEL) {
                CarouselView(
                    stations      = stations,
                    activeIdx     = activeIdx,
                    appMode       = appMode,
                    onSelect      = onSelectStation,
                    onAiClick     = onAiClick,
                    modifier      = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    modifier            = Modifier.weight(1f),
                    contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(stations) { idx, station ->
                        StationListItem(
                            station       = station,
                            appMode       = appMode,
                            isActive      = idx == activeIdx,
                            isPlaying     = isPlaying,
                            streamQuality = streamQuality,
                            onClick       = { onSelectStation(idx) }
                        )
                    }
                }
            }
        }

        // ── AI-Panel (oben) ────────────────────────────────────────────────────
        AnimatedVisibility(
            visible  = showAiPanel,
            enter    = slideInVertically { -it } + fadeIn(),
            exit     = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .zIndex(10f)
        ) {
            AiPanel(
                appMode     = appMode,
                isLoading   = isLoadingAi,
                text        = aiAnalysis,
                onDismiss   = onHideAiPanel
            )
        }
    }
}

@Composable
private fun CarouselView(
    stations: List<Station>,
    activeIdx: Int,
    appMode: AppMode,
    onSelect: (Int) -> Unit,
    onAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        stations.forEachIndexed { index, station ->
            val delta = index - activeIdx
            if (abs(delta) > 1) return@forEachIndexed

            val translateX by animateFloatAsState(
                targetValue   = delta * 180f,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = 200f),
                label         = "carousel_x"
            )
            val scale by animateFloatAsState(
                targetValue   = if (delta == 0) 1f else 0.85f,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = 200f),
                label         = "carousel_scale"
            )
            val alpha by animateFloatAsState(
                targetValue   = if (delta == 0) 1f else 0.4f,
                label         = "carousel_alpha"
            )

            StationCarouselCard(
                station  = station,
                appMode  = appMode,
                isActive = delta == 0,
                onAiClick = onAiClick,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = translateX.dp.toPx()
                        scaleX       = scale
                        scaleY       = scale
                        this.alpha   = alpha
                        transformOrigin = TransformOrigin.Center
                    }
                    .zIndex(if (delta == 0) 2f else 1f)
                    .clickable(enabled = delta != 0) { onSelect(index) }
            )
        }
    }
}

@Composable
fun AiPanel(
    appMode: AppMode,
    isLoading: Boolean,
    text: String,
    onDismiss: () -> Unit
) {
    val accent = accentFor(appMode)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xF01C1C1E),
        tonalElevation = 8.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null,
                            tint = if (appMode == AppMode.SPOTIFY) Color.Black else Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = when (appMode) {
                            AppMode.RADIO   -> "WAVE-AI DJ"
                            AppMode.PODCAST -> "AI SUMMARY"
                            AppMode.SPOTIFY -> "SPOTIFY INSIGHTS"
                        },
                        style = StyleLabel.copy(color = accent)
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Schließen", tint = TextHint, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(Modifier.height(12.dp))

            if (isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        color    = accent,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Generiere Insights...", style = StyleBodySm)
                }
            } else {
                Text(
                    text  = "\"$text\"",
                    style = StyleBodySm.copy(color = TextPrimary),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun VisualizerBars(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "vis")

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment     = Alignment.Bottom,
        modifier              = Modifier.height(200.dp)
    ) {
        repeat(12) { i ->
            val height by infiniteTransition.animateFloat(
                initialValue  = 0.1f,
                targetValue   = 1f,
                animationSpec = infiniteRepeatable(
                    animation    = keyframes {
                        durationMillis = 600
                        0.1f at 0
                        1f   at 300
                        0.1f at 600
                    },
                    repeatMode  = RepeatMode.Restart,
                    initialStartOffset = StartOffset(i * 50)
                ),
                label = "bar_$i"
            )
            Box(
                modifier = Modifier
                    .width(14.dp)
                    .fillMaxHeight(height)
                    .clip(RoundedCornerShape(topStart = 7.dp, topEnd = 7.dp))
                    .background(color)
            )
        }
    }
}

private fun Modifier.zIndex(z: Float) = this.graphicsLayer { renderEffect = null; this.alpha = alpha }
