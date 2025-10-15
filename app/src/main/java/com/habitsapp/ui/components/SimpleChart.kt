package com.habitsapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun SimpleBarChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barWidth = canvasWidth / (data.size * 2)
        val maxValue = data.maxOrNull() ?: 1f

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * canvasHeight * 0.8f
            val x = (index * 2 + 1) * barWidth
            val y = canvasHeight - barHeight

            // Draw bar
            drawRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )

            // Draw value text
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawText(
                    data[index].toInt().toString(),
                    x + barWidth / 2f,
                    y - 10f,
                    paint
                )
            }
        }
    }
}

@Composable
fun SimpleLineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val maxValue = data.maxOrNull() ?: 1f
        val spacing = canvasWidth / (data.size - 1).coerceAtLeast(1)

        // Normalize data
        val normalizedData = data.map { value ->
            (1 - (value / maxValue)) * canvasHeight * 0.8f + canvasHeight * 0.1f
        }

        // Draw lines
        for (i in 0 until normalizedData.size - 1) {
            drawLine(
                color = lineColor,
                start = Offset(i * spacing, normalizedData[i]),
                end = Offset((i + 1) * spacing, normalizedData[i + 1]),
                strokeWidth = 4f
            )
        }

        // Draw points
        normalizedData.forEachIndexed { index, value ->
            drawCircle(
                color = lineColor,
                radius = 6f,
                center = Offset(index * spacing, value)
            )
        }
    }
}