package com.joy.joyfillandroid
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*



class NumberField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val linearLayout: LinearLayout
    private val textView: TextView
    private val editText: EditText
    private val imageView1: ImageView
    private val imageView2: ImageView
    private var number: Int = 0

    companion object{
        private const val DEFAULT_TEXT_SIZE_SP = 16.8
        private const val DEFAULT_TEXT_COLOR = android.R.color.black
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.number_field_layout, this, true)
        // Initialize views
        linearLayout = findViewById(R.id.textField_layout)
        textView = findViewById(R.id.textView)
        editText = findViewById(R.id.editText)
        imageView1 = findViewById(R.id.imageView_1)
        imageView2 = findViewById(R.id.imageView_2)

        textView.textSize = DEFAULT_TEXT_SIZE_SP.toFloat()
        textView.setPadding(38, 0, 0, 0)
        textView.setTextColor(Color.BLACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            textView.typeface = Typeface.create(null, 500, false)
        }
        editText.inputType = InputType.TYPE_NULL

        // Apply custom attributes if needed
        attrs?.let { applyCustomAttributes(context, it) }

        imageView1.setOnClickListener{
            number += 1
            editText.setText(""+number)
        }

        imageView2.setOnClickListener{
            number -= 1
            editText.setText(""+number)
        }
    }

    private fun applyCustomAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout)
        typedArray.recycle()
    }

    // Mark: Function update values at run time
    fun updateNumberFieldValue(numberTitle: String, numberValue: Int){
        textView.text = numberTitle
        number = numberValue
        editText.setText(""+numberValue)
    }

}