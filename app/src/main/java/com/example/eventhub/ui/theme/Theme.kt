package com.example.eventhub.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val EventHubColorScheme = lightColorScheme(
    primary = Purple,
    secondary = Purplelight,
    tertiary = Purplemidium,
    onPrimary = Color.White,
    background = Color.White,

    surface = Color.White,

    surfaceVariant = Purplelight,

    onSurface = Color.Black,
    outline = Color(0xFFD8C8E6)
)
@Composable
fun EventhubTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme = EventHubColorScheme,

        typography = Typography,

        content = content
    )
}

