package com.joy.joyfillandroid



import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.P)
class Table @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    val width50dp = 50.dp
    val width133dp = 133.dp
    val height40dp = 40.dp
    val textColor ="#121417"
    val tableBorderColor = "#F3F4F8"
    var highlightedRowIndex = -1
    var selectedRowCell= 0
    var selectedCellRowIndexOf= 0
    private var selectedCell: LinearLayout? = null
    val addRowLayout = LinearLayout(context)
    val moreLayout = LinearLayout(context)
    var addNewIndex: Int = 0

    val tableLayout = TableLayout(context)
    private var selectedEditText: EditText? = null
    private var selectedRow: TableRow? = null

    val headRow = TableRow(context)
    private var selectedRowIndex: Int = -1

    private var popupWindow: PopupWindow? = null
    private lateinit var popupWindowKeyBoard: PopupWindow
    private var focusedEditText: EditText? = null
    private var isKeyboardShowing = false

    val dataArray = mutableListOf(
        DataItem("Text 1 at 1", R.drawable.img_box_fill),
        DataItem("Text 1 at 1", R.drawable.img_box_fill),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Lorem ipsum dolor sit amet, consectetur adipiscing. lit. Pellentesque vel rutrum nibh,", R.drawable.img_box),
        DataItem("Lorem dolor sit amet, consectetur adipiscing.", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 1", R.drawable.img_box),
        DataItem("Text 1 at 10", R.drawable.img_box),
        DataItem("Text 1 at 11", R.drawable.img_box),
        DataItem("Text 1 at 12", R.drawable.img_box),
        DataItem("Text 1 at 121", R.drawable.img_box),
        DataItem("Text 1 at 112", R.drawable.img_box),
    )


    init {
        orientation = VERTICAL


        val relativeLayout = RelativeLayout(context)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setPadding(0, 0, 0, dpToPx(0))

        val floorsTextView = TextView(context)
        floorsTextView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0,dpToPx(10),0,dpToPx(0))
        }
        floorsTextView.text = "Interior Images"
        floorsTextView.setTextColor(Color.parseColor("#3F404D"))
        floorsTextView.textSize = 14f
        floorsTextView.gravity = RelativeLayout.CENTER_VERTICAL
        floorsTextView.typeface = customTypeFace(700)


        moreLayout.visibility = View.GONE
        moreLayout.setPadding(30,15,30,15)
        moreLayout.background = buttonBackground()
        moreLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)

            setMargins(0, 0, dpToPx(110), 0)
        }
        moreLayout.orientation = LinearLayout.HORIZONTAL
        moreLayout.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_END

        moreLayout.setOnClickListener {
            showDropdown(moreLayout, context)
        }

        val moreText = TextView(context)
        moreText.text = "More"
        moreText.typeface = customTypeFace(400)
        moreText.setTextColor(Color.parseColor("#121417"))
        moreText.textSize = 14f

        val addImage = ImageView(context)
        addImage.setImageResource(R.drawable.expand_down)
        addImage.layoutParams = LinearLayout.LayoutParams(
            dpToPx(18),
            dpToPx(18),
            1f
        ).apply {
            setMargins(dpToPx(6), dpToPx(2), 0, 0)

        }


        addRowLayout.setPadding(30,15,30,15)
        addRowLayout.background = buttonBackground()
        addRowLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        }
        addRowLayout.orientation = LinearLayout.HORIZONTAL
        addRowLayout.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_END

        val addRowText = TextView(context)
        addRowText.text = "Add Row"
        addRowText.typeface = customTypeFace(400)
        addRowText.setTextColor(Color.parseColor("#121417"))
        addRowText.textSize = 14f

        val PlusImage = ImageView(context)
        PlusImage.setImageResource(R.drawable.add_round)
        PlusImage.layoutParams = LinearLayout.LayoutParams(
            dpToPx(18),
            dpToPx(18),
            1f
        ).apply {
            setMargins(dpToPx(6), dpToPx(2), 0, 0)

        }

        moreLayout.addView(moreText)
        moreLayout.addView(addImage)
        addRowLayout.addView(addRowText)
        addRowLayout.addView(PlusImage)

        relativeLayout.addView(floorsTextView)
        relativeLayout.addView(moreLayout)
        relativeLayout.addView(addRowLayout)

        addView(relativeLayout)

        val rootView = LinearLayout(context)
        rootView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        rootView.orientation = LinearLayout.VERTICAL
        rootView.setPadding(5.dp, 5.dp, 5.dp, 5.dp)



        val horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        horizontalScrollView.setPadding(0, 5.dp, 0, 0)
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.viewTreeObserver.addOnScrollChangedListener(
            ViewTreeObserver.OnScrollChangedListener {
                // Get the new horizontal scroll position
                val scrollX = horizontalScrollView.scrollX

                // Update the scroll position to maintain the selection
                horizontalScrollView.scrollTo(scrollX, 0)
            }
        )

        tableLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        headRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headRow.setBackgroundColor(Color.parseColor("#F3F4F8"))

        val blankHeadCell = TextView(context)
        blankHeadCell.background = topLeftCellBackground()
        blankHeadCell.layoutParams = TableRow.LayoutParams(
            width50dp, height40dp
        )
        blankHeadCell.gravity = android.view.Gravity.CENTER
        headRow.addView(blankHeadCell)

        val hashCell = TextView(context)
        hashCell.background = getCellBackground()
        hashCell.layoutParams = TableRow.LayoutParams(
            width50dp, height40dp
        )
        hashCell.gravity = android.view.Gravity.CENTER
        hashCell.text ="#"
        hashCell.setTextColor(Color.parseColor(textColor))
        headRow.addView(hashCell)

        val firstFloor = TextView(context)
        firstFloor.background = getCellBackground()
        firstFloor.setTextColor(Color.parseColor(textColor))
        firstFloor.layoutParams = TableRow.LayoutParams(
            width133dp, height40dp
        )
        firstFloor.gravity = android.view.Gravity.CENTER
        firstFloor.text= "First Floor"
        headRow.addView(firstFloor)

        val secondFloor = TextView(context)
        secondFloor.background = getCellBackground()
        secondFloor.setTextColor(Color.parseColor(textColor))
        secondFloor.layoutParams = TableRow.LayoutParams(
            width133dp, height40dp
        )
        secondFloor.gravity = android.view.Gravity.CENTER
        secondFloor.text= "Second Floor"
        headRow.addView(secondFloor)

        val thirdFloor = TextView(context)
        thirdFloor.background = getCellBackground()
        thirdFloor.setTextColor(Color.parseColor(textColor))
        thirdFloor.layoutParams = TableRow.LayoutParams(
            width133dp, height40dp
        )
        thirdFloor.gravity = android.view.Gravity.CENTER
        thirdFloor.text= "Third Floor"
        headRow.addView(thirdFloor)
        val dropDownCell = TextView(context)
        dropDownCell.background = topRightCellBackground()
        dropDownCell.setTextColor(Color.parseColor(textColor))
        dropDownCell.layoutParams = TableRow.LayoutParams(
            width133dp, height40dp
        )
        dropDownCell.gravity = android.view.Gravity.CENTER
        dropDownCell.text= "DropDown Cell"
        headRow.addView(dropDownCell)

        tableLayout.addView(headRow)
        val scrollView = ScrollView(context)
        scrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        scrollView.isVerticalScrollBarEnabled = false
        scrollView.isHorizontalScrollBarEnabled = false
        dataArray.forEachIndexed { index, dataRow ->
            addNewIndex = index+1
            val newIndex = index+1
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.background = getRowBackground()
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = TableRow.LayoutParams(width50dp, TableRow.LayoutParams.MATCH_PARENT)
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout.background = getCellBackground()
            val imageView = ImageView(context)
            val imageViewLayoutParams = TableRow.LayoutParams(15.dp, 15.dp)
            imageView.layoutParams = imageViewLayoutParams
            imageViewLayoutParams.setMargins(0, 10.dp, 0,0)
            imageView.setImageResource(R.drawable.unckeck_circle_img)
            linearLayout.addView(imageView)
            tableRow.addView(linearLayout)

            val hashLinearLayout = LinearLayout(context)
            hashLinearLayout.layoutParams = TableRow.LayoutParams(width50dp, TableRow.LayoutParams.MATCH_PARENT)
            hashLinearLayout.gravity = Gravity.CENTER_HORIZONTAL
            hashLinearLayout.background = getCellBackground()

            val hash_Id = TextView(context)
            val hashIdLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
            hashIdLayoutParams.setMargins(0, 10.dp, 0,0)
            hash_Id.layoutParams = hashIdLayoutParams
            hash_Id.setTextColor(Color.parseColor(textColor))
            hash_Id.setText(""+newIndex)
            hashLinearLayout.addView(hash_Id)
            tableRow.addView(hashLinearLayout)

            val Text_at_1 = LinearLayout(context)
            Text_at_1.background = getCellBackground()
            Text_at_1.gravity = Gravity.START
            Text_at_1.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            val TextAtEditText = EditText(context)

            val textat_1 = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            TextAtEditText.background =  editTextBackground()
            TextAtEditText.layoutParams = textat_1
            TextAtEditText.setPadding(10.dp, 10.dp,10.dp,10.dp)
            TextAtEditText.textSize = 12f
            TextAtEditText.setTextColor(Color.parseColor(textColor))
            TextAtEditText.setText(dataRow.text as String)
            TextAtEditText.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    highlightCell(Text_at_1, tableRow, tableLayout, newIndex)
                    showKeyboardButtons(TextAtEditText, context, tableRow, tableLayout, Text_at_1)
                }else{
                    hideKeyBoard(TextAtEditText)
                    TextAtEditText.clearFocus()
                }
            }

            Text_at_1.addView(TextAtEditText)
            tableRow.addView(Text_at_1)

            val linearLayout2 = LinearLayout(context)
            linearLayout2.background = getCellBackground()
            linearLayout2.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout2.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            linearLayout2.orientation = LinearLayout.HORIZONTAL

            val imageView2 = ImageView(context)
            val imaageView2LayoutParams = LinearLayout.LayoutParams(25.dp, 25.dp)
            imageView2.layoutParams = imaageView2LayoutParams
            imaageView2LayoutParams.setMargins(0,10.dp, 0,0)
            imageView2.setImageResource(dataRow.imageResource as Int)

            val textView3 = TextView(context)
            val textView3LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )
            textView3.layoutParams = textView3LayoutParams
            textView3.textSize =12f
            textView3LayoutParams.setMargins(1.dp, 18.dp, 0,0)
            textView3.gravity = Gravity.BOTTOM
            textView3.setTextColor(Color.parseColor("#6B6C7C"))
            textView3.text = "+4"

            if (index !=0 && index !=1){
                textView3.visibility = View.INVISIBLE
            }


            linearLayout2.addView(imageView2)
            linearLayout2.addView(textView3)
            tableRow.addView(linearLayout2)

            val Text_at_2 = LinearLayout(context)
            Text_at_2.background = getCellBackground()
            Text_at_2.gravity = Gravity.CENTER_HORIZONTAL
            Text_at_2.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            Text_at_2.orientation = LinearLayout.HORIZONTAL

            val TextAtEditText_2 = EditText(context)
            val textat_2 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            TextAtEditText_2.background = editTextBackground()

            TextAtEditText_2.layoutParams = textat_2
            TextAtEditText_2.textSize = 12f
            TextAtEditText_2.setTextColor(Color.parseColor(textColor))
            TextAtEditText_2.setText("Collum = "+ newIndex)
            TextAtEditText_2.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    highlightCell(Text_at_2, tableRow, tableLayout, newIndex)
                    showKeyboardButtons(TextAtEditText, context, tableRow, tableLayout, Text_at_2)
                }else{
                    hideKeyBoard(TextAtEditText_2)
                    TextAtEditText_2.clearFocus()
                }
            }

            Text_at_2.addView(TextAtEditText_2)
            tableRow.addView(Text_at_2)

            val linearLayout3 = LinearLayout(context)
            linearLayout3.background = getCellBackground()
            linearLayout3.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout3.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            linearLayout3.orientation = LinearLayout.HORIZONTAL
            linearLayout3.setOnClickListener {
                val multiDropDown = MultiDropDown(context)
                multiDropDown.createLinearLayout(context)
            }
            val textView6 = TextView(context)
            val textView6LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )
            textView6.layoutParams = textView6LayoutParams
            textView6LayoutParams.setMargins(0,10.dp, 0,0)
            textView6.setTextColor(Color.parseColor(textColor))
            textView6.text = "Text Feild"
            textView6.textSize = 14f
            val imageView3 = ImageView(context)
            val imageView3LayoutParams = TableRow.LayoutParams(20.dp, 20.dp)
            imageView3.layoutParams = imageView3LayoutParams
            imageView3LayoutParams.setMargins(0,10.dp,0,0)
            imageView3.setImageResource(R.drawable.ic_baseline_down)
            imageView3.setPadding(5.dp, 0, 0, 0)
            linearLayout3.addView(textView6)
            linearLayout3.addView(imageView3)
            tableRow.addView(linearLayout3)
            addRowLayout.setOnClickListener {
                val newText = "New Text at 1"
                val newImageResource = R.drawable.img_box
                val newDataItem = DataItem(newText, newImageResource)
                dataArray.add(newDataItem)
                updateTableLayout(tableLayout, dataArray)
            }

            if (index == dataArray.size - 1) {
                // If it's the last row, set the custom background with rounded corners
                tableRow.background = createRoundedBackgroundForLastRow(30f)
                linearLayout.background = lastRowLeftBottom()
                linearLayout3.background = lastRowRightBottom()
            } else {
                // If it's not the last row, set the default background
                tableRow.background = getRowBackground()
            }

            tableLayout.addView(tableRow)

            linearLayout.setOnClickListener {
                highlightRow(tableRow, newIndex, tableLayout, moreLayout, imageView, Text_at_1)
                onRowClicked(tableRow, imageView, Text_at_1)
                headRow.setBackgroundColor(Color.parseColor("#F3F4F8"))
                linearLayout.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                linearLayout2.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                linearLayout3.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                Text_at_1.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                Text_at_2.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                hashLinearLayout.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
            }
        }
        horizontalScrollView.addView(tableLayout)
        scrollView.addView(horizontalScrollView)
        rootView.addView(scrollView)
        addView(rootView)
    }

    private fun onCellClicked(tableRow: TableRow, textAt1: LinearLayout) {
        if (textAt1 == selectedCell) {
            selectedCell = null
            tableRow.background = getRowBackground() // Set the background for the row to default
        } else {
            selectedCell?.let { previousSelectedCell ->
                val previousSelectedTableRow = previousSelectedCell.parent as TableRow
                previousSelectedTableRow.background = getRowBackground() // Set the background for the previously selected row to default
            }
            selectedCell = textAt1
            tableRow.background = getSelectedCellBackground() // Set the background for the row to selected color
        }
    }


    private fun onRowClicked(row: TableRow, imageView: ImageView, textAt1: LinearLayout) {
        if (row == selectedRow) {
            selectedRow = null
            imageView.setImageResource(R.drawable.unckeck_circle_img) // Set the default image
            textAt1.background = getCellBackground()
        } else {
            selectedRow?.let { previousSelectedRow ->
                val previousSelectedLinearLayout = previousSelectedRow.getChildAt(0) as LinearLayout
                val previousSelectedImageView = previousSelectedLinearLayout.getChildAt(0) as ImageView
                val previousSelectedTextAt1 = previousSelectedRow.getChildAt(selectedRowCell) as LinearLayout
                previousSelectedTextAt1.background = getCellBackground()
                previousSelectedImageView.setImageResource(R.drawable.unckeck_circle_img) // Set the default image for the previously selected row
            }
            selectedRow = row
            imageView.setImageResource(R.drawable.ckeck_circle_img) // Set the selected image
        }
    }

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()

    override fun onDraw(canvas: Canvas?) {
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(dpToPx(8),dpToPx(10),dpToPx(0),dpToPx(0))
        setLayoutParams(layoutParams)
        super.onDraw(canvas)
    }

    private fun highlightCell(selectedCell: View, tableRow: TableRow, tableLayout: TableLayout,rowIndex: Int) {
        val selectedCellRowIndex = tableRow.indexOfChild(selectedCell)
        selectedRowCell = selectedCellRowIndex
        val clickedRow = tableLayout.getChildAt(rowIndex) as TableRow
        selectedCellRowIndexOf = rowIndex
        for (i in 0 until tableRow.childCount) {
            val cell = tableRow.getChildAt(i)
            if (cell == selectedCell) {
                cell.setOnClickListener {
                    tableLayout.getChildAt(selectedRowIndex)?.clearFocus()
                    selectedRowIndex = -1
                    cell.requestFocus()
                    cell.background = getSelectedCellBackground()
                }
                cell.background = getSelectedCellBackground()
            } else {
                cell.background = getCellBackground()
            }
        }

        val tableLayout = tableRow.parent as TableLayout
        val columnIndex = tableRow.indexOfChild(selectedCell)
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            if (row != tableRow) {
                val cell = row.getChildAt(columnIndex)
                cell.background = getCellBackground()
            }
        }
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun customTypeFace(fontWeight: Int): Typeface{
        val typeface = Typeface.create(null, fontWeight, false)
        return typeface
    }
    //Mark: Cell Background
    fun getCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun topLeftCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(30f, 30f, 0f, 0f, 0f, 0f, 0f, 0f)
        drawable.setStroke(2, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun topRightCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 30f, 30f, 0f, 0f, 0f, 0f)
        drawable.setStroke(2, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun editTextBackground(): GradientDrawable{
        val editDrawable = GradientDrawable()
        editDrawable.shape = GradientDrawable.RECTANGLE
        return editDrawable
    }

    fun getRowBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2 , Color.parseColor("#E2E3E7"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun getNewRowBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(4 , Color.parseColor("#1F6BFF"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    fun getSelectedCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2 , Color.parseColor("#1F6BFF"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun createCellDrawableWithTopBottomStrokes(strokeColor: Int, strokeWidth: Int): ShapeDrawable {
        val cellDrawable = ShapeDrawable(RectShape())
        cellDrawable.paint.style = android.graphics.Paint.Style.STROKE
        cellDrawable.paint.strokeWidth = strokeWidth.toFloat()
        cellDrawable.paint.color = strokeColor
        return cellDrawable
    }

    fun buttonBackground(): GradientDrawable{
        val buttonDrawable = GradientDrawable()
        buttonDrawable.setColor(Color.WHITE)
        buttonDrawable.shape = GradientDrawable.RECTANGLE
        buttonDrawable.cornerRadius = 15f
        buttonDrawable.setStroke(3, Color.parseColor("#E2E3E7"))
        return buttonDrawable
    }

    fun createRoundedBackgroundForLastRow(radius: Float): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setStroke(2, Color.parseColor("#E2E3E7"))
        // Set the radius for each corner (bottom-left and bottom-right)
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
        return gradientDrawable
    }

    fun  lastRowRightBottom(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 30f, 30f, 0f, 0f)
        return drawable
    }

    fun  lastRowLeftBottom(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 30f, 30f)
        return drawable
    }


   //Mark: Select row
    fun highlightRow(selectedRow: TableRow, rowIndex: Int, tableLayout: TableLayout, moreLayout: LinearLayout, imageView: ImageView, Text_at_1: LinearLayout) {
        if (highlightedRowIndex != rowIndex) {
            if (highlightedRowIndex >= 0 && highlightedRowIndex < tableLayout.childCount) {
                moreLayout.visibility = View.GONE
                val previousRow = tableLayout.getChildAt(highlightedRowIndex) as TableRow
                previousRow.background = getRowBackground()
            }
            if (rowIndex >= 0 && rowIndex < tableLayout.childCount) {
                moreLayout.visibility = View.VISIBLE
                val clickedRow = tableLayout.getChildAt(rowIndex) as TableRow
                clickedRow.background = getNewRowBackground()
                highlightedRowIndex = rowIndex
            }else{
                moreLayout.visibility = View.GONE
                val previousRow = tableLayout.getChildAt(highlightedRowIndex) as TableRow
                System.out.println(" previousRow "+ previousRow.getVirtualChildAt(highlightedRowIndex))
                previousRow.background = getRowBackground()
                highlightedRowIndex = 0
            }
        }
    }

    // Mark:- Action for more button to open DropDown
    fun moreOption(context: Context, popShow: Boolean): LinearLayout{
        val linearLayout = createLinearLayout()
        linearLayout.background = linearLayoutBackground()
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            dpToPx(230),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.setPadding(10, 10, 10, 10)

        val textView1 = createTextView("Insert Below", "#121417")
        val view1 = createSeparatorView("#E9EAEF")

        val textView2 = createTextView("Duplicate", "#121417")
        val view2 = createSeparatorView("#E9EAEF")

        val textView3 = createTextView("Move Up", "#121417")
        val view3 = createSeparatorView("#E9EAEF")

        val textView4 = createTextView("Move Down", "#121417")
        val view4 = createSeparatorView("#E9EAEF")

        val textView5 = createTextView("Delete", "#FB4534")

        textView1.setOnClickListener{
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
            insertBelowRow()
        }

        textView2.setOnClickListener{
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
            val dataItemDuplicate = dataArray[highlightedRowIndex-1]
            duplicateRow(tableLayout, dataArray, dataItemDuplicate)
        }
        textView3.setOnClickListener{
            moveUpRow()
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
        }
        textView4.setOnClickListener{
            moveDownRow()
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
        }
        textView5.setOnClickListener{
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
            val dataItemToDelete = dataArray[highlightedRowIndex-1]
            deleteRow(tableLayout, dataArray, dataItemToDelete)
            updateTableLayout(tableLayout, dataArray)
        }
        linearLayout.addView(textView1)
        linearLayout.addView(view1)
        linearLayout.addView(textView2)
        linearLayout.addView(view2)
        linearLayout.addView(textView3)
        linearLayout.addView(view3)
        linearLayout.addView(textView4)
        linearLayout.addView(view4)
        linearLayout.addView(textView5)
        return linearLayout
    }

    //Mark: Navigation Left Move Tapped
    private fun moveCursorLeft() {
        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex > 0) {
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus()
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex ) as TableRow
            val cellAbove = rowAbove.getChildAt(2)
            cellAbove.background = getSelectedCellBackground()
            cellAbove.requestFocus()
        }
    }

    //Mark: Navigation Right Move Tapped
    private fun moveCursorRight() {
        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex > 0) {
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus()
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex ) as TableRow
            val cellAbove = rowAbove.getChildAt(4)
            cellAbove.background = getSelectedCellBackground()
            cellAbove.requestFocus()
        }
    }

    //Mark: Navigation Top Move Tapped
    private fun moveCursorTop(editText: EditText, tableRow: TableRow, tableLayout: TableLayout, selectedCell: LinearLayout) {
        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex > 1) {
            // Clear the background and cursor position from the currently selected cell
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus() // Clear the cursor position

            // Get the row above the currently selected cell
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex - 1) as TableRow

            // Get the cell in the row above at the same column index
            val cellAbove = rowAbove.getChildAt(selectedRowCell)

            // Set the background for the selected cell in the row above
            cellAbove.background = getSelectedCellBackground()

            // Set the cursor position in the selected cell in the row above
            cellAbove.requestFocus()
        }
    }

    //Mark: Navigation Down Move Tapped
    private fun moveCursorBottom() {
        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex >= 1 && selectedCellRowIndex < dataArray.size-1) {
            // Clear the background and cursor position from the currently selected cell
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus() // Clear the cursor position

            // Get the row above the currently selected cell
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex + 1) as TableRow

            // Get the cell in the row above at the same column index
            val cellAbove = rowAbove.getChildAt(selectedRowCell)

            // Set the background for the selected cell in the row above
            cellAbove.background = getSelectedCellBackground()

            // Set the cursor position in the selected cell in the row above
            cellAbove.requestFocus()
        }
    }


    //Mark: Create Custom Buttons above Default Keyboard
    fun createKeyboardKey(editText: EditText, tableRow: TableRow, tableLayout: TableLayout, linearLayout: LinearLayout):LinearLayout{
        val linearLayout =  LinearLayout(context)
        linearLayout.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        linearLayout.setBackgroundColor(Color.TRANSPARENT)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.END

        val leftKeyLayout = LinearLayout(context)
        leftKeyLayout.background = buttonBackground()
        leftKeyLayout.layoutParams = LayoutParams(dpToPx(50), dpToPx(40))
        leftKeyLayout.gravity = Gravity.CENTER

        val leftKey = ImageView(context)
        val leftKeyLayoutParams = LayoutParams(dpToPx(20), dpToPx(20))
        leftKey.layoutParams = leftKeyLayoutParams
        leftKeyLayoutParams.setMargins(0,0,0,0)
        leftKey.setImageResource(R.drawable.left_key)
        leftKeyLayout.addView(leftKey)

        val rightKeyLayout = LinearLayout(context)
        rightKeyLayout.background = buttonBackground()
        val layoutParams2 = LayoutParams(dpToPx(50), dpToPx(40))
        layoutParams2.setMargins(dpToPx(10),0,0,0)
        rightKeyLayout.layoutParams = layoutParams2
        layoutParams2.setMargins(dpToPx(0),0,dpToPx(10),0)
        rightKeyLayout.gravity = Gravity.CENTER

        val rightKey = ImageView(context)
        val rightKeyLayoutParams = LayoutParams(dpToPx(20), dpToPx(20))
        rightKey.layoutParams = rightKeyLayoutParams
        rightKey.setImageResource(R.drawable.right_key)
        rightKeyLayout.addView(rightKey)

        val topKeyLayout = LinearLayout(context)
        topKeyLayout.background = buttonBackground()
        val layoutParams3 = LayoutParams(dpToPx(50), dpToPx(40))
        layoutParams3.setMargins(dpToPx(10),0,0,0)
        topKeyLayout.layoutParams = layoutParams3
        topKeyLayout.gravity = Gravity.CENTER

        val topKey = ImageView(context)
        val topKeyLayoutParams = LayoutParams(dpToPx(20), dpToPx(20))
        topKey.layoutParams = topKeyLayoutParams
        topKey.setImageResource(R.drawable.up_key)
        topKeyLayout.addView(topKey)

        val downKeyLayout = LinearLayout(context)
        downKeyLayout.background = buttonBackground()
        val layoutParams4 = LayoutParams(dpToPx(50), dpToPx(40))
        layoutParams4.setMargins(dpToPx(10),0,dpToPx(10),0)
        downKeyLayout.layoutParams = layoutParams4
        downKeyLayout.gravity = Gravity.CENTER

        val downKey = ImageView(context)
        val downKeyLayoutParams = LayoutParams(dpToPx(20), dpToPx(20))
        downKey.layoutParams = downKeyLayoutParams
        downKey.setImageResource(R.drawable.down_key)
        downKeyLayout.addView(downKey)

        leftKeyLayout.setOnClickListener {
            moveCursorLeft()
        }

        rightKeyLayout.setOnClickListener {
            moveCursorRight()
        }

        topKeyLayout.setOnClickListener {
            moveCursorTop(editText, tableRow, tableLayout, linearLayout)
        }

        downKeyLayout.setOnClickListener {
            moveCursorBottom()
        }

        linearLayout.addView(leftKeyLayout)
        linearLayout.addView(topKeyLayout)
        linearLayout.addView(downKeyLayout)
        linearLayout.addView(rightKeyLayout)
        return linearLayout
    }

    fun showKeyboardButtons(view: View, context: Context, tableRow: TableRow, tableLayout: TableLayout, linearLayout: LinearLayout) {
        val customView = createKeyboardKey(view as EditText, tableRow, tableLayout, linearLayout)
        popupWindowKeyBoard = PopupWindow(
            customView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            false
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindowKeyBoard!!.elevation = 10f
        }
        customView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val customViewWidth = customView.measuredWidth
        val customViewHeight = customView.measuredHeight

        val rootView = view.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val visibleHeight = rect.height() + 50
                val keyboardHeight = screenHeight - visibleHeight

                if (keyboardHeight > screenHeight / 4) { // You can adjust this threshold as needed
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true
                        val yPos = screenHeight  - keyboardHeight - customViewHeight
                        popupWindowKeyBoard.showAtLocation(rootView, Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, yPos)

                    }
                } else {
                    if(isKeyboardShowing){
                        isKeyboardShowing= false
                        popupWindowKeyBoard.dismiss()
                    }
                }
            }
        })
    }

    //Mark: Hide Keyboard
    fun hideKeyBoard(view: View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        view.clearFocus()
        popupWindowKeyBoard.dismiss()
    }

    fun showDropdown(view: View, context: Context) {
        val customView = moreOption(context, true)
        popupWindow = PopupWindow(
            customView,
            dpToPx(230),
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow!!.elevation = 10f
        }
        customView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val customViewWidth = customView.measuredWidth
        val xOffset = -(customViewWidth + 20)
        popupWindow!!.showAsDropDown(view, xOffset, 0)
    }

    private fun createTextView(text: String, textColor: String): TextView {
        val textView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.text = text
        textView.setTextColor(Color.parseColor(textColor))
        textView.textSize = 14f
        textView.setPadding(15, 15, 15, 15)
        textView.layoutParams = layoutParams
        return textView
    }

    private fun createSeparatorView(backgroundColor: String): View {
        val separatorView = View(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1
        )
        separatorView.setBackgroundColor(Color.parseColor(backgroundColor))
        separatorView.layoutParams = layoutParams
        return separatorView
    }

    private fun createLinearLayout(): LinearLayout {
        return LinearLayout(context)
    }

    fun linearLayoutBackground():GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadius = 30f
        drawable.setStroke(3, Color.parseColor("#F9F9FA"))
        drawable.setColor(Color.WHITE)
        return drawable
    }

    //Mark: Function for Delete Row
    fun deleteRow(tableLayout: TableLayout, dataArray: MutableList<DataItem>, dataItem: DataItem){
        val rowIndexToDelete = dataArray.indexOf(dataItem)
        if (rowIndexToDelete != -1) {
            dataArray.removeAt(rowIndexToDelete)
            updateTableLayout(tableLayout, dataArray)
            highlightedRowIndex = 0
        } else {
            updateTableLayout(tableLayout, dataArray)
        }
    }

    //Mark: Function for Insert Row below to the selected row
    fun insertBelowRow(){
        val newDataItem = DataItem("New Text", R.drawable.img_box)
        val insertPosition = highlightedRowIndex
        dataArray.add(insertPosition, newDataItem)
        updateTableLayout(tableLayout, dataArray)
        highlightedRowIndex = 0
    }

    //Mark: Function for move row up
    fun moveUpRow(){
        if (highlightedRowIndex > 0 && highlightedRowIndex < dataArray.size ) {
            moveRowUp(dataArray, highlightedRowIndex-1)
            updateTableLayout(tableLayout, dataArray)
            highlightedRowIndex = 0
        }
    }
    fun moveRowUp(dataArray: MutableList<DataItem>, selectedIndex: Int) {
        if (selectedIndex > 0 && selectedIndex < dataArray.size) {
            val selectedRow = dataArray[selectedIndex]
            dataArray.removeAt(selectedIndex)
            dataArray.add(selectedIndex - 1, selectedRow)
        } else { }
    }

    //Mark: Function for move row down
    fun moveDownRow(){
        if ( highlightedRowIndex < dataArray.size - 1) {
            swapData(dataArray, highlightedRowIndex, highlightedRowIndex-1)
            updateTableLayout(tableLayout, dataArray)
            highlightedRowIndex = 0
        }
    }

    //Mark: Function for duplicate the row
    fun duplicateRow(tableLayout: TableLayout, dataArray: MutableList<DataItem>, dataItem: DataItem){
        val rowIndexToDelete = dataArray.indexOf(dataItem)
        if (rowIndexToDelete != -1) {
            dataArray.add(rowIndexToDelete, dataItem)
            updateTableLayout(tableLayout, dataArray)
            highlightedRowIndex = 0
        } else {
            updateTableLayout(tableLayout, dataArray)
        }
    }

    //Mark:- Keyboard Delegate function
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getWindowToken(), 0)
        return super.dispatchTouchEvent(ev)
    }

    //Mark:- function to update table after navigation performed
    fun updateTableLayout(tableLayout: TableLayout, dataArray: List<DataItem>) {
        tableLayout.removeAllViews()
        headRow.setBackgroundColor(Color.parseColor("#F3F4F8"))
        tableLayout.addView(headRow)
        dataArray.forEachIndexed { index, dataRow ->
            val newIndex = index+1
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.background = getRowBackground()

            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = TableRow.LayoutParams(width50dp, TableRow.LayoutParams.MATCH_PARENT)
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout.background = getCellBackground()

            val imageView = ImageView(context)
            val imageViewLayoutParams = TableRow.LayoutParams(15.dp, 15.dp)
            imageView.layoutParams = imageViewLayoutParams
            imageViewLayoutParams.setMargins(0, 10.dp, 0,0)
            imageView.setImageResource(R.drawable.unckeck_circle_img)
            linearLayout.addView(imageView)
            tableRow.addView(linearLayout)

            val hashLinearLayout = LinearLayout(context)
            hashLinearLayout.layoutParams = TableRow.LayoutParams(width50dp, TableRow.LayoutParams.MATCH_PARENT)
            hashLinearLayout.gravity = Gravity.CENTER_HORIZONTAL
            hashLinearLayout.background = getCellBackground()

            val hash_Id = TextView(context)
            val hashIdLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
            hashIdLayoutParams.setMargins(0, 10.dp, 0,0)
            hash_Id.layoutParams = hashIdLayoutParams
            hash_Id.setTextColor(Color.parseColor(textColor))
            hash_Id.setText(""+newIndex)
            hashLinearLayout.addView(hash_Id)
            tableRow.addView(hashLinearLayout)

            val Text_at_1 = LinearLayout(context)
            Text_at_1.background = getCellBackground()
            Text_at_1.gravity = Gravity.START
            Text_at_1.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            val TextAtEditText = EditText(context)

            val textat_1 = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            TextAtEditText.background =  editTextBackground()
            TextAtEditText.layoutParams = textat_1
            TextAtEditText.setPadding(10.dp, 10.dp,10.dp,10.dp)
            TextAtEditText.textSize = 12f
            TextAtEditText.setTextColor(Color.parseColor(textColor))
            TextAtEditText.setText(dataRow.text as String)
            TextAtEditText.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    highlightCell(Text_at_1, tableRow, tableLayout, newIndex)
                    showKeyboardButtons(TextAtEditText, context, tableRow, tableLayout, Text_at_1)
                }else{
                    hideKeyBoard(TextAtEditText)
                    TextAtEditText.clearFocus()
                }
            }

            Text_at_1.addView(TextAtEditText)
            tableRow.addView(Text_at_1)

            val linearLayout2 = LinearLayout(context)
            linearLayout2.background = getCellBackground()
            linearLayout2.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout2.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            linearLayout2.orientation = LinearLayout.HORIZONTAL

            val imageView2 = ImageView(context)
            val imaageView2LayoutParams = LinearLayout.LayoutParams(25.dp, 25.dp)
            imageView2.layoutParams = imaageView2LayoutParams
            imaageView2LayoutParams.setMargins(0,10.dp, 0,0)
            imageView2.setImageResource(dataRow.imageResource as Int)

            val textView3 = TextView(context)
            val textView3LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )
            textView3.layoutParams = textView3LayoutParams
            textView3.textSize =12f
            textView3LayoutParams.setMargins(1.dp, 18.dp, 0,0)
            textView3.gravity = Gravity.BOTTOM
            textView3.setTextColor(Color.parseColor("#6B6C7C"))
            textView3.text = "+4"
            if (index !=0 && index !=1){
                textView3.visibility = View.INVISIBLE
            }

            linearLayout2.addView(imageView2)
            linearLayout2.addView(textView3)
            tableRow.addView(linearLayout2)

            val Text_at_2 = LinearLayout(context)
            Text_at_2.background = getCellBackground()
            Text_at_2.gravity = Gravity.CENTER_HORIZONTAL
            Text_at_2.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            Text_at_2.orientation = LinearLayout.HORIZONTAL

            val TextAtEditText_2 = EditText(context)
            val textat_2 = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            TextAtEditText_2.background = editTextBackground()

            TextAtEditText_2.layoutParams = textat_2
            TextAtEditText_2.textSize = 12f
            TextAtEditText_2.setTextColor(Color.parseColor(textColor))
            TextAtEditText_2.setText("Collum = "+ newIndex)
            TextAtEditText_2.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    highlightCell(Text_at_2, tableRow, tableLayout, newIndex)
                    showKeyboardButtons(TextAtEditText, context, tableRow, tableLayout, Text_at_2)
                }else{
                    hideKeyBoard(TextAtEditText_2)
                    TextAtEditText_2.clearFocus()
                }
            }
            Text_at_2.addView(TextAtEditText_2)
            tableRow.addView(Text_at_2)

            val linearLayout3 = LinearLayout(context)
            linearLayout3.background = getCellBackground()
            linearLayout3.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout3.layoutParams = TableRow.LayoutParams(width133dp, TableRow.LayoutParams.MATCH_PARENT)
            linearLayout3.orientation = LinearLayout.HORIZONTAL

            val textView6 = TextView(context)
            val textView6LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )
            textView6.layoutParams = textView6LayoutParams
            textView6LayoutParams.setMargins(0,10.dp, 0,0)
            textView6.setTextColor(Color.parseColor(textColor))
            textView6.text = "Text Feild"

            val imageView3 = ImageView(context)
            val imageView3LayoutParams = TableRow.LayoutParams(20.dp, 20.dp)
            imageView3.layoutParams = imageView3LayoutParams
            imageView3LayoutParams.setMargins(0,10.dp,0,0)
            imageView3.setImageResource(R.drawable.ic_baseline_down)
            imageView3.setPadding(5.dp, 0, 0, 0)

            linearLayout3.setOnClickListener {
                val multiDropDown = MultiDropDown(context)
                multiDropDown.createLinearLayout(context)
            }


            if (index == dataArray.size - 1) {
                // If it's the last row, set the custom background with rounded corners
                tableRow.background = createRoundedBackgroundForLastRow(30f)
                linearLayout.background = lastRowLeftBottom()
                linearLayout3.background = lastRowRightBottom()
            } else {
                // If it's not the last row, set the default background
                tableRow.background = getRowBackground()
            }


            linearLayout3.addView(textView6)
            linearLayout3.addView(imageView3)
            tableRow.addView(linearLayout3)
            tableLayout.addView(tableRow)

            linearLayout.setOnClickListener {
                TextAtEditText.background = getCellBackground()
                highlightRow(tableRow, newIndex, tableLayout, moreLayout, imageView, Text_at_1)
                onRowClicked(tableRow, imageView, Text_at_1)
                linearLayout.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                linearLayout2.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                linearLayout3.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                Text_at_1.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                Text_at_2.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
                hashLinearLayout.background = createCellDrawableWithTopBottomStrokes(Color.parseColor("#E6E7EA"),2)
            }
        }
    }
}

fun swapData(dataArray: MutableList<DataItem>, position1: Int, position2: Int) {
    if (position1 in 0 until dataArray.size && position2 in 0 until dataArray.size) {
        Collections.swap(dataArray, position1, position2)
    } else {
        Collections.swap(dataArray, position1, position2)
    }
}

data class DataItem(val text: String, val imageResource: Int)
