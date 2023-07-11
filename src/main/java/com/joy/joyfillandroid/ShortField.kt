package com.joy.joyfillandroid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

@SuppressLint("AppCompatCustomView", "ResourceAsColor")
class ShortField(context: Context) : EditText(context) {


    init {
        val textField = TextField(context)
        val linearLayout = LinearLayout(getContext())
        // Apply custom design modifications
        val drawable = ContextCompat.getDrawable(context, R.drawable.custom_background)
        background = drawable

        // set Padding
        val padding = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        setPadding(padding, padding, padding, padding)
        val  margin = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = layoutParams
        //set margins
        layoutParams.setMargins(margin, margin, margin, margin)

        setText("Jane Doe")
        textSize = 14f
        setTextColor(Color.BLACK)
        val inputType = InputType.TYPE_CLASS_TEXT
        setInputType(inputType)
        // set Single Line
        setSingleLine(true)

        textField.text = "Text Field"
        linearLayout.addView(textField)
        addView(linearLayout)
    }

    private fun addView(linearLayout: LinearLayout) {
    val parentView = LinearLayout(context)
    parentView.addView(linearLayout)

    }


    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        // Perform actions when the text changes
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(30,10,30,30)
        setLayoutParams(layoutParams)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
    }

}