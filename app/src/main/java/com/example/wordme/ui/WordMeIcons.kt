package com.example.wordme.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object WordMeIcons {

    val Calendar: ImageVector
        get() = ImageVector.Builder(
            name = "Calendar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Main calendar box
            moveTo(4f, 7f)
            lineTo(20f, 7f)
            lineTo(20f, 20f)
            lineTo(4f, 20f)
            close()
            // Divider line
            moveTo(4f, 11f)
            lineTo(20f, 11f)
            // Left hanger
            moveTo(8f, 4f)
            lineTo(8f, 8f)
            // Right hanger
            moveTo(16f, 4f)
            lineTo(16f, 8f)
        }.build()

    val Ribbon: ImageVector
        get() = ImageVector.Builder(
            name = "Ribbon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Ribbon star/badge circle
            moveTo(12f, 11f)
            curveToRelative(3.31f, 0f, 6f, -2.69f, 6f, -6f)
            curveToRelative(0f, -3.31f, -2.69f, -6f, -6f, -6f)
            curveToRelative(-3.31f, 0f, -6f, 2.69f, -6f, 6f)
            curveToRelative(0f, 3.31f, 2.69f, 6f, 6f, 6f)
            close()
        }.path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Star paths or center star
            moveTo(12f, 3f)
            lineTo(13f, 5.5f)
            lineTo(15.5f, 5.5f)
            lineTo(13.5f, 7f)
            lineTo(14.5f, 9.5f)
            lineTo(12f, 8f)
            lineTo(9.5f, 9.5f)
            lineTo(10.5f, 7f)
            lineTo(8.5f, 5.5f)
            lineTo(11f, 5.5f)
            close()
        }.path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Left ribbon tail
            moveTo(9f, 10.5f)
            lineTo(6f, 21f)
            lineTo(10.5f, 18.5f)
            lineTo(12f, 20f)
        }.path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Right ribbon tail
            moveTo(15f, 10.5f)
            lineTo(18f, 21f)
            lineTo(13.5f, 18.5f)
            lineTo(12f, 20f)
        }.build()

    val Speaker: ImageVector
        get() = ImageVector.Builder(
            name = "Speaker",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
            stroke = null
        ) {
            moveTo(9f, 9f)
            lineTo(5f, 9f)
            verticalLineTo(15f)
            horizontalLineTo(9f)
            lineTo(14f, 20f)
            verticalLineTo(4f)
            lineTo(9f, 9f)
            close()
        }.path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            // Medium sound wave
            moveTo(17f, 8f)
            curveToRelative(1.5f, 1.5f, 1.5f, 6.5f, 0f, 8f)
        }.path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            // Outer sound wave
            moveTo(20f, 5f)
            curveToRelative(2.8f, 2.8f, 2.8f, 11.2f, 0f, 14f)
        }.build()

    val SpeechBubble: ImageVector
        get() = ImageVector.Builder(
            name = "SpeechBubble",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(21f, 11.5f)
            curveTo(21f, 15.64f, 16.97f, 19f, 12f, 19f)
            curveTo(10.53f, 19f, 9.15f, 18.66f, 7.94f, 18.06f)
            lineTo(3f, 19.5f)
            lineTo(4.5f, 14.85f)
            curveTo(3.56f, 13.9f, 3f, 12.75f, 3f, 11.5f)
            curveTo(3f, 7.36f, 7.03f, 4f, 12f, 4f)
            curveTo(16.97f, 4f, 21f, 7.36f, 21f, 11.5f)
            close()
        }.build()

    val Pencil: ImageVector
        get() = ImageVector.Builder(
            name = "Pencil",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(3f, 17f)
            lineTo(3f, 21f)
            lineTo(7f, 21f)
            lineTo(20.5f, 7.5f)
            curveTo(21.3f, 6.7f, 21.3f, 5.3f, 20.5f, 4.5f)
            curveTo(19.7f, 3.7f, 18.3f, 3.7f, 17.5f, 4.5f)
            lineTo(3f, 17f)
            close()
            // Divider near top
            moveTo(16f, 6f)
            lineTo(19f, 9f)
        }.build()

    val Sparkles: ImageVector
        get() = ImageVector.Builder(
            name = "Sparkles",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
            stroke = null
        ) {
            // Main Sparkle (Left)
            moveTo(8f, 2f)
            curveTo(8f, 5.5f, 5.5f, 8f, 2f, 8f)
            curveTo(5.5f, 8f, 8f, 10.5f, 8f, 14f)
            curveTo(8f, 10.5f, 10.5f, 8f, 14f, 8f)
            curveTo(10.5f, 8f, 8f, 5.5f, 8f, 2f)
            close()
        }.path(
            fill = SolidColor(Color.Black),
            stroke = null
        ) {
            // Secondary Sparkle (Top-Right)
            moveTo(18f, 10f)
            curveTo(18f, 12.5f, 16f, 14f, 14f, 14f)
            curveTo(16f, 14f, 18f, 15.5f, 18f, 18f)
            curveTo(18f, 15.5f, 20f, 14f, 22f, 14f)
            curveTo(20f, 14f, 18f, 12.5f, 18f, 10f)
            close()
        }.build()

    val Lightbulb: ImageVector
        get() = ImageVector.Builder(
            name = "Lightbulb",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 2f)
            curveTo(7.58f, 2f, 4f, 5.58f, 4f, 10f)
            curveTo(4f, 12.82f, 5.46f, 15.3f, 7.68f, 16.74f)
            lineTo(8.5f, 20f)
            lineTo(15.5f, 20f)
            lineTo(16.32f, 16.74f)
            curveTo(18.54f, 15.3f, 20f, 12.82f, 20f, 10f)
            curveTo(20f, 5.58f, 16.42f, 2f, 12f, 2f)
            close()
            // Bulb bottom threads
            moveTo(9f, 20f)
            lineTo(15f, 20f)
            moveTo(10f, 22f)
            lineTo(14f, 22f)
        }.build()

    val Book: ImageVector
        get() = ImageVector.Builder(
            name = "Book",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Book cover outline
            moveTo(4f, 19.5f)
            curveTo(4f, 18.5f, 5.5f, 17f, 8.5f, 17f)
            lineTo(20f, 17f)
            lineTo(20f, 3f)
            lineTo(8.5f, 3f)
            curveTo(5.5f, 3f, 4f, 4.5f, 4f, 7.5f)
            close()
            // Book page sweep curve at bottom
            moveTo(4f, 7.5f)
            curveTo(4f, 6.5f, 5.5f, 5f, 8.5f, 5f)
            lineTo(20f, 5f)
            // Left binding
            moveTo(4f, 19.5f)
            curveTo(4f, 20.5f, 5.5f, 21.5f, 8.5f, 21.5f)
            lineTo(20f, 21.5f)
        }.build()

    val Trophy: ImageVector
        get() = ImageVector.Builder(
            name = "Trophy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Trophy cup body
            moveTo(6f, 4f)
            lineTo(18f, 4f)
            lineTo(18f, 11f)
            curveTo(18f, 14.5f, 15f, 17f, 12f, 17f)
            curveTo(9f, 17f, 6f, 14.5f, 6f, 11f)
            close()
            // Stem
            moveTo(12f, 17f)
            lineTo(12f, 21f)
            // Base
            moveTo(8f, 21f)
            lineTo(16f, 21f)
        }.path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Left handle
            moveTo(6f, 6f)
            curveTo(3.5f, 6f, 3.5f, 10f, 6f, 10f)
            // Right handle
            moveTo(18f, 6f)
            curveTo(20.5f, 6f, 20.5f, 10f, 18f, 10f)
        }.build()

    val Fire: ImageVector
        get() = ImageVector.Builder(
            name = "Fire",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
            stroke = null
        ) {
            // Custom flame shape path
            moveTo(17.55f, 11.2f)
            curveTo(17.55f, 7.37f, 14.65f, 4.2f, 12f, 2f)
            curveTo(11.9f, 3.86f, 11.23f, 5.75f, 10.23f, 7.34f)
            curveTo(8.42f, 10.24f, 7.5f, 12.6f, 7.5f, 15f)
            curveTo(7.5f, 19.42f, 11.08f, 23f, 15.5f, 23f)
            curveTo(19.92f, 23f, 23.5f, 19.42f, 23.5f, 15f)
            curveTo(23.5f, 13.5f, 23.1f, 12.1f, 22.4f, 10.9f)
            curveTo(22.1f, 12.1f, 21.3f, 13.1f, 20.3f, 13.8f)
            curveTo(19.2f, 14.5f, 18.2f, 14.9f, 17f, 14.9f)
            curveTo(17.3f, 13.7f, 17.55f, 12.4f, 17.55f, 11.2f)
            close()
        }.build()

    val Lock: ImageVector
        get() = ImageVector.Builder(
            name = "Lock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Base lock box
            moveTo(5f, 11f)
            lineTo(19f, 11f)
            lineTo(19f, 21f)
            lineTo(5f, 21f)
            close()
            // Lock shackle loop
            moveTo(8f, 11f)
            verticalLineTo(7f)
            curveTo(8f, 4.8f, 9.8f, 3f, 12f, 3f)
            curveTo(14.2f, 3f, 16f, 4.8f, 16f, 7f)
            verticalLineTo(11f)
        }.build()

    val Star: ImageVector
        get() = ImageVector.Builder(
            name = "Star",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
            stroke = null
        ) {
            moveTo(12f, 2f)
            lineTo(15.09f, 8.26f)
            lineTo(22f, 9.27f)
            lineTo(17f, 14.14f)
            lineTo(18.18f, 21.02f)
            lineTo(12f, 17.77f)
            lineTo(5.82f, 21.02f)
            lineTo(7f, 14.14f)
            lineTo(2f, 9.27f)
            lineTo(8.91f, 8.26f)
            close()
        }.build()

    val ChevronRight: ImageVector
        get() = ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(9f, 6f)
            lineTo(15f, 12f)
            lineTo(9f, 18f)
        }.build()

    val Microphone: ImageVector
        get() = ImageVector.Builder(
            name = "Microphone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            // Mic capsule
            moveTo(12f, 2f)
            curveToRelative(-1.66f, 0f, -3f, 1.34f, -3f, 3f)
            lineTo(9f, 11f)
            curveToRelative(0f, 1.66f, 1.34f, 3f, 3f, 3f)
            curveToRelative(1.66f, 0f, 3f, -1.34f, 3f, -3f)
            lineTo(15f, 5f)
            curveToRelative(0f, -1.66f, -1.34f, -3f, -3f, -3f)
            close()
            // Mic cradle
            moveTo(19f, 10f)
            curveToRelative(0f, 3.87f, -3.13f, 7f, -7f, 7f)
            curveToRelative(-3.87f, 0f, -7f, -3.13f, -7f, -7f)
            // Stand stem
            moveTo(12f, 17f)
            lineTo(12f, 21f)
            // Stand base
            moveTo(8f, 21f)
            lineTo(16f, 21f)
        }.build()

    val Stop: ImageVector
        get() = ImageVector.Builder(
            name = "Stop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
            stroke = null
        ) {
            moveTo(7f, 7f)
            lineTo(17f, 7f)
            curveToRelative(0.55f, 0f, 1f, 0.45f, 1f, 1f)
            lineTo(18f, 16f)
            curveToRelative(0f, 0.55f, -0.45f, 1f, -1f, 1f)
            lineTo(7f, 17f)
            curveToRelative(-0.55f, 0f, -1f, -0.45f, -1f, -1f)
            lineTo(6f, 8f)
            curveToRelative(0f, -0.55f, 0.45f, -1f, 1f, -1f)
            close()
        }.build()
}



