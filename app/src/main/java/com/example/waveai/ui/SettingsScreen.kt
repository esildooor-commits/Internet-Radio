package com.example.waveai.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waveai.*
import com.example.waveai.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    streamQuality: StreamQuality,
    eqLevels: List<Float>,
    activePreset: String,
    sleepTimerMinutes: Int,
    onQualityChange: (StreamQuality) -> Unit,
    onPresetSelect: (EqPreset) -> Unit,
    onEqBandChange: (Int, Float) -> Unit,
    onTimerSelect: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 100.dp)
        ) {
            Spacer(Modifier.height(56.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                TextButton(onClick = onClose) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = TextSecond)
                    Spacer(Modifier.width(4.dp))
                    Text("BACK", style = StyleLabel.copy(color = TextSecond))
                }
                Text("STUDIO SETTINGS", style = StyleTitle.copy(fontSize = 18.sp))
            }

            Spacer(Modifier.height(32.dp))

            SectionHeader(icon = Icons.Default.SignalCellularAlt, label = "STREAM QUALITÄT")
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(0.05f))
                    .padding(4.dp)
            ) {
                QualityButton(
                    label    = "High Fidelity (HD)",
                    active   = streamQuality == StreamQuality.HIGH,
                    color    = Emerald,
                    icon     = Icons.Default.SignalCellularAlt,
                    modifier = Modifier.weight(1f),
                    onClick  = { onQualityChange(StreamQuality.HIGH) }
                )
                QualityButton(
                    label    = "Low Signal (Spar)",
                    active   = streamQuality == StreamQuality.LOW,
                    color    = Red,
                    icon     = Icons.Default.SignalCellularAlt,
                    modifier = Modifier.weight(1f),
                    onClick  = { onQualityChange(StreamQuality.LOW) }
                )
            }
            Text(
                text = if (streamQuality == StreamQuality.LOW)
                    "Spar-Modus aktiv. Analoges Rauschen simuliert."
                else
                    "HD-Streaming aktiv. Voller Frequenzbereich.",
                style    = StyleBodySm.copy(fontSize = 8.sp),
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            Spacer(Modifier.height(32.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                SectionHeader(icon = Icons.Default.Tune, label = "MASTER EQUALIZER")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Indigo.copy(0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(activePreset.uppercase(), style = StyleLabel.copy(color = Indigo))
                }
            }
            Spacer(Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding        = PaddingValues(horizontal = 0.dp)
            ) {
                items(EQ_PRESETS) { preset ->
                    EqPresetChip(
                        name   = preset.name,
                        active = activePreset == preset.name,
                        onClick = { onPresetSelect(preset) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF161618)
            ) {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment     = Alignment.Bottom
                ) {
                    eqLevels.forEachIndexed { i, level ->
                        EqBand(
                            label  = EQ_LABELS[i],
                            value  = level,
                            onValueChange = { onEqBandChange(i, it) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            SectionHeader(icon = Icons.Default.Bedtime, label = "KI SLEEP TIMER")
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SLEEP_TIMER_OPTIONS.forEach { mins ->
                    SleepTimerChip(
                        label   = if (mins == 0) "OFF" else "${mins}m",
                        active  = sleepTimerMinutes == mins,
                        color   = Orange,
                        onClick = { onTimerSelect(mins) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Button(
            onClick   = onClose,
            modifier  = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .height(56.dp),
            shape     = RoundedCornerShape(24.dp),
            colors    = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("SAVE & APPLY", style = StyleLabel.copy(color = Color.Black))
        }
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = TextSecond, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, style = StyleLabel.copy(color = TextHint))
    }
}

@Composable
private fun QualityButton(
    label: String,
    active: Boolean,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (active) color else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint     = if (active) Color.White else TextHint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            label.uppercase(),
            style = StyleLabel.copy(color = if (active) Color.White else TextHint),
            fontSize = 8.sp
        )
    }
}

@Composable
private fun EqPresetChip(name: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) Color.White else Color.White.copy(0.05f))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            name.uppercase(),
            style = StyleLabel.copy(color = if (active) Color.Black else TextHint)
        )
    }
}

@Composable
private fun EqBand(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier  = Modifier.height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value         = value / 100f,
                onValueChange = { onValueChange(it * 100f) },
                modifier      = Modifier
                    .width(120.dp)
                    .graphicsLayer { rotationZ = -90f },
                colors        = SliderDefaults.colors(
                    thumbColor            = Indigo,
                    activeTrackColor      = Indigo,
                    inactiveTrackColor    = Color.White.copy(0.1f)
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = StyleLabel.copy(color = TextHint, fontSize = 7.sp))
    }
}

@Composable
private fun SleepTimerChip(
    label: String,
    active: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) color else Color.White.copy(0.05f))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, style = StyleLabel.copy(color = if (active) Color.White else TextHint))
    }
}
