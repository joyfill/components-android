package com.joy.joyfillandroid

import android.content.Context
import android.graphics.drawable.GradientDrawable

fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}
