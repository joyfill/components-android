package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class LongText: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
    context,
    attrs,
    defStyle
    ) {
        init()
    }

    private fun init() {
        setSingleLine(false) // Allow multiple lines
        minLines = 10 // Set the minimum number of lines to infinite
        maxLines = Integer.MAX_VALUE // Set the maximum number of lines to infinite
        inputType = inputType or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE // Enable multi-line input
        gravity = Gravity.START
        val drawable = ContextCompat.getDrawable(context, R.drawable.custom_background)
        background = drawable
        textSize = 14f
        val padding = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        setPadding(padding, padding, padding, padding)
        setText("John Doe")
    }

    override fun onDraw(canvas: Canvas?) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(30,10,30,30)
        setLayoutParams(layoutParams)
        layoutParams.gravity = Gravity.START
        super.onDraw(canvas)

    }
}