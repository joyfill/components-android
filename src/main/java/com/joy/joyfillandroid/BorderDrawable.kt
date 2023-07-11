package com.joy.joyfillandroid

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable

class BorderDrawable(
    private val borderColor: Int,
    private val cornerRadius: Int,
    private val borderWidth: Int
) : Drawable() {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = borderWidth.toFloat()
        color = borderColor
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val borderRect = RectF()
    private val borderPath = Path()

    override fun draw(canvas: Canvas) {
        val bounds = bounds

        // Draw background
        backgroundPaint.color = if (cornerRadius > 0) Color.TRANSPARENT else borderColor
        canvas.drawRect(bounds, backgroundPaint)

        // Draw border
        if (borderWidth > 0 && cornerRadius > 0) {
            borderRect.set(
                bounds.left + borderWidth / 2f,
                bounds.top + borderWidth / 2f,
                bounds.right - borderWidth / 2f,
                bounds.bottom - borderWidth / 2f
            )
            val adjustedCornerRadius = cornerRadius - borderWidth / 2f
            borderPath.reset()
            borderPath.addRoundRect(borderRect, adjustedCornerRadius, adjustedCornerRadius, Path.Direction.CW)
            canvas.drawPath(borderPath, borderPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        // Not implemented
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not implemented
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.UNKNOWN
    }
}