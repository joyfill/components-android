package com.joy.joyfillandroid

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout

class OnDrawHelper {
    companion object {
        fun onDrawGlobal(context: Context, linearLayout: View) {
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(30, 10, 30, 0)
            linearLayout.layoutParams = layoutParams
            layoutParams.gravity = Gravity.CENTER_VERTICAL
        }
    }
}