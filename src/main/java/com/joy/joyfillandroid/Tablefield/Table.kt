package com.joy.joyfillandroid.Tablefield

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.joy.joyfillandroid.*
import org.json.JSONArray
import java.util.*

@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.P)
class Table @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    val textColor ="#121417"
    val headRowBackgroundColor = "#F3F4F8"
    var highlightedRowIndex = -1
    var selectedRowCell= 0
    var selectedCellRowIndexOf= 0
    private var selectedCell: LinearLayout? = null
    val addRowLayout = LinearLayout(context)
    val moreLayout = LinearLayout(context)
    val closeImage = ImageView(context)
    var addNewIndex: Int = 0
    val tableLayout = TableLayout(context)
    private var selectedRow: TableRow? = null

    val recyclerView = RecyclerView(context)
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: DropDownAdapter
    var list= ArrayList<RecyclerModel>()

    val headRow = TableRow(context)
    private var selectedRowIndex: Int = -1

    private var popupWindow: PopupWindow? = null
    private lateinit var popupWindowKeyBoard: PopupWindow
    private var isKeyboardShowing = false
    var rowIndex: Int? = 0
    var columnIndex: Int? = 0
    object globalVariable {
        var text: String? = ""
        var title: String? = ""
        var getColumnTitle = mutableListOf<String>()
        var getColumnType = mutableListOf<String>()
        var tableColumnValue = mutableListOf<String>()
        var rowArray = mutableListOf<RowsModel>()
        var tableColumnOrderArrayID = mutableListOf<String>()
        var tableColumnsArray : JSONArray = JSONArray()
    }

    val rowsArray = globalVariable.rowArray


    init {
        removeDummyRow()
        orientation = VERTICAL
        val relativeLayout = RelativeLayout(context)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0,dpToPx(context,10),0, dpToPx(context, 10))
        }

        val tableTitle = TextView(context)
        tableTitle.layoutParams = RelativeLayout.LayoutParams(
            dpToPx(context, 150),
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0,dpToPx(context,10),0,0)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        }
        tableTitle.ellipsize = TextUtils.TruncateAt.END
        tableTitle.setSingleLine(true)
        tableTitle.text = globalVariable.title
        tableTitle.setTextColor(Color.parseColor("#3F404D"))
        tableTitle.textSize = 14f
        tableTitle.gravity = RelativeLayout.CENTER_VERTICAL
        tableTitle.typeface = customTypeFace(700)

        moreLayout.visibility = View.GONE
        moreLayout.setPadding(40,15,30,15)
        moreLayout.background = addRowButtonBackground()
        moreLayout.layoutParams = RelativeLayout.LayoutParams(
            dpToPx(context, 78),
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            setMargins(0, 0, dpToPx(context,140), 0)
        }
        moreLayout.orientation = LinearLayout.HORIZONTAL
        moreLayout.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_END

        moreLayout.setOnClickListener {
            moreButtonOptions(moreLayout, context)
        }

        val moreText = TextView(context)
        moreText.text = "More"
        moreText.typeface = customTypeFace(400)
        moreText.setTextColor(Color.parseColor(textColor))
        moreText.textSize = 14f

        val downArrowImage = ImageView(context)
        downArrowImage.setImageResource(R.drawable.ic_expand_down)
        downArrowImage.layoutParams = LinearLayout.LayoutParams(
            dpToPx(context,16),
            dpToPx(context,16),
            1f
        ).apply {
            setMargins(dpToPx(context,5), dpToPx(context,4), 0, 0)
        }

        addRowLayout.setPadding(40,15,30,15)
        addRowLayout.background = addRowButtonBackground()
        addRowLayout.layoutParams = RelativeLayout.LayoutParams(
            dpToPx(context, 100),
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            setMargins(0, 0, dpToPx(context,35), 0)
        }
        addRowLayout.orientation = LinearLayout.HORIZONTAL
        addRowLayout.gravity = LinearLayout.TEXT_ALIGNMENT_CENTER

        val addRowText = TextView(context)
        addRowText.text = "Add Row"
        addRowText.typeface = customTypeFace(400)
        addRowText.setTextColor(Color.parseColor(textColor))
        addRowText.textSize = 14f

        val plusImage = ImageView(context)
        plusImage.setImageResource(R.drawable.ic_add_round)
        plusImage.layoutParams = LinearLayout.LayoutParams(
            dpToPx(context,20),
            dpToPx(context,20),
            1f
        ).apply {
            setMargins(dpToPx(context,5), dpToPx(context,0), 0, dpToPx(context,0))
        }

        closeImage.setImageResource(R.drawable.ic_close_ring)
        closeImage.layoutParams = RelativeLayout.LayoutParams(
            dpToPx(context,24),
            dpToPx(context,24)
        ).apply {
            setMargins(dpToPx(context,1), dpToPx(context,2), dpToPx(context,2), 0)
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        }
        //Mark:- Function for close table
        closeImage.setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }
        moreLayout.addView(moreText)
        moreLayout.addView(downArrowImage)
        addRowLayout.addView(addRowText)
        addRowLayout.addView(plusImage)

        relativeLayout.addView(tableTitle)
        relativeLayout.addView(moreLayout)
        relativeLayout.addView(addRowLayout)
        relativeLayout.addView(closeImage)
        addView(relativeLayout)

        val rootView = LinearLayout(context)
        rootView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        rootView.orientation = LinearLayout.VERTICAL
        tableLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        headRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headRow.setBackgroundColor(Color.parseColor(headRowBackgroundColor))
        setHeaderBackground(headRow)

        val selectionButtonCell = TextView(context)
        selectionButtonCell.background = setTableTopLeftCornerRadius()
        selectionButtonCell.layoutParams = TableRow.LayoutParams(
            dpToPx(context,50), TableRow.LayoutParams.MATCH_PARENT
        )
        selectionButtonCell.setPadding(dpToPx(context,5), dpToPx(context,0), dpToPx(context,5),dpToPx(context,5))
        selectionButtonCell.gravity = android.view.Gravity.CENTER
        headRow.addView(selectionButtonCell)

        val numberCell = TextView(context)
        numberCell.background = getCellBackground()
        numberCell.layoutParams = TableRow.LayoutParams(
            dpToPx(context,50), TableRow.LayoutParams.MATCH_PARENT
        )
        numberCell.gravity = android.view.Gravity.CENTER
        numberCell.text ="#"
        numberCell.setTextColor(Color.parseColor(textColor))
        headRow.addView(numberCell)

        tableViewHeader(tableLayout, globalVariable.getColumnTitle, headRow)

        val horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.viewTreeObserver.addOnScrollChangedListener(
            ViewTreeObserver.OnScrollChangedListener {
                val scrollX = horizontalScrollView.scrollX
                horizontalScrollView.scrollTo(scrollX, 0)
            }
        )

        val scrollView = ScrollView(context)
        scrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        scrollView.isVerticalScrollBarEnabled = false
        scrollView.isHorizontalScrollBarEnabled = false
        rowsArray.forEachIndexed { index, dataRow ->
                addNewIndex = index + 1
                val newIndex = index + 1
                val tableRow = TableRow(context)

                tableRow.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                tableRow.background = setRowBackground()

                val linearLayout = LinearLayout(context)
                linearLayout.layoutParams = TableRow.LayoutParams(dpToPx(context,50), TableRow.LayoutParams.MATCH_PARENT)
                linearLayout.gravity = Gravity.CENTER_HORIZONTAL
                linearLayout.background = getCellBackground()
                val imageView = ImageView(context)
                val imageViewLayoutParams =
                    TableRow.LayoutParams(dpToPx(context, 15), dpToPx(context, 15))
                imageView.layoutParams = imageViewLayoutParams
                imageViewLayoutParams.setMargins(0, dpToPx(context, 10), 0, 0)
                imageView.setImageResource(R.drawable.unchecked_icon)
                linearLayout.addView(imageView)
                tableRow.addView(linearLayout)

                val hashLinearLayout = LinearLayout(context)
                hashLinearLayout.layoutParams =
                    TableRow.LayoutParams(dpToPx(context,50), TableRow.LayoutParams.MATCH_PARENT)
                hashLinearLayout.gravity = Gravity.CENTER_HORIZONTAL
                hashLinearLayout.background = getCellBackground()

                val hash_Id = TextView(context)
                val hashIdLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT
                )
                hashIdLayoutParams.setMargins(0, dpToPx(context, 10), 0, 0)
                hash_Id.layoutParams = hashIdLayoutParams
                hash_Id.setTextColor(Color.parseColor(textColor))
                hash_Id.setText("" + newIndex)
                hashLinearLayout.addView(hash_Id)
                tableRow.addView(hashLinearLayout)

                insertCellsValues(index, tableRow, newIndex)

                addRowLayout.setOnClickListener {
                    addRowButton(scrollView)
                }

                if (index == rowsArray.size - 1 ) {
                    tableRow.background = cornerRadiusForLastRow(30f)
                    linearLayout.background = lastRowLeftBottomCornerRadius()
                } else {
                    tableRow.background = setRowBackground()
                }

                tableLayout.addView(tableRow)

                linearLayout.setOnClickListener {
                selectedRow(tableRow, newIndex, tableLayout, moreLayout, imageView)
                onRowSelectionButtonClick(tableRow, imageView)
                setHeaderBackground(headRow)
                }
        }
        horizontalScrollView.addView(tableLayout)
        scrollView.addView(horizontalScrollView)
        rootView.addView(scrollView)
        addView(rootView)
    }

    //Mark: Function for Remove Dummy Row
    private fun removeDummyRow() {
        val idsToRemove = setOf("newRowId")
        rowsArray.removeIf { it.id in idsToRemove }
    }

    //Mark: Create rows and update data into cells of rows
    fun insertCellsValues(index: Int, tableRow: TableRow, newIndex: Int) {
        for (i in globalVariable.getColumnType.indices) {
            val type = globalVariable.getColumnType[i]
            val columnOrderIndex = i
            val rowOrderIndex = index
            val columnId = globalVariable.tableColumnOrderArrayID[columnOrderIndex]
            when (type) {
                "text" -> {
                    val textColumnLayout = LinearLayout(context)
                    textColumnLayout.background = getCellBackground()
                    textColumnLayout.gravity = Gravity.START
                    textColumnLayout.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                    )
                    val textColumnView = EditText(context)
                    val textColumnViewLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
                    textColumnView.background = editTextBackground()
                    textColumnView.layoutParams = textColumnViewLayoutParams
                    textColumnView.setPadding(
                        dpToPx(context, 10),
                        dpToPx(context, 10),
                        dpToPx(context, 10),
                        dpToPx(context, 10)
                    )
                    textColumnView.textSize = 12f
                    textColumnView.gravity = Gravity.START
                    textColumnView.setTextColor(Color.parseColor(textColor))
                    textColumnView.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            selectedCell(textColumnLayout, tableRow, tableLayout, newIndex)
                        }
                    }

                    if (index == 2  && columnOrderIndex == globalVariable.getColumnType.size - 1) {
                        textColumnLayout.background = lastRowRightBottomCornerRadius()
                    }
                    val cellValues = globalVariable.rowArray[rowOrderIndex].cells
                    val value =cellValues[columnOrderIndex]
                    textColumnView.setText(""+value)
                    textColumnLayout.addView(textColumnView)
                    tableRow.addView(textColumnLayout)
                }
                "dropdown" -> {
                    var options: JSONArray? = null
                    val dropDownColumnLayout = LinearLayout(context)
                    val dropDownText = TextView(context)
                    dropDownColumnLayout.background = getCellBackground()
                    dropDownColumnLayout.gravity = Gravity.CENTER_HORIZONTAL
                    dropDownColumnLayout.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                    )
                    dropDownColumnLayout.orientation = LinearLayout.HORIZONTAL
                    val dropDownTextLayoutParams = TableRow.LayoutParams(
                        dpToPx(context, 90),
                        TableRow.LayoutParams.WRAP_CONTENT,
                    )

                    dropDownText.layoutParams = dropDownTextLayoutParams
                    dropDownTextLayoutParams.setMargins(0, dpToPx(context, 10), 0, 0)
                    dropDownText.setTextColor(Color.parseColor(textColor))
                    val cellValues = globalVariable.rowArray[rowOrderIndex].cells

                    for (i in 0 until globalVariable.tableColumnsArray.length()) {
                        val option = globalVariable.tableColumnsArray.getJSONObject(i)
                        if (option.getString("_id") == columnId) {
                            options = option.getJSONArray("options")
                        }
                    }
                    val value = cellValues[columnOrderIndex]
                    var displayValue: String?= ""
                    for (i in 0 until options!!.length()) {
                        val option = options.getJSONObject(i)
                        if (option.getString("_id") == value) {
                            displayValue = option.getString("value")
                        }
                    }
                    dropDownText.setText(""+displayValue)
                    dropDownText.textSize = 12f
                    dropDownText.typeface = Typeface.create(null, 400, false)
                    dropDownText.textAlignment = TEXT_ALIGNMENT_CENTER
                    val downArrow = ImageView(context)
                    val downArrowLayoutParams =
                        TableRow.LayoutParams(dpToPx(context, 20), dpToPx(context, 20))
                    downArrow.layoutParams = downArrowLayoutParams
                    downArrowLayoutParams.setMargins(0, dpToPx(context, 10), 0, 0)
                    downArrow.setImageResource(R.drawable.ic_expand_down)
                    downArrow.setPadding(dpToPx(context, 5), 0, 0, 0)

                    if (index == 2 && columnOrderIndex == Table.globalVariable.getColumnType.size - 1) {
                        dropDownColumnLayout.background = lastRowRightBottomCornerRadius()
                    }

                    dropDownColumnLayout.setOnClickListener {
                        var indexValue: Pair<Int, Int>? = null
                        indexValue = findColumnIndex(tableLayout, dropDownColumnLayout)
                        rowIndex = indexValue?.first
                        columnIndex = indexValue?.second

                        if (rowIndex != null && columnIndex != null) {
                            dropDownOptionsBottomSheet(context, options)
                        }
                    }

                    dropDownColumnLayout.addView(dropDownText)
                    dropDownColumnLayout.addView(downArrow)
                    tableRow.addView(dropDownColumnLayout)
                }
            }
        }
    }

    //Mark:- Function for Add New Row and perform on Add Row Button
    private fun addRowButton(scrollView: ScrollView) {
        val newRow = RowsModel(id = "ID", deleted = false, cells = globalVariable.tableColumnValue)
        rowsArray.add(newRow)
        updateTableLayout(tableLayout, rowsArray)
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    //Mark:- Mark:- Function for finding row index and column index of the table
    fun findColumnIndex(tableLayout: TableLayout, cell: View): Pair<Int, Int>? {
        val rowCount = tableLayout.childCount
        for (rowIndex in 0 until rowCount) {
            val row = tableLayout.getChildAt(rowIndex) as TableRow
            val cellCount = row.childCount
            for (columnIndex in 0 until cellCount) {
                val currentCell = row.getChildAt(columnIndex)
                if (currentCell === cell) {
                    return Pair(rowIndex, columnIndex)
                }
            }
        }
        return null
    }

    //Mark:- Function for update dropDown Text value
        fun updateDropDownCellText(tableLayout: TableLayout, rowIndex: Int, columnIndex: Int) {
        val row = tableLayout.getChildAt(rowIndex) as TableRow
        if (columnIndex >= 0 && columnIndex < row.childCount) {
            val linearLayout = row.getChildAt(columnIndex) as LinearLayout
            if (linearLayout.childCount > 0) {
                val cell = linearLayout.getChildAt(0) as TextView
                if (cell != null){
                    cell.text = globalVariable.text
                }
            }
        }
    }


    //Mark: Function for showing Bottom Sheet with multiple options
    fun dropDownOptionsBottomSheet(context: Context, option: JSONArray?): LinearLayout {

        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linearLayout.background = context.getDrawable(R.drawable.bottom_sheet_background)
        }

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.END
        layoutParams.topMargin = dpToPx(context, 15)
        layoutParams.rightMargin = dpToPx(context, 24)
        layoutParams.bottomMargin = dpToPx(context, 12)

        val recyclerViewParent = recyclerView.parent as? ViewGroup
        recyclerViewParent?.removeView(recyclerView)

        val recyclerViewLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(context, 200))
        recyclerView.layoutParams = recyclerViewLayoutParams
        recyclerViewLayoutParams.setMargins(0, dpToPx(context,20),0, dpToPx(context, 10))

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        recyclerView.hasFixedSize()
        list = ArrayList()
        for (i in 0 until option!!.length()) {
            val item = option.getJSONObject(i)
            val checkBoxValue = item.optString("value", "")

            list.add(RecyclerModel(checkBoxValue))
        }
        adapter = DropDownAdapter(list, context)
        recyclerView.adapter= adapter

        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
        bottomSheetDialog.setContentView(linearLayout)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListenr(context, recyclerView, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val newText = list.get(position).checkBox
                adapter.notifyDataSetChanged()
                globalVariable.text = newText
                updateDropDownCellText(tableLayout, rowIndex!!, columnIndex!! )
                bottomSheetDialog.dismiss()
            }
        }))

        linearLayout.addView(recyclerView)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
        return linearLayout
    }


    private fun onRowSelectionButtonClick(row: TableRow, imageView: ImageView) {
        if (row == selectedRow) {
            selectedRow = null
            imageView.setImageResource(R.drawable.ic_uncheck_circle_img) // Set the default image
        } else {
            selectedRow?.let { previousSelectedRow ->
                val previousSelectedLinearLayout = previousSelectedRow.getChildAt(0) as LinearLayout
                val previousSelectedImageView = previousSelectedLinearLayout.getChildAt(0) as ImageView
                val previousSelectedTextAt1 = previousSelectedRow.getChildAt(selectedRowCell) as LinearLayout
                previousSelectedTextAt1.background = getCellBackground()
                previousSelectedImageView.setImageResource(R.drawable.ic_uncheck_circle_img) // Set the default image for the previously selected row
            }
            selectedRow = row
            imageView.setImageResource(R.drawable.ic_check_circle_img) // Set the selected image
        }
    }

    override fun onDraw(canvas: Canvas?) {
        OnDrawHelper.onDrawGlobal(context, this)
        super.onDraw(canvas)
    }

    private fun selectedCell(selectedCell: View, tableRow: TableRow, tableLayout: TableLayout,rowIndex: Int) {
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
                    cell.background = selectedCellBackground()
                }
                cell.background = selectedCellBackground()
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


    //Mark: Cell Background
    fun getCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun setTableTopLeftCornerRadius(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(30f, 30f, 0f, 0f, 0f, 0f, 0f, 0f)
        drawable.setStroke(2, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun setTableTopRightCornerRadius(): GradientDrawable{
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

    fun setRowBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2 , Color.parseColor("#E2E3E7"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }
    fun selectedRowBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(4 , Color.parseColor("#1F6BFF"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    fun selectedCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2 , Color.parseColor("#1F6BFF"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    fun addRowButtonBackground(): GradientDrawable{
        val buttonDrawable = GradientDrawable()
        buttonDrawable.setColor(Color.WHITE)
        buttonDrawable.shape = GradientDrawable.RECTANGLE
        buttonDrawable.cornerRadius = 15f
        buttonDrawable.setStroke(3, Color.parseColor("#E2E3E7"))
        return buttonDrawable
    }

    private fun setHeaderBackground(view: TableRow) {
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor(headRowBackgroundColor))
        drawable.cornerRadii = floatArrayOf(
            30f, 30f, 30f, 30f,
            0f, 0f, 0f, 0f
        )

        view.background = drawable
    }


    fun cornerRadiusForLastRow(radius: Float): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setStroke(2, Color.parseColor("#E2E3E7"))
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
        return gradientDrawable
    }

    fun  lastRowRightBottomCornerRadius(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 30f, 30f, 0f, 0f)
        return drawable
    }

    fun  lastRowLeftBottomCornerRadius(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 30f, 30f)
        return drawable
    }


   //Mark: Function for Select row
    fun selectedRow(selectedRow: TableRow, rowIndex: Int, tableLayout: TableLayout, moreLayout: LinearLayout, imageView: ImageView) {
       if (highlightedRowIndex != rowIndex) {
           if (highlightedRowIndex >= 0 && highlightedRowIndex < tableLayout.childCount) {
               val previousRow = tableLayout.getChildAt(highlightedRowIndex) as TableRow
               previousRow.background = setRowBackground()
           }

           if (rowIndex >= 0 && rowIndex < tableLayout.childCount) {
               val clickedRow = tableLayout.getChildAt(rowIndex) as TableRow
               clickedRow.background = selectedRowBackground()
               highlightedRowIndex = rowIndex
               moreLayout.visibility = View.VISIBLE
           } else {
               highlightedRowIndex = -1
               moreLayout.visibility = View.GONE
           }
       } else {
           val clickedRow = tableLayout.getChildAt(rowIndex) as TableRow
           clickedRow.background = setRowBackground()
           highlightedRowIndex = -1
           moreLayout.visibility = View.GONE
       }
    }

    //Mark:- Action for more button to open DropDown
    fun dropDownView(context: Context, popUpShow: Boolean): LinearLayout{
        val backgroundColor ="#E9EAEF"
        val deleteTextColor ="#FB4534"
        val linearLayout = createLinearLayout()
        linearLayout.background = linearLayoutBackground()
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            dpToPx(context,230),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.setPadding(10, 10, 10, 10)

        val insertRow = label("Insert Below", textColor)
        val view1 = view(backgroundColor)

        val duplicateRow = label("Duplicate", textColor)
        val view2 = view(backgroundColor)

        val moveRowUP = label("Move Up", textColor)
        val view3 = view(backgroundColor)

        val moveRowDown = label("Move Down", textColor)
        val view4 = view(backgroundColor)

        val deleteRow = label("Delete", deleteTextColor)


        insertRow.setOnClickListener{
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
            val indexToAddBelow = highlightedRowIndex-1
            val newRow = RowsModel(id = "ID", deleted = false, cells = globalVariable.tableColumnValue)
            handleAddBelowRow(indexToAddBelow, newRow)
        }

        duplicateRow.setOnClickListener{
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
            val dataItemDuplicate = highlightedRowIndex-1
            handleDuplicateRow(tableLayout, rowsArray,dataItemDuplicate)
        }
        moveRowUP.setOnClickListener{
            handleMoveRowUp()
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
        }
        moveRowDown.setOnClickListener{
            handleMoveRowDown()
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
        }
        deleteRow.setOnClickListener{
            moreLayout.visibility = View.GONE
            popupWindow!!.dismiss()
            val dataItemToDelete = highlightedRowIndex-1
            handleDeleteRow(tableLayout, rowsArray, dataItemToDelete)
            updateTableLayout(tableLayout, rowsArray)
        }
        linearLayout.addView(insertRow)
        linearLayout.addView(view1)
        linearLayout.addView(duplicateRow)
        linearLayout.addView(view2)
        linearLayout.addView(moveRowUP)
        linearLayout.addView(view3)
        linearLayout.addView(moveRowDown)
        linearLayout.addView(view4)
        linearLayout.addView(deleteRow)
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
            cellAbove.background = selectedCellBackground()
            cellAbove.requestFocus()
        }
    }


    //Mark: Navigation Right Move Tapped
    private fun moveCursorRight(editText: EditText) {

        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex > 0) {
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus()
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex ) as TableRow
            val cellAbove = rowAbove.getChildAt(3)
            cellAbove.background = selectedCellBackground()
            cellAbove.requestFocus()
        }
    }

    fun moveFocusToNextEditText(editText: EditText){
        val nextEditText = findNextEditText(editText)
        nextEditText?.requestFocus()
    }

    private fun findNextEditText(currentEditText: EditText): EditText? {
        val currentRow = currentEditText.parent as TableRow
        val childCount = currentRow.childCount
        var foundCurrent = false

        for (i in 0 until childCount) {
            val cell = currentRow.getChildAt(i)
            if (cell == currentEditText) {
                foundCurrent = true
                continue
            }
            if (foundCurrent && cell is EditText) {
                return cell
            }
        }

        return null
    }

    //Mark: Navigation Top Move Tapped
    private fun moveCursorTop(editText: EditText, tableRow: TableRow, tableLayout: TableLayout, selectedCell: LinearLayout) {
        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex > 1) {
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus()
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex - 1) as TableRow
            val cellAbove = rowAbove.getChildAt(selectedRowCell)
            cellAbove.background = selectedCellBackground()
            moveFocusToNextEditText(editText)
            cellAbove.requestFocus()
        }
    }

    //Mark: Navigation Down Move Tapped
    private fun moveCursorBottom() {
        var selectedCellRowIndex = selectedCellRowIndexOf
        if (selectedCellRowIndex >= 1 && selectedCellRowIndex < rowsArray.size - 1) {
            val currentRow = tableLayout.getChildAt(selectedCellRowIndex) as TableRow
            val currentCell = currentRow.getChildAt(selectedRowCell)
            currentCell.background = getCellBackground()
            currentCell.clearFocus()
            val rowAbove = tableLayout.getChildAt(selectedCellRowIndex + 1) as TableRow
            val cellAbove = rowAbove.getChildAt(selectedRowCell)
            cellAbove.background = selectedCellBackground()
            cellAbove.requestFocus()
        }
    }

    //Mark: Create Custom Buttons above Default Keyboard
    fun navigationKeys(editText: EditText, tableRow: TableRow, tableLayout: TableLayout, linearLayout: LinearLayout):LinearLayout{
        val linearLayout =  LinearLayout(context)
        linearLayout.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        linearLayout.setBackgroundColor(Color.TRANSPARENT)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.END

        val leftKeyLayout = LinearLayout(context)
        leftKeyLayout.background = addRowButtonBackground()
        leftKeyLayout.layoutParams = LayoutParams(dpToPx(context,50), dpToPx(context,40))
        leftKeyLayout.gravity = Gravity.CENTER

        val leftKey = ImageView(context)
        val leftKeyLayoutParams = LayoutParams(dpToPx(context,20), dpToPx(context,20))
        leftKey.layoutParams = leftKeyLayoutParams
        leftKeyLayoutParams.setMargins(0,0,0,0)
        leftKey.setImageResource(R.drawable.ic_left_key)
        leftKeyLayout.addView(leftKey)

        val rightKeyLayout = LinearLayout(context)
        rightKeyLayout.background = addRowButtonBackground()
        val layoutParams2 = LayoutParams(dpToPx(context,50), dpToPx(context,40))
        layoutParams2.setMargins(dpToPx(context,10),0,0,0)
        rightKeyLayout.layoutParams = layoutParams2
        layoutParams2.setMargins(dpToPx(context,0),0,dpToPx(context,10),0)
        rightKeyLayout.gravity = Gravity.CENTER

        val rightKey = ImageView(context)
        val rightKeyLayoutParams = LayoutParams(dpToPx(context,20), dpToPx(context,20))
        rightKey.layoutParams = rightKeyLayoutParams
        rightKey.setImageResource(R.drawable.ic_right_key)
        rightKeyLayout.addView(rightKey)

        val topKeyLayout = LinearLayout(context)
        topKeyLayout.background = addRowButtonBackground()
        val layoutParams3 = LayoutParams(dpToPx(context,50), dpToPx(context,40))
        layoutParams3.setMargins(dpToPx(context,10),0,0,0)
        topKeyLayout.layoutParams = layoutParams3
        topKeyLayout.gravity = Gravity.CENTER

        val topKey = ImageView(context)
        val topKeyLayoutParams = LayoutParams(dpToPx(context,20), dpToPx(context,20))
        topKey.layoutParams = topKeyLayoutParams
        topKey.setImageResource(R.drawable.ic_up_key)
        topKeyLayout.addView(topKey)

        val downKeyLayout = LinearLayout(context)
        downKeyLayout.background = addRowButtonBackground()
        val layoutParams4 = LayoutParams(dpToPx(context,50), dpToPx(context,40))
        layoutParams4.setMargins(dpToPx(context,10),0,dpToPx(context,10),0)
        downKeyLayout.layoutParams = layoutParams4
        downKeyLayout.gravity = Gravity.CENTER

        val downKey = ImageView(context)
        val downKeyLayoutParams = LayoutParams(dpToPx(context,20), dpToPx(context,20))
        downKey.layoutParams = downKeyLayoutParams
        downKey.setImageResource(R.drawable.ic_down_key)
        downKeyLayout.addView(downKey)

        leftKeyLayout.setOnClickListener {
            moveCursorLeft()
        }

        rightKeyLayout.setOnClickListener {
            moveCursorRight(editText)
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

    //Mark:- Function for showing custom keyboard
    fun showNavigationKeys(view: EditText, context: Context, tableRow: TableRow, tableLayout: TableLayout, linearLayout: LinearLayout) {
        val customView = navigationKeys(view as EditText, tableRow, tableLayout, linearLayout)
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

                if (keyboardHeight > screenHeight / 4) {
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
//        popupWindowKeyBoard.dismiss()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        view.clearFocus()
    }

    fun moreButtonOptions(view: View, context: Context) {
        val customView = dropDownView(context, true)
        popupWindow = PopupWindow(
            customView,
            dpToPx(context,230),
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

    private fun label(text: String, textColor: String): TextView {
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

    private fun view(backgroundColor: String): View {
        val separatorView = View(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context,1)
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
    fun handleDeleteRow(tableLayout: TableLayout, rowsArray: MutableList<RowsModel>,indexToRemove: Int) {
        if (indexToRemove >= 0 && indexToRemove < rowsArray.size) {
            rowsArray.removeAt(indexToRemove)
            updateTableLayout(tableLayout, rowsArray)
            highlightedRowIndex = 0
        }
    }

    //Mark: Function for Insert Row below to the selected row
    fun handleAddBelowRow(indexToAddBelow: Int, newRow: RowsModel) {
        if (indexToAddBelow >= 0 && indexToAddBelow < rowsArray.size) {
            rowsArray.add(indexToAddBelow + 1, newRow)
            updateTableLayout(tableLayout, rowsArray)
            highlightedRowIndex = 0
        }
    }

    //Mark: Function for move row up
    fun handleMoveRowUp(){
        if ( highlightedRowIndex <= rowsArray.size) {
            swapRowUp(rowsArray, highlightedRowIndex-1)
            updateTableLayout(tableLayout, rowsArray)
            highlightedRowIndex = 0
        }
    }
    fun swapRowUp(rowsArray: MutableList<RowsModel>, selectedIndex: Int) {
        if (selectedIndex <= rowsArray.size) {
            val selectedRow = rowsArray[selectedIndex]
            rowsArray.removeAt(selectedIndex)
            rowsArray.add(selectedIndex - 1, selectedRow)
        } else { }
    }

    //Mark: Function for move row down
    fun handleMoveRowDown(){
        if ( highlightedRowIndex < rowsArray.size ) {
            swapRowDown(rowsArray, highlightedRowIndex, highlightedRowIndex-1)
            updateTableLayout(tableLayout, rowsArray)
            highlightedRowIndex = 0
        }
    }

    // Function for duplicate a selected row by its ID
    fun handleDuplicateRow(tableLayout: TableLayout, rowsArray: MutableList<RowsModel>, indexToDuplicate: Int) {
        if (indexToDuplicate >= 0 && indexToDuplicate < rowsArray.size) {
            val selectedRow = rowsArray[indexToDuplicate]
            val duplicatedRow = selectedRow.copy(id = "newId") // Change the ID if needed
            rowsArray.add(indexToDuplicate + 1, duplicatedRow)
            updateTableLayout(tableLayout, rowsArray)
            highlightedRowIndex = 0
        }
    }

    //Mark:- Keyboard Delegate function
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getWindowToken(), 0)
        return super.dispatchTouchEvent(ev)
    }

    //Mark:- function to update table after navigation performed
    fun updateTableLayout(tableLayout: TableLayout, rowsArray: MutableList<RowsModel>) {
        tableLayout.removeAllViews()
        headRow.setBackgroundColor(Color.parseColor(headRowBackgroundColor))
        setHeaderBackground(headRow)
        tableLayout.addView(headRow)

        rowsArray.forEachIndexed { index, dataRow ->
            val newIndex = index+1
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.background = setRowBackground()

            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = TableRow.LayoutParams(dpToPx(context,50), TableRow.LayoutParams.MATCH_PARENT)
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL
            linearLayout.background = getCellBackground()

            val imageView = ImageView(context)
            val imageViewLayoutParams = TableRow.LayoutParams(dpToPx(context,15), dpToPx(context,15))
            imageView.layoutParams = imageViewLayoutParams
            imageViewLayoutParams.setMargins(0, dpToPx(context,10), 0,0)
            imageView.setImageResource(R.drawable.unchecked_icon)
            linearLayout.addView(imageView)
            tableRow.addView(linearLayout)

            val hashLinearLayout = LinearLayout(context)
            hashLinearLayout.layoutParams = TableRow.LayoutParams(dpToPx(context,50), TableRow.LayoutParams.MATCH_PARENT)
            hashLinearLayout.gravity = Gravity.CENTER_HORIZONTAL
            hashLinearLayout.background = getCellBackground()

            val hash_Id = TextView(context)
            val hashIdLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT)
            hashIdLayoutParams.setMargins(0, dpToPx(context,10), 0,0)
            hash_Id.layoutParams = hashIdLayoutParams
            hash_Id.setTextColor(Color.parseColor(textColor))
            hash_Id.setText(""+newIndex)
            hashLinearLayout.addView(hash_Id)
            tableRow.addView(hashLinearLayout)
            insertCellsValues(index, tableRow, newIndex)

            if (index == rowsArray.size - 1 ) {
                tableRow.background = cornerRadiusForLastRow(30f)
                linearLayout.background = lastRowLeftBottomCornerRadius()
            } else {
                tableRow.background = setRowBackground()
            }
            tableLayout.addView(tableRow)


            linearLayout.setOnClickListener {
                selectedRow(tableRow, newIndex, tableLayout, moreLayout, imageView)
                onRowSelectionButtonClick(tableRow, imageView)
                setHeaderBackground(headRow)
            }
        }
    }

    fun tableViewHeader(tableLayout: TableLayout, columnTitleArray: MutableList<String>, headerRow: TableRow) {
        val numColumns = columnTitleArray.size
        val lastIndex = columnTitleArray.size - 1
        for ((index, columnName) in columnTitleArray.withIndex()) {
            val textView = TextView(tableLayout.context)
            val availableWidth = getScreenWidth(context) - 300
            textView.setTextColor(Color.parseColor(textColor))
            val layoutParams = TableRow.LayoutParams(
                if (numColumns == 1) {
                    availableWidth
                } else if (numColumns == 2) {
                    availableWidth/2
                } else {
                    dpToPx(context, 133)
                },
                TableRow.LayoutParams.MATCH_PARENT
            )

            textView.layoutParams = layoutParams

            textView.gravity = android.view.Gravity.CENTER
            textView.text = columnName
            textView.background = getCellBackground()
            if (index == lastIndex) {
                textView.background = setTableTopRightCornerRadius()
            } else {
                textView.background = getCellBackground()
            }

            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.setPadding(8, 8, 8, 8) // Add padding if needed
            headerRow.addView(textView)
        }
        tableLayout.addView(headerRow)
    }
}
    //Mark: Function for Swap Rows
    fun swapRowDown(rowsArray: MutableList<RowsModel>, position1: Int, position2: Int) {
        if (position1 in 0 until rowsArray.size && position2 in 0 until rowsArray.size) {
            Collections.swap(rowsArray, position1, position2)
        } else {
            Collections.swap(rowsArray, position1, position2)
        }
    }
