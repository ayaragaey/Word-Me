package com.example.wordme.ui.theme

import androidx.compose.ui.graphics.Color

// Premium Word Me Color Palette
val NavyPrimary = Color(0xFF0D2A59)      // #0D2A59 Primary dark text/headings
val AccentBlue = Color(0xFF2784F5)       // #2784F5 Primary accent blue
val SoftSkyBlue = Color(0xFFEAF4FF)      // #EAF4FF Light blue/highlight background
val PaleBlueBg = Color(0xFFF6FAFF)       // #F6FAFF Background
val PureWhite = Color(0xFFFFFFFF)        // #FFFFFF Cards
val PaleBlueBorder = Color(0xFFD8E9FC)   // #D8E9FC Border blue
val MutedBlueGrey = Color(0xFF718096)    // #718096 Muted text

// Map to Material 3 standard scheme naming
val LightPrimary = NavyPrimary
val LightSecondary = AccentBlue
val LightTertiary = SoftSkyBlue
val LightBackground = PaleBlueBg
val LightSurface = PureWhite
val LightPrimaryContainer = SoftSkyBlue
val LightOutline = PaleBlueBorder
val LightOnSurfaceVariant = MutedBlueGrey

// Dark Colors fallback (in case dark theme is used, though light-theme is suggested)
val DarkPrimary = Color(0xFFF1F5F9)
val DarkSecondary = Color(0xFF60A5FA)
val DarkTertiary = Color(0xFF1E3A8A)
val DarkBackground = Color(0xFF0B132B)
val DarkSurface = Color(0xFF1C2541)
val DarkPrimaryContainer = Color(0xFF1E293B)
val DarkOutline = Color(0xFF334155)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)