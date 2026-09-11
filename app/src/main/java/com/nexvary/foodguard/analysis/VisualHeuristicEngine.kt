package com.nexvary.foodguard.analysis

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.max

data class ImageSignalReport(
    val averageLuminance: Int,
    val averageSaturation: Int,
    val darkPixelRatio: Float,
    val sampledPixels: Int,
    val needsRetake: Boolean,
    val observations: List<String>
)

object VisualHeuristicEngine {
    fun analyze(bitmap: Bitmap): ImageSignalReport {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return ImageSignalReport(0, 0, 0f, 0, true, listOf("Image could not be read."))
        }

        val step = max(1, max(bitmap.width, bitmap.height) / 120)
        var lumaSum = 0L
        var saturationSum = 0L
        var darkPixels = 0
        var sampled = 0
        val hsv = FloatArray(3)

        var y = 0
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val luma = ((0.2126f * r) + (0.7152f * g) + (0.0722f * b)).toInt()
                Color.RGBToHSV(r, g, b, hsv)

                lumaSum += luma
                saturationSum += (hsv[1] * 255f).toInt()
                if (luma < 45) darkPixels++
                sampled++
                x += step
            }
            y += step
        }

        val avgLuma = if (sampled == 0) 0 else (lumaSum / sampled).toInt()
        val avgSat = if (sampled == 0) 0 else (saturationSum / sampled).toInt()
        val darkRatio = if (sampled == 0) 0f else darkPixels.toFloat() / sampled.toFloat()
        val observations = mutableListOf<String>()

        if (avgLuma < 55) observations += "The image is dark; retake it in brighter neutral light."
        if (avgLuma > 225) observations += "The image is overexposed; reduce glare before analysis."
        if (darkRatio > 0.55f) observations += "Most of the frame is very dark, which can hide surface details."
        if (avgSat < 18) observations += "Color information is limited; neutral lighting may improve visual comparison."
        if (observations.isEmpty()) observations += "Image quality is suitable for visual comparison."

        return ImageSignalReport(
            averageLuminance = avgLuma,
            averageSaturation = avgSat,
            darkPixelRatio = darkRatio,
            sampledPixels = sampled,
            needsRetake = avgLuma < 55 || avgLuma > 225 || darkRatio > 0.55f,
            observations = observations
        )
    }
}
