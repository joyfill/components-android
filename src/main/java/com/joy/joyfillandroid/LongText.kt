package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class LongText(context: Context): LinearLayout(context) {
    private var longTextTitle =""
    private var longTextValue =""

    val textField = TextField(context)
    val editText = EditText(context)

    init {
        orientation = VERTICAL
        gravity = Gravity.START
        textField.text = longTextTitle
        addView(textField)

        val drawable = ContextCompat.getDrawable(context, R.drawable.custom_background)
        editText.background = drawable

        // set Padding
        val padding = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        editText.setPadding(padding, padding, padding, padding)
        layoutParams.setMargins(dpToPx(context,14), dpToPx(context,5), dpToPx(context,14), dpToPx(context,14))
        editText.layoutParams = layoutParams
        editText.textSize = 14f
        editText.setTextColor(Color.BLACK)
        val inputType = InputType.TYPE_CLASS_TEXT
        editText.setInputType(inputType)

        editText.setSingleLine(false) // Allow multiple lines
        editText.minLines = 10 // Set the minimum number of lines to infinite
        editText.maxLines = Integer.MAX_VALUE // Set the maximum number of lines to infinite
        editText.inputType = inputType or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE // Enable multi-line input
        editText.gravity = Gravity.START
        editText.textSize = 14f
        editText.setText(longTextValue)
        addView(editText)
    }

    fun updateTextFieldTitle(title: String, value: String) {
        textField.text = title
        editText.setText(value)
    }
    override fun onDraw(canvas: Canvas?) {
        OnDrawHelper.onDrawGlobal(context, this)
        super.onDraw(canvas)
    }
}