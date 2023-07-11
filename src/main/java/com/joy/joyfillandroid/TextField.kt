package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView

class TextField(context: Context?) : AppCompatTextView(context!!) {

    private var borderColor: Int = 0
    private var cornerRadius: Int = 0
    private var borderWidth: Int = 0
    private var isBold: Boolean = false
    private var isItalic: Boolean = false
    private var fontName: String? = null

    companion object {
        private const val DEFAULT_TEXT_SIZE_SP = 6f
        private const val DEFAULT_TEXT_COLOR = android.R.color.black
    }

    init {
        // Read attributes from XML

        text = "Text Field"
        textSize = TextField.DEFAULT_TEXT_SIZE_SP
        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER_VERTICAL
        linearLayout.layoutParams = layoutParams
        layoutParams.setMargins(10,10,10,10)

        setTextColor(Color.BLACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            typeface = Typeface.create(null, 500, false)
        }
        val padding = resources.getDimensionPixelSize(R.dimen.custom_edittext_padding)
        setPadding(16, 0, 0, 10)

        // Set default text color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(context!!.getColor(TextField.DEFAULT_TEXT_COLOR))
        }


        val typedArray = context!!.obtainStyledAttributes(R.styleable.CustomTextView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            borderColor = typedArray.getColor(
                R.styleable.CustomTextView_borderColor,
                context!!.getColor(android.R.color.transparent)
            )
        }

        cornerRadius = typedArray.getDimensionPixelSize(
            R.styleable.CustomTextView_cornerRadius,
            0
        )

        borderWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomTextView_borderWidth,
            0
        )

        isBold = typedArray.getBoolean(R.styleable.CustomTextView_boldText, false)
        isItalic = typedArray.getBoolean(R.styleable.CustomTextView_italicText, false) //


        applyProperties()

        typedArray.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(30,10,30,10)
        setLayoutParams(layoutParams)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
    }


    //MARK:- set Properties
    private fun applyProperties() {
        setBorderColor(borderColor)
        setCornerRadius(cornerRadius)
        setBorderWidth(borderWidth)
        setBold(isBold)
        setItalic(isItalic)
        setTextSize(textSize)
        setFontName(fontName)
    }

    // Sets the border color text of the textField
    fun setBorderColor(color: Int) {
        borderColor = color
        updateBorder()
    }

    // Sets the corner radius text of the textField
    fun setCornerRadius(radius: Int) {
        cornerRadius = radius
        updateBorder()
    }
    // Sets the border with text of the textField
    fun setBorderWidth(width: Int) {
        borderWidth = width
        updateBorder()
    }

    // Sets the bold text of the textField
    fun setBold(isBold: Boolean) {
        this.isBold = isBold
        updateTextStyle()
    }
    // Sets the italice text of the textField
    fun setItalic(isItalic: Boolean) {
        this.isItalic = isItalic
        updateTextStyle()
    }

    // Sets the text size of the textField
    fun setTextSize1(size: Float) {
        textSize = size
        textSize = size
        updateTextSize()
    }

    fun setFontName(fontName: String?) {
        this.fontName = fontName
        updateFont()
    }

    private fun updateBorder() {
        val borderDrawable = BorderDrawable(
            borderColor,
            cornerRadius,
            borderWidth
        )
        background = borderDrawable
    }

    private fun updateTextStyle() {
        val style = when {
            isBold && isItalic -> Typeface.BOLD_ITALIC
            isBold -> Typeface.BOLD
            isItalic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
        setTypeface(typeface, style)
    }

    private fun updateTextSize() {
        setTextSize1(textSize)
    }

    private fun updateFont() {
        fontName?.let {
            val typeface = Typeface.createFromAsset(context.assets, "fonts/$it.ttf")
            setTypeface(typeface)
        }
    }
}