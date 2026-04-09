package com.example.waveai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Background   = Color(0xFF121212)
val Surface      = Color(0xFF1C1C1E)
val SurfaceHigh  = Color(0xFF2A2A2C)
val Border       = Color(0x1AFFFFFF)

val Indigo       = Color(0xFF6366F1)
val IndigoDark   = Color(0xFF4338CA)
val Purple       = Color(0xFFA855F7)
val Spotify      = Color(0xFF1DB954)
val Emerald      = Color(0xFF10B981)
val Amber        = Color(0xFFF59E0B)
val Red          = Color(0xFFEF4444)
val Orange       = Color(0xFFF97316)

val TextPrimary  = Color(0xFFFFFFFF)
val TextSecond   = Color(0xFF9CA3AF)
val TextHint     = Color(0xFF6B7280)

private val DarkColors = darkColorScheme(
    primary        = Indigo,
    secondary      = Purple,
    background     = Background,
    surface        = Surface,
    onPrimary      = Color.White,
    onBackground   = TextPrimary,
    onSurface      = TextPrimary,
)

@Composable
fun WaveAiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}

val StyleTitle = TextStyle(
    fontSize   = 28.sp,
    fontWeight = FontWeight.Black,
    fontStyle  = FontStyle.Italic,
    letterSpacing = (-0.5).sp
)

val StyleLabel = TextStyle(
    fontSize   = 9.sp,
    fontWeight = FontWeight.Black,
    letterSpacing = 1.5.sp
)

val StyleBodySm = TextStyle(
    fontSize   = 11.sp,
    fontWeight = FontWeight.Medium,
    color      = TextSecond
)
