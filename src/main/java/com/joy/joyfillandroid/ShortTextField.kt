package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class ShortTextField(context: Context): LinearLayout(context) {
    private var shortTextTitle = ""
    private var shortTextValue = ""
    val textField = TextField(context)
    private val editText = EditText(context)

    init {
        orientation = VERTICAL
        gravity = Gravity.START
        textField.text = shortTextTitle
        addView(textField)

        val drawable = ContextCompat.getDrawable(context, R.drawable.custom_background)
        editText.background = drawable

        // set Padding
        val padding = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        editText.setPadding(padding, padding, padding, padding)
        val  margin = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(dpToPx(context,14), dpToPx(context,5), dpToPx(context,14), dpToPx(context,14))
        editText.layoutParams = layoutParams
        editText.setText(shortTextValue)
        editText.textSize = 14f
        editText.setTextColor(Color.BLACK)
        val inputType = InputType.TYPE_CLASS_TEXT
        editText.setInputType(inputType)
        // set Single Line

        editText.setSingleLine(true)
        addView(editText)
    }

    override fun onDraw(canvas: Canvas?) {
        OnDrawHelper.onDrawGlobal(context, this)
        super.onDraw(canvas)
    }

     fun updateTextFieldTitle(title: String, value: String) {
        textField.text = title
         editText.setText(value)
    }
}