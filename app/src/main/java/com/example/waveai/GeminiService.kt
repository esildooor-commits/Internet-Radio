package com.example.waveai

import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object GeminiService {

    // ⚠️ API-Key hier eintragen oder aus BuildConfig/Secret laden
    private const val API_KEY = ""
    private const val MODEL   = "gemini-2.0-flash"
    private val BASE_URL get() =
        "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$API_KEY"

    private const val SYSTEM_PROMPT =
        "Du bist ein cooler Radio-DJ namens 'Wave-AI'. " +
        "Antworte kurz, prägnant und im lockeren Radio-Slang auf Deutsch."

    suspend fun callGemini(prompt: String): String {
        repeat(5) { attempt ->
            try {
                val body = JSONObject().apply {
                    put("system_instruction", JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().put("text", SYSTEM_PROMPT)))
                    })
                    put("contents", JSONArray().put(
                        JSONObject().put("parts", JSONArray().put(
                            JSONObject().put("text", prompt)
                        ))
                    ))
                }

                val conn = (URL(BASE_URL).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    connectTimeout = 15_000
                    readTimeout    = 30_000
                    outputStream.use { it.write(body.toString().toByteArray()) }
                }

                val response = conn.inputStream.bufferedReader().readText()
                val json     = JSONObject(response)
                return json
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
            } catch (e: Exception) {
                val waitMs = Math.pow(2.0, attempt.toDouble()).toLong() * 1000
                delay(waitMs)
            }
        }
        return "Verbindung zum Studio steht nicht... Funkstille!"
    }

    fun buildPrompt(mode: AppMode, station: Station): String = when (mode) {
        AppMode.RADIO   ->
            "Erzähle mir kurz etwas interessantes über ${station.genre} " +
            "und den Track '${station.nowPlaying}'. Nutze Radio-Sprache."
        AppMode.PODCAST ->
            "Fasse super kurz zusammen, worum es im Podcast '${station.name}' " +
            "und speziell in der Episode '${station.nowPlaying}' gehen könnte."
        AppMode.SPOTIFY ->
            "Gib mir einen coolen Fun Fact zur Playlist '${station.name}'. " +
            "Was macht diesen Mix besonders?"
    }
}
