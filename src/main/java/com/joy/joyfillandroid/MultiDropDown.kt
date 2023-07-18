package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MultiDropDown@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DropDownAdapter
    val autoCompleteTextView = AppCompatAutoCompleteTextView(context)
    private lateinit var bottomSheetDialog: BottomSheetDialog
    var list= ArrayList<RecyclerModel>()

    init {

        addView(createAutoCompleteTextView(context))

    }
    fun createAutoCompleteTextView(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)

        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        linearLayout.gravity = Gravity.START

        val textField = TextField(context)
        textField.text ="MultiDropDown Field"
        linearLayout.addView(textField)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        val backgroundDrawable = createEditTextBackground()

        autoCompleteTextView.id = R.id.autoTextView
        autoCompleteTextView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        autoCompleteTextView.background= backgroundDrawable
        autoCompleteTextView.hint = "Option 1"
        autoCompleteTextView.textSize = 14f
        autoCompleteTextView.setTextColor(Color.parseColor("#121417"))
        autoCompleteTextView.inputType = InputType.TYPE_NULL
        autoCompleteTextView.setPadding(
            convertDpToPx(context, 15),
            convertDpToPx(context, 15),
            convertDpToPx(context, 15),
            convertDpToPx(context, 15)
        )
        autoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_down_24, 0)
        autoCompleteTextView.layoutParams = layoutParams
        layoutParams.setMargins(convertDpToPx(context, 12), convertDpToPx(context, 5),convertDpToPx(context, 12), 0)


        autoCompleteTextView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == KeyEvent.ACTION_UP) {
                createLinearLayout(context)
            }
            return@setOnTouchListener false
        }

        linearLayout.addView(autoCompleteTextView)

        return linearLayout
    }

    fun createLinearLayout(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linearLayout.background = context.getDrawable(R.drawable.bottom_sheet_background)
        }

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.END
        layoutParams.topMargin = convertDpToPx(context, 15)
        layoutParams.rightMargin = convertDpToPx(context, 24)
        layoutParams.bottomMargin = convertDpToPx(context, 12)

        val textView = TextView(context)
        textView.text = "Done"
        textView.setTextColor(Color.parseColor("#0066FF"))
        textView.textSize = 16f
        textView.typeface = Typeface.DEFAULT_BOLD
        textView.layoutParams = layoutParams

        linearLayout.addView(textView)

        val recyclerView = RecyclerView(context)
        recyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, convertDpToPx(context, 200))

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        recyclerView.hasFixedSize()
        list = ArrayList()
        list.add(RecyclerModel("Yes"))
        list.add(RecyclerModel("No"))
        list.add(RecyclerModel("N/A"))

        adapter = DropDownAdapter(list, context)
        recyclerView.adapter= adapter

        recyclerView.addOnItemTouchListener(RecyclerItemClickListenr(context, recyclerView, object : RecyclerItemClickListenr.OnItemClickListener {


            override fun onItemClick(view: View, position: Int) {

                if (position==0){
                    autoCompleteTextView.setText("Selected "+ (position+1))
                    autoCompleteTextView.background = createEditTextBackgroundOnTextChange()
                    autoCompleteTextView.setPadding(
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15)
                    )
                }else if (position==1) {
                    autoCompleteTextView.setText("Selected "+(position+1))
                    autoCompleteTextView.background = createEditTextBackgroundOnTextChange()
                    autoCompleteTextView.setPadding(
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15)
                    )
                } else if (position==2) {
                    autoCompleteTextView.setText("Selected "+(position+1))
                    autoCompleteTextView.background = createEditTextBackgroundOnTextChange()
                    autoCompleteTextView.setPadding(
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15)
                    )
                }else{
                    autoCompleteTextView.setText("Selected 0")
                    autoCompleteTextView.background = createEditTextBackgroundOnTextChange()
                    autoCompleteTextView.setPadding(
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15),
                        convertDpToPx(context, 15)
                    )
                }
            }
            override fun onItemLongClick(view: View?, position: Int) {
                TODO("do nothing")
            }
        }))




        textView.setOnClickListener{
            bottomSheetDialog.dismiss()

        }

        linearLayout.addView(recyclerView)
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
        bottomSheetDialog.setContentView(linearLayout)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
        return linearLayout
    }



    fun convertDpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }


    fun createEditTextBackground(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setStroke(convertDpToPx(context,1), Color.parseColor("#D1D1D6"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(
                convertDpToPx(context,7),
                convertDpToPx(context,7),
                convertDpToPx(context,7),
                convertDpToPx(context,7)
            )
        }
        drawable.cornerRadius = convertDpToPx(context,12).toFloat()
        drawable.setColor(Color.parseColor("#FFFFFF"))

        return drawable
    }


    fun createEditTextBackgroundOnTextChange(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setStroke(3, Color.parseColor("#4776EE"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(
                convertDpToPx(context,7),
                convertDpToPx(context,7),
                convertDpToPx(context,7),
                convertDpToPx(context,7)
            )
        }
        drawable.cornerRadius = convertDpToPx(context,12).toFloat()
        drawable.setColor(Color.parseColor("#FFFFFF"))

        return drawable
    }
}