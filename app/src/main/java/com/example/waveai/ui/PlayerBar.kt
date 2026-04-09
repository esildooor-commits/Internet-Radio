package com.example.waveai.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.waveai.*
import com.example.waveai.ui.theme.*

@Composable
fun PlayerBar(
    station: Station,
    appMode: AppMode,
    isPlaying: Boolean,
    streamQuality: StreamQuality,
    onPrev: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onVisualizerToggle: () -> Unit,
    onModeChange: (AppMode) -> Unit,
    onSettingsOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = accentFor(appMode)

    Surface(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(40.dp),
        color     = Surface,
        tonalElevation = 4.dp
    ) {
        Column(Modifier.padding(12.dp)) {

            // ── Cover + Controls ───────────────────────────────────────────────
            Row(
                modifier            = Modifier.fillMaxWidth(),
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Cover & Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = onVisualizerToggle)
                    ) {
                        AsyncImage(
                            model             = station.coverUrl,
                            contentDescription = station.name,
                            contentScale      = ContentScale.Crop,
                            modifier          = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text     = station.name.uppercase(),
                            style    = StyleTitle.copy(fontSize = 13.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 90.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (streamQuality == StreamQuality.LOW) Red else Emerald
                                    )
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text  = if (streamQuality == StreamQuality.LOW) "SPAR-MODUS" else "HD STREAM",
                                style = StyleLabel.copy(color = TextHint)
                            )
                        }
                    }
                }

                // Controls
                Row(
                    modifier          = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White.copy(0.05f))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onPrev, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (appMode == AppMode.PODCAST) Icons.Default.Replay10
                            else Icons.Default.SkipPrevious,
                            contentDescription = "Vorherige",
                            tint = TextSecond
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (appMode == AppMode.SPOTIFY) Spotify else Color.White)
                            .clickable(onClick = onPlayPause),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector       = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint              = if (appMode == AppMode.SPOTIFY) Color.Black else Color.Black,
                            modifier          = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = onNext, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (appMode == AppMode.PODCAST) Icons.Default.Forward10
                            else Icons.Default.SkipNext,
                            contentDescription = "Nächste",
                            tint = TextSecond
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = Border, thickness = 0.5.dp)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                NavItem(
                    icon     = Icons.Default.Radio,
                    label    = "Radio",
                    active   = appMode == AppMode.RADIO,
                    color    = Indigo,
                    onClick  = { onModeChange(AppMode.RADIO) }
                )
                NavItem(
                    icon     = Icons.Default.Mic,
                    label    = "Casts",
                    active   = appMode == AppMode.PODCAST,
                    color    = Purple,
                    onClick  = { onModeChange(AppMode.PODCAST) }
                )
                NavItem(
                    icon     = Icons.Default.Tune,
                    label    = "Setup",
                    active   = false,
                    color    = TextSecond,
                    onClick  = onSettingsOpen
                )
                NavItem(
                    icon     = Icons.Default.Album,
                    label    = "Spotify",
                    active   = appMode == AppMode.SPOTIFY,
                    color    = Spotify,
                    onClick  = { onModeChange(AppMode.SPOTIFY) }
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    active: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier          = Modifier.clickable(onClick = onClick).padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector       = icon,
            contentDescription = label,
            tint              = if (active) color else TextHint,
            modifier          = Modifier.size(22.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text  = label.uppercase(),
            style = StyleLabel.copy(color = if (active) color else TextHint)
        )
    }
}

fun accentFor(mode: AppMode) = when (mode) {
    AppMode.RADIO   -> Indigo
    AppMode.PODCAST -> Purple
    AppMode.SPOTIFY -> Spotify
}
