package com.joy.joyfillandroid


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
class TableField(context: Context): LinearLayout(context){


    val borderColor: String ="#E6E7EA"

    val dataList = listOf(
        arrayOf("First Floor", "Second Floor", "Third Floor"),
        arrayOf("Text 1 at 2", "Text 1 at 2", "Text 1 at 2"),
        arrayOf("Text 1 at 2", "Text 1 at 2", "Text 1 at 2"),
        arrayOf("Text 1 at 2", "Text 1 at 2", "Text 1 at 2")
    )

    init {
        orientation = VERTICAL
        setPadding(25,15,25,15)


        val relativeLayout = RelativeLayout(context)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setPadding(0, 0, 0, dpToPx(10))

        val floorsTextView = TextView(context)
        floorsTextView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0,dpToPx(10),0,dpToPx(0))
        }

        floorsTextView.text = "Floors"
        floorsTextView.setTextColor(Color.parseColor("#3F404D"))
        floorsTextView.textSize = 14f
        floorsTextView.typeface = customTypeFace(500)

        val textViewLayout = LinearLayout(context)
        textViewLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            addRule(RelativeLayout.RIGHT_OF, )
            setMargins(0, 0, dpToPx(25), dpToPx(2))
        }
        textViewLayout.orientation = LinearLayout.HORIZONTAL
        textViewLayout.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_END

        val viewTextView = TextView(context)
        viewTextView.text = "View"
        viewTextView.typeface = customTypeFace(500)
        viewTextView.setTextColor(Color.parseColor("#256FFF"))
        viewTextView.textSize = 14f

        val arrowImageView = ImageView(context)
        arrowImageView.setImageResource(R.drawable.ic_expand_left)
        arrowImageView.layoutParams = LinearLayout.LayoutParams(
            dpToPx(13),
            dpToPx(13),
            1f
        ).apply {
            setMargins(dpToPx(3), dpToPx(4), 0, 0)

        }

        textViewLayout.setOnClickListener {
            removeAllViews()
            val tableView = Table(context)
            addView(tableView)
        }

        val plusThreeTextView = TextView(context)
        plusThreeTextView.typeface = customTypeFace(500)
        plusThreeTextView.textSize = 12f

        plusThreeTextView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            setMargins(0, dpToPx(12), dpToPx(1), 0)
        }


        plusThreeTextView.text = "+62"
        plusThreeTextView.setTextColor(Color.parseColor("#121417"))
        plusThreeTextView.textSize = 12f

        textViewLayout.addView(viewTextView)
        textViewLayout.addView(arrowImageView)

        relativeLayout.addView(floorsTextView)
        relativeLayout.addView(textViewLayout)
        relativeLayout.addView(plusThreeTextView)

        addView(relativeLayout)

        addView(createTable(dataList))

    }


    @SuppressLint("ResourceType")
    fun createTable(data: List<Array<String>>): TableLayout {
        val tableLayout = TableLayout(context)
        val tableLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tableLayout.background = tableBackground()
        tableLayout.layoutParams = tableLayoutParams

        val firstRow = TableRow(context)
        val firstRowParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        firstRow.layoutParams = firstRowParams

        for (i in data[0].indices) {
            val textView = TextView(context)
            val textViewParams = TableRow.LayoutParams(
                dpToPx(133),
                dpToPx(38),
                1f
            )
            textView.gravity = Gravity.CENTER
            textView.textSize = 14f
            textView.setTextColor(Color.parseColor("#121417"))
            textView.setPadding(5, 5, 5, 5)
            textView.text = data[0][i]

            val drawable = GradientDrawable()
            if (i == 0) {
                drawable.cornerRadii = floatArrayOf(30f, 30f, 0f, 0f, 0f, 0f, 0f, 0f)
            } else if (i == data[0].size - 1) {
                drawable.cornerRadii = floatArrayOf(0f, 0f, 30f, 30f, 0f, 0f, 0f, 0f)
            } else {
                drawable.cornerRadius = 0f
            }

            drawable.setStroke(2, Color.parseColor("#E6E7EA")) // Set the stroke color and thickness
            drawable.setColor(Color.parseColor("#F3F4F8"))
            textView.background = drawable

            firstRow.addView(textView, textViewParams)
        }


        tableLayout.addView(firstRow, firstRowParams)

        for (i in 1 until data.size) {
            val tableRow = TableRow(context)
            val tableRowParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableRow.layoutParams = tableRowParams

            for (j in data[i].indices) {
                val editText = EditText(context)
                val editTextParams = TableRow.LayoutParams(
                    dpToPx(133),
                    dpToPx(38),
                    1f

                )
                editText.gravity = Gravity.CENTER
                editText.textSize = 14f
                editText.setTextColor(Color.parseColor("#121417"))
                editText.setPadding(5, 5, 5, 5)
                editText.setText(data[i][j])

                val cellDrawable = GradientDrawable()
                cellDrawable.cornerRadius = 0f
                if (i == 1 && j == 0) {
                    cellDrawable.cornerRadii = floatArrayOf(
                        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
                    )
                } else if (i == 1 && j == data[i].size - 1) {
                    cellDrawable.cornerRadii = floatArrayOf(
                        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
                    )
                } else if (i == data.size - 1 && j == 0) {
                    cellDrawable.cornerRadii = floatArrayOf(
                        0f, 0f, 0f, 0f, 0f, 0f, 30f, 30f
                    )
                } else if (i == data.size - 1 && j == data[i].size - 1) {
                    cellDrawable.cornerRadii = floatArrayOf(
                        0f, 0f, 0f, 0f, 30f, 30f, 0f, 0f
                    )
                }
                cellDrawable.setStroke(1, Color.parseColor("#E6E7EA")) // Set the stroke color and thickness
                editText.background = cellDrawable
                editText.setSingleLine()

                editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        cellDrawable.setStroke(2, Color.parseColor("#1F6BFF"))
                    } else {
                        cellDrawable.setStroke(1, Color.parseColor("#E6E7EA"))
                    }
                }
                tableRow.addView(editText, editTextParams)
            }
            tableLayout.addView(tableRow, tableRowParams)
        }

        return tableLayout
    }

    override fun onDraw(canvas: Canvas?) {
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(dpToPx(12),dpToPx(12),dpToPx(12),dpToPx(12))
        setLayoutParams(layoutParams)
        super.onDraw(canvas)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun customTypeFace(fontWeight: Int): Typeface{
        val typeface = Typeface.create(null, fontWeight, false)
        return typeface
    }

    fun tableBackground(): GradientDrawable{
        val drawable = GradientDrawable()

        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadii = floatArrayOf(
            30f,30f,
            30f,30f,
            30f,30f,
            30f,30f,
        )
        drawable.setStroke(4, Color.parseColor("#E6E7EA"))
        return drawable
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}