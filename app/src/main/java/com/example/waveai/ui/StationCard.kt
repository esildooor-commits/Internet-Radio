package com.example.waveai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.waveai.*
import com.example.waveai.ui.theme.*

@Composable
fun StationCarouselCard(
    station: Station,
    appMode: AppMode,
    isActive: Boolean,
    onAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = accentFor(appMode)
    val gradBrush = Brush.verticalGradient(
        colors = listOf(Color(station.gradientStart), Color(station.gradientEnd))
    )

    Box(
        modifier = modifier
            .width(260.dp)
            .height(360.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(gradBrush)
            .padding(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(47.dp))
                .background(Color(0xF0181818))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                AsyncImage(
                    model              = station.coverUrl,
                    contentDescription = station.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(0.2f), Color.Transparent, Color(0xF0181818))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.Black.copy(0.4f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text  = station.genre.uppercase(),
                        style = StyleLabel.copy(color = Color.White)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text     = station.name.uppercase(),
                        style    = StyleTitle.copy(fontSize = 20.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text  = when (appMode) {
                            AppMode.RADIO   -> "Host: ${station.dj}"
                            AppMode.PODCAST -> "Von: ${station.dj}"
                            AppMode.SPOTIFY -> "Kuratiert von: ${station.dj}"
                        }.uppercase(),
                        style = StyleLabel.copy(color = TextHint),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape    = RoundedCornerShape(16.dp),
                        color    = Color.White.copy(0.05f),
                        tonalElevation = 0.dp
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                text = when (appMode) {
                                    AppMode.RADIO   -> "JETZT ON AIR"
                                    AppMode.PODCAST -> "NEUESTE FOLGE"
                                    AppMode.SPOTIFY -> "AKTUELLER TRACK"
                                },
                                style = StyleLabel.copy(color = accent)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text     = station.nowPlaying,
                                style    = StyleBodySm.copy(color = TextPrimary, fontSize = 11.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Button(
                    onClick  = onAiClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = accent)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (appMode == AppMode.SPOTIFY) Color.Black else Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text  = when (appMode) {
                            AppMode.RADIO   -> "AI DJ TALK"
                            AppMode.PODCAST -> "KI ZUSAMMENFASSUNG"
                            AppMode.SPOTIFY -> "MIX INSIGHTS"
                        },
                        style = StyleLabel.copy(
                            color = if (appMode == AppMode.SPOTIFY) Color.Black else Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StationListItem(
    station: Station,
    appMode: AppMode,
    isActive: Boolean,
    isPlaying: Boolean,
    streamQuality: StreamQuality,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = accentFor(appMode)

    Surface(
        modifier  = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .clickable(onClick = onClick),
        shape     = RoundedCornerShape(32.dp),
        color     = Surface,
        tonalElevation = if (isActive) 4.dp else 0.dp
    ) {
        Row(Modifier.height(110.dp)) {
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model              = station.coverUrl,
                    contentDescription = station.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
                if (isActive && isPlaying) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(accent.copy(0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = "Playing",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text  = station.genre.uppercase(),
                        style = StyleLabel.copy(color = accent)
                    )
                    Text(
                        text  = if (streamQuality == StreamQuality.LOW) "48 kbps Mono" else station.bitrate,
                        style = StyleLabel.copy(color = TextHint)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = station.name.uppercase(),
                    style    = StyleTitle.copy(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = station.dj,
                    style    = StyleBodySm,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
