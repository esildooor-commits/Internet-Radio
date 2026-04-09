package com.example.waveai

enum class AppMode { RADIO, PODCAST, SPOTIFY }
enum class StreamQuality { HIGH, LOW }
enum class ViewMode { CAROUSEL, LIST }

data class Station(
    val id: String,
    val name: String,
    val genre: String,
    val nowPlaying: String,
    val dj: String,
    val listeners: String,
    val bitrate: String,
    val coverUrl: String,
    val gradientStart: Long,
    val gradientEnd: Long
)

data class EqPreset(
    val name: String,
    val levels: List<Float>
)

val STATIONS = listOf(
    Station("1", "Deep House Relax", "Electronic",
        "Sunset Melodies - Kygo Style", "DJ Alex Vibe", "1.2k", "320 kbps HQ",
        "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=60",
        0xFF4F46E5, 0xFF7E22CE),
    Station("2", "Jazz Classics", "Jazz",
        "Blue in Green - Miles Davis", "Sarah Jenkins", "850", "256 kbps",
        "https://images.unsplash.com/photo-1511192336575-5a79af67a629?w=800&auto=format&fit=crop&q=60",
        0xFFF59E0B, 0xFF991B1B),
    Station("3", "Indie Rock Wave", "Rock",
        "Lost in Yesterday - Tame Impala", "The Indie Hour", "2.4k", "320 kbps HQ",
        "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=800&auto=format&fit=crop&q=60",
        0xFF10B981, 0xFF0F766E),
    Station("4", "Classic FM", "Classical",
        "Symphony No. 5 - Beethoven", "Maestro Rundfunk", "3.1k", "Lossless",
        "https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=800&auto=format&fit=crop&q=60",
        0xFFEAB308, 0xFF9A3412),
    Station("5", "Urban Beats", "Hip-Hop",
        "God's Plan - Drake", "DJ Fresh", "5.7k", "320 kbps HQ",
        "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?w=800&auto=format&fit=crop&q=60",
        0xFFE11D48, 0xFF831843)
)

val PODCASTS = listOf(
    Station("p1", "Darknet Diaries", "Tech / True Crime",
        "Ep 133: The Hacker", "Jack Rhysider", "45k", "128 kbps",
        "https://images.unsplash.com/photo-1589903308904-1010c2294adc?w=800&auto=format&fit=crop&q=60",
        0xFF6D28D9, 0xFF0F172A),
    Station("p2", "The Daily", "News",
        "The Future of AI", "Michael Barbaro", "120k", "128 kbps",
        "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800&auto=format&fit=crop&q=60",
        0xFF1D4ED8, 0xFF0F172A),
    Station("p3", "Design Matters", "Design",
        "Creative Processes", "Debbie Millman", "12k", "192 kbps",
        "https://images.unsplash.com/photo-1561070791-2526d30994b5?w=800&auto=format&fit=crop&q=60",
        0xFFC2410C, 0xFF7F1D1D)
)

val SPOTIFY_MIXES = listOf(
    Station("s1", "Daily Mix 1", "Mixed",
        "Basierend auf deinem Geschmack", "Für Dich erstellt", "Privat", "320 kbps Ogg",
        "https://images.unsplash.com/photo-1614680376573-df3480f0c6ff?w=800&auto=format&fit=crop&q=60",
        0xFF1DB954, 0xFF0a401c),
    Station("s2", "Dein Mix der Woche", "New Music",
        "Frische Entdeckungen", "Spotify", "Privat", "320 kbps Ogg",
        "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=60",
        0xFF4F46E5, 0xFF0F172A),
    Station("s3", "Release Radar", "Neuerscheinungen",
        "Die neuesten Tracks", "Spotify", "Privat", "320 kbps Ogg",
        "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=60",
        0xFF374151, 0xFF000000)
)

val EQ_PRESETS = listOf(
    EqPreset("Normal",    listOf(50f, 50f, 50f, 50f, 50f)),
    EqPreset("Bass Boost",listOf(90f, 75f, 50f, 40f, 35f)),
    EqPreset("Rock",      listOf(70f, 60f, 40f, 65f, 75f)),
    EqPreset("Jazz",      listOf(60f, 50f, 45f, 55f, 65f)),
    EqPreset("Pop",       listOf(45f, 60f, 75f, 60f, 50f)),
    EqPreset("Vocal",     listOf(30f, 40f, 85f, 80f, 50f)),
    EqPreset("Electronic",listOf(80f, 55f, 45f, 70f, 85f)),
    EqPreset("Chill",     listOf(40f, 45f, 50f, 45f, 40f))
)

val EQ_LABELS = listOf("60Hz", "230Hz", "910Hz", "3kHz", "14kHz")
val SLEEP_TIMER_OPTIONS = listOf(0, 15, 30, 60)
