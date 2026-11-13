package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Immutable
data class SjtExtendedColors(
    val headerForest: Color,
    val headerForestAccent: Color,
    val onHeaderForest: Color,
    val cardBorder: Color,
    val noticeBackground: Color,
    val noticeBorder: Color,
    val noticeText: Color,
    val bestSelectedBackground: Color,
    val bestSelectedBorder: Color,
    val bestSelectedContent: Color,
    val worstSelectedBackground: Color,
    val worstSelectedBorder: Color,
    val worstSelectedContent: Color,
    val mutedText: Color,
    val progressTrack: Color
)

val LocalSjtColors = staticCompositionLocalOf {
    SjtExtendedColors(
        headerForest = ForestDeep,
        headerForestAccent = ForestPrimary,
        onHeaderForest = Color.White,
        cardBorder = BorderSage,
        noticeBackground = NoticeAmberBg,
        noticeBorder = NoticeAmberBorder,
        noticeText = NoticeAmberText,
        bestSelectedBackground = SelectedMintBg,
        bestSelectedBorder = SelectedMintBorder,
        bestSelectedContent = ForestPrimary,
        worstSelectedBackground = WorstRoseBg,
        worstSelectedBorder = WorstRoseBorder,
        worstSelectedContent = ErrorCrimson,
        mutedText = MutedSage,
        progressTrack = BorderSage
    )
}

private val LightExtendedColors = SjtExtendedColors(
    headerForest = ForestDeep,
    headerForestAccent = ForestPrimary,
    onHeaderForest = Color.White,
    cardBorder = BorderSage,
    noticeBackground = NoticeAmberBg,
    noticeBorder = NoticeAmberBorder,
    noticeText = NoticeAmberText,
    bestSelectedBackground = SelectedMintBg,
    bestSelectedBorder = SelectedMintBorder,
    bestSelectedContent = ForestPrimary,
    worstSelectedBackground = WorstRoseBg,
    worstSelectedBorder = WorstRoseBorder,
    worstSelectedContent = ErrorCrimson,
    mutedText = MutedSage,
    progressTrack = BorderSage
)

private val DarkExtendedColors = SjtExtendedColors(
    headerForest = Color(0xFF0E2B23),
    headerForestAccent = Color(0xFF1A473A),
    onHeaderForest = Color(0xFFF2F7F4),
    cardBorder = DarkBorderSage,
    noticeBackground = DarkNoticeAmberBg,
    noticeBorder = DarkNoticeAmberBorder,
    noticeText = DarkNoticeAmberText,
    bestSelectedBackground = DarkSelectedMintBg,
    bestSelectedBorder = DarkPrimaryMint,
    bestSelectedContent = DarkPrimaryMint,
    worstSelectedBackground = DarkWorstRoseBg,
    worstSelectedBorder = DarkWorstRoseBorder,
    worstSelectedContent = DarkErrorCrimson,
    mutedText = DarkMutedSage,
    progressTrack = DarkBorderSage
)

private val LightColorScheme = lightColorScheme(
    primary = ForestPrimary,
    onPrimary = Color.White,
    primaryContainer = SelectedMintBg,
    onPrimaryContainer = ForestDeep,
    secondary = MutedSage,
    onSecondary = Color.White,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = InkDark,
    tertiary = NoticeAmberBorder,
    onTertiary = Color.White,
    background = WashBackground,
    onBackground = InkDark,
    surface = PaperWhite,
    onSurface = InkDark,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = MutedSage,
    outline = BorderSage,
    outlineVariant = BorderSage,
    error = ErrorCrimson,
    onError = Color.White,
    errorContainer = WorstRoseBg,
    onErrorContainer = ErrorCrimson
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryMint,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkSelectedMintBg,
    onPrimaryContainer = DarkInk,
    secondary = DarkMutedSage,
    onSecondary = DarkOnPrimary,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = DarkInk,
    tertiary = DarkNoticeAmberBorder,
    onTertiary = DarkOnPrimary,
    background = DarkBackground,
    onBackground = DarkInk,
    surface = DarkSurface,
    onSurface = DarkInk,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkMutedSage,
    outline = DarkBorderSage,
    outlineVariant = DarkBorderSage,
    error = DarkErrorCrimson,
    onError = DarkOnPrimary,
    errorContainer = DarkWorstRoseBg,
    onErrorContainer = DarkErrorCrimson
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun NeveloszuloSjtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalSjtColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}
