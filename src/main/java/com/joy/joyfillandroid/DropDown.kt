package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent.ACTION_UP
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DropDown @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val autoCompleteTextView = AppCompatAutoCompleteTextView(context)
    val textField = TextField(context)
    private lateinit var bottomSheetDialog: BottomSheetDialog
    var optionYes =""
    var optionNo =""
    var optionNA =""


    init {
     addView(createAutoCompleteTextView(context))
    }

    fun createAutoCompleteTextView(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)

        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        linearLayout.gravity = Gravity.START

        linearLayout.addView(textField)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val backgroundDrawable = createEditTextBackground()

        autoCompleteTextView.id = R.id.autoTextView
        autoCompleteTextView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        autoCompleteTextView.background= backgroundDrawable
        autoCompleteTextView.textSize = 14f
        autoCompleteTextView.setTextColor(Color.parseColor("#121417"))
        autoCompleteTextView.inputType = InputType.TYPE_NULL
        autoCompleteTextView.setPadding(
            dpToPx(context, 15),
            dpToPx(context, 15),
            dpToPx(context, 15),
            dpToPx(context, 15)
        )
        autoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_down_24, 0)
        autoCompleteTextView.layoutParams = layoutParams
        layoutParams.setMargins(dpToPx(context, 12), dpToPx(context, 5),dpToPx(context, 12), dpToPx(context, 10))


        autoCompleteTextView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == ACTION_UP) {
                createCustomCheckboxLayout(context)
            }
            return@setOnTouchListener false
        }

        linearLayout.addView(autoCompleteTextView)

        return linearLayout
    }


    fun createEditTextBackground(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setStroke(dpToPx(context, 1), Color.parseColor("#D1D1D6"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(
                dpToPx(context,7),
                dpToPx(context,7),
                dpToPx(context,7),
                dpToPx(context,7)
            )
        }
        drawable.cornerRadius = dpToPx(context,12).toFloat()
        drawable.setColor(Color.parseColor("#FFFFFF"))

        return drawable
    }


    fun createEditTextBackgroundOnTextChange(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setStroke(3, Color.parseColor("#4776EE"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(
                dpToPx(context,7),
                dpToPx(context,7),
                dpToPx(context,7),
                dpToPx(context,7)
            )
        }
        drawable.cornerRadius = dpToPx(context,12).toFloat()
        drawable.setColor(Color.parseColor("#FFFFFF"))

        return drawable
    }


    //Bottom Sheet UI
    fun createCustomCheckboxLayout(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.setMargins(0, dpToPx(context,15),0,dpToPx(context,30))
        linearLayout.layoutParams = layoutParams

        linearLayout.orientation = LinearLayout.VERTICAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linearLayout.background = context.getDrawable(R.drawable.bottom_sheet_background)
        }
        linearLayout.setPadding(0, 0, 0, dpToPx(context, 5))

        val checkboxYes = createCustomCheckbox(context, optionYes)
        val view1 = createSeparatorView(context)
        val checkboxNo = createCustomCheckbox(context, optionNo)
        val view2 = createSeparatorView(context)
        val checkboxNA = createCustomCheckbox(context, optionNA)
        val view3 = createSeparatorView(context)

        linearLayout.addView(checkboxYes)
        linearLayout.addView(view1)
        linearLayout.addView(checkboxNo)
        linearLayout.addView(view2)
        linearLayout.addView(checkboxNA)
        linearLayout.addView(view3)

        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
        bottomSheetDialog.setContentView(linearLayout)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()

        return linearLayout
    }

    // Check box functionality
    private fun createCustomCheckbox(context: Context, text: String): CheckBox {
        val checkBox = CheckBox(context)
        checkBox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        checkBox.buttonDrawable = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            checkBox.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.custom_checkbox,
                0,
                0,
                0
            )
        }
        checkBox.compoundDrawablePadding = dpToPx(context, 20)
        checkBox.textSize = 18f
        checkBox.setPadding(dpToPx(context, 24), dpToPx(context, 14), 0, dpToPx(context, 14))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkBox.setTextColor(context.getColor(R.color.black))
        }
        checkBox.text = text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            checkBox.typeface = Typeface.create(null, 400, false)
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                autoCompleteTextView.setText(text)
                val backgroundDrawable1 = createEditTextBackgroundOnTextChange()
                checkBox.setBackgroundColor(Color.parseColor("#E3E3E3"))
                autoCompleteTextView.background = backgroundDrawable1
                autoCompleteTextView.setPadding(
                    dpToPx(context, 15),
                    dpToPx(context, 15),
                    dpToPx(context, 15),
                    dpToPx(context, 15)
                )
                bottomSheetDialog.dismiss()
            }
        }

        return checkBox
    }

    private fun createSeparatorView(context: Context): View {
        val separatorView = View(context)
        separatorView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 1)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            separatorView.setBackgroundColor(context.getColor(R.color.gray))
        }
        return separatorView
    }

    fun updateValue(textTitle: String, hint: String, textValue: String, YesCheck:String, NoCheck:String, NACheck:String){
        textField.text = textTitle
        autoCompleteTextView.setHint(hint)
        autoCompleteTextView.setText(textValue)
        optionYes = YesCheck
        optionNo = NoCheck
        optionNA = NACheck
    }
}