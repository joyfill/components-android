package com.joy.joyfillandroid.Tablefield

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.joy.joyfillandroid.DropDownAdapter
import com.joy.joyfillandroid.OnDrawHelper
import com.joy.joyfillandroid.R
import com.joy.joyfillandroid.RecyclerItemClickListenr
import com.joy.joyfillandroid.RecyclerModel
import com.joy.joyfillandroid.dpToPx
import com.joy.joyfillandroid.getScreenWidth
import org.json.JSONArray
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class TableField(context: Context): LinearLayout(context){
    val textColor ="#121417"
    val titleTextView = TextView(context)
    val borderColor: String ="#E6E7EA"
    var addNewIndex: Int = 0
    val rowCountTextView = TextView(context)
    val recyclerView = RecyclerView(context)
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var adapter: DropDownAdapter
    var list= ArrayList<RecyclerModel>()
    private var testString: String? = null
    var dataArray = TableFieldObj.RowArray
    var rowIndex: Int? = 0
    var columnIndex: Int? = 0
    object TableFieldObj {
        var RowArray = mutableListOf<MyDataList>()
    }
    init {
        addDummyRow()
        val columnArray = Table.ShareText.getRowTitle
        orientation = VERTICAL
        setPadding(25,15,25,15)


        val relativeLayout = RelativeLayout(context)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeLayout.setPadding(0, 0, 0, dpToPx(context,10))

        titleTextView.layoutParams = RelativeLayout.LayoutParams(
            dpToPx(context, 280),
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(dpToPx(context,1),dpToPx(context,10),0,dpToPx(context,0))
        }
        titleTextView.setTextColor(Color.parseColor("#000000"))
        titleTextView.textSize = 14f
        titleTextView.typeface = customTypeFace(500)

        val textViewLayout = LinearLayout(context)
        textViewLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            addRule(RelativeLayout.RIGHT_OF, )
            setMargins(0, 0, dpToPx(context,25), dpToPx(context,2))
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
            dpToPx(context,13),
            dpToPx(context,13),
            1f
        ).apply {
            setMargins(dpToPx(context,5), dpToPx(context,4), 0, 0)
        }

        textViewLayout.setOnClickListener {
            val intent = Intent(context, TableViewActivity::class.java)
            context.startActivity(intent)
        }


        rowCountTextView.typeface = customTypeFace(500)
        rowCountTextView.textSize = 12f
        rowCountTextView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            setMargins(0, dpToPx(context,12), dpToPx(context,5), 0)
        }

        rowCountTextView.setTextColor(Color.parseColor("#121417"))
        rowCountTextView.textSize = 12f

        textViewLayout.addView(viewTextView)
        textViewLayout.addView(arrowImageView)

        relativeLayout.addView(titleTextView)
        relativeLayout.addView(textViewLayout)
        relativeLayout.addView(rowCountTextView)
        addView(relativeLayout)

        val rootView = LinearLayout(context)
        rootView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        rootView.orientation = LinearLayout.VERTICAL

        val horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        horizontalScrollView.setHorizontalScrollBarEnabled(false)
        horizontalScrollView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                horizontalScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        val tableLayout = TableLayout(context)
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )

        val headRow = TableRow(context)
        headRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headRow.setBackgroundColor(Color.parseColor("#F3F4F8"))
        setTopRowBackground(headRow)
        createDynamicColumns(tableLayout, columnArray, headRow, Table.ShareText.getRowType)

        for (index in 0 until 3) {
            val dataRow = TableFieldObj.RowArray[index]
            addNewIndex = index + 1
            val newIndex = index + 1
            val tableRow = TableRow(context)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            if (index == 2 ) {
                tableRow.background = createRoundedBackgroundForLastRow(30f)
            } else {
                tableRow.background = getRowBackground()
            }

            addTextDropDownColumn(index, tableRow, tableLayout)
            tableLayout.addView(tableRow)
        }

        horizontalScrollView.addView(tableLayout)
        rootView.addView(horizontalScrollView)
        addView(rootView)
    }

    //Mark:- Function for Add Dummy Row
    private fun addDummyRow() {
        if (TableFieldObj.RowArray.size == 1){
            val numCells = findNumberOfColumns()
            val emptyRowCells = List(numCells) { "" }
            val dummyRow1 = MyDataList(id = "newRowId", deleted = false, cells = emptyRowCells)
            val dummyRow2 = MyDataList(id = "newRowId", deleted = false, cells = emptyRowCells)
            TableFieldObj.RowArray.add(dummyRow1)
            TableFieldObj.RowArray.add(dummyRow2)

        }else if(TableFieldObj.RowArray.size == 2){
            val numCells = findNumberOfColumns()
            val emptyRowCells = List(numCells) { "" }
            val dummyRow1 = MyDataList(id = "newRowId", deleted = false, cells = emptyRowCells)
            TableFieldObj.RowArray.add(dummyRow1)
        }
    }

    //Mark:- Function for update row count
    override fun isFocused(): Boolean {
        val idsToRemove = setOf("newRowId")
        dataArray.removeIf { it.id in idsToRemove }
        rowCountTextView.text = "+" + Table.ShareText.dataObjects.size
        return super.isFocused()
    }

    //Mark:- Function for create dynamic header row and set columns title
    fun createDynamicColumns(tableLayout: TableLayout, columnArray: MutableList<String>, headerRow: TableRow, columnType: MutableList<String>) {
        val numColumns = columnArray.size
        val context = tableLayout.context

        for ((index, columnName) in columnArray.withIndex()) {
            val textView = TextView(context)
            textView.setPadding(dpToPx(context,5), dpToPx(context,5), dpToPx(context,5),dpToPx(context,5))
            textView.setTextColor(Color.parseColor(textColor))
            val availableWidth = getScreenWidth(context) - 100
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

            if (index == 0) {
                textView.background = topLeftCellBackground()
            }else if (index == numColumns - 1) {
                textView.background = topRightCellBackground()
            }else{
                setTopRowBackground(headerRow)
                textView.background = getCellBackground()
            }
            textView.layoutParams = layoutParams
            textView.gravity = Gravity.CENTER
            textView.text = columnName
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            headerRow.addView(textView)
        }
        tableLayout.addView(headerRow)
    }

    //Mark:- Function for create dynamic rows and update cells values
     fun addTextDropDownColumn(index: Int, tableRow: TableRow, tableLayout: TableLayout) {
        for (i in Table.ShareText.getRowType.indices) {
            val type = Table.ShareText.getRowType[i]
            val columnOrderIndex = i
            val rowOrderIndex = index
            val columnId = Table.ShareText.tableColumnOrderArrayID[columnOrderIndex]
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
                    textColumnView.background = editTextBackground("#1F6BFF",0)
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
                    if (index == 2  && columnOrderIndex == Table.ShareText.getRowType.size - 1) {
                        textColumnLayout.background = lastRowRightBottom()
                    }else if (index == 2 && columnOrderIndex == 0){
                        textColumnLayout.background = firstCellLastRow()
                    }

                    textColumnView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            textColumnView.background =  editTextBackground("#1F6BFF",2)
                        } else {
                            textColumnView.background = editTextBackground("#E6E7EA",3)
                        }
                    }

                    val cellValues = TableFieldObj.RowArray[rowOrderIndex].cells
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
                    val cellValues = TableFieldObj.RowArray[rowOrderIndex].cells

                    for (i in 0 until Table.ShareText.tableColumns_Array.length()) {
                        val option = Table.ShareText.tableColumns_Array.getJSONObject(i)
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
                    if (index == 2 && columnOrderIndex == Table.ShareText.getRowType.size - 1) {
                        dropDownColumnLayout.background = lastRowRightBottom()
                    }else if (index == 2 && columnOrderIndex == 0){
                        dropDownColumnLayout.background = firstCellLastRow()
                    }

                    dropDownColumnLayout.setOnClickListener {
                        var indexValue: Pair<Int, Int>? = null
                        indexValue = getColumnIndex(tableLayout, dropDownColumnLayout)
                        rowIndex = indexValue?.first
                        columnIndex = indexValue?.second
                        if (rowIndex != null && columnIndex != null) {
                            createRecyclerView(context, options, tableLayout)
                        }
                    }

                    dropDownColumnLayout.addView(dropDownText)
                    dropDownColumnLayout.addView(downArrow)
                    tableRow.addView(dropDownColumnLayout)
                }
            }
        }
    }

    //Mark:- Function for finding row index and column index of the table
    fun getColumnIndex(tableLayout: TableLayout, cell: View): Pair<Int, Int>? {
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

    //Mark: Function for showing Bottom Sheet with multiple options
    fun createRecyclerView(context: Context, option: JSONArray?, tableLayout: TableLayout): LinearLayout {

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
                testString = newText
                Table.ShareText.text = newText
                updateCellText(tableLayout, rowIndex!!, columnIndex!! )
                bottomSheetDialog.dismiss()
            }
        }))

        linearLayout.addView(recyclerView)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
        return linearLayout
    }

    fun updateCellText(tableLayout: TableLayout, rowIndex: Int, columnIndex: Int) {
        val row = tableLayout.getChildAt(rowIndex) as TableRow
        if (columnIndex >= 0 && columnIndex < row.childCount) {
            val linearLayout = row.getChildAt(columnIndex) as LinearLayout
            if (linearLayout.childCount > 0) {
                val cell = linearLayout.getChildAt(0) as TextView
                if (cell != null){
                    cell.text = Table.ShareText.text
                }
            }
        }
    }

    //Mark: function for finds number of columns
    fun findNumberOfColumns(): Int {
        if (dataArray.isNotEmpty()) {
            val firstRow = dataArray.first()
            return firstRow.cells.size
        }
        return 0
    }
    
    override fun onDraw(canvas: Canvas?) {
        OnDrawHelper.onDrawGlobal(context, this)
        super.onDraw(canvas)

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

    fun topRightCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 30f, 30f, 0f, 0f, 0f, 0f)
        drawable.setStroke(3, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    fun topLeftCellBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(30f, 30f, 0f, 0f, 0f, 0f, 0f, 0f)
        drawable.setStroke(3, Color.parseColor("#E6E7EA"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    fun editTextBackground(color: String, strokewidth: Int): GradientDrawable{
        val editDrawable = GradientDrawable()
        editDrawable.shape = GradientDrawable.RECTANGLE
        editDrawable.setStroke(strokewidth,Color.parseColor(color))
        return editDrawable
    }

    private fun setTopRowBackground(view: TableRow){
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#F3F4F8"))
        drawable.cornerRadii = floatArrayOf(
            30f, 30f, 30f, 30f,
            0f, 0f, 0f, 0f
        )
        drawable.setStroke(3,Color.parseColor("#E6E7EA"))
        view.background = drawable
    }

    fun  lastRowRightBottom(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2,Color.parseColor("#E6E7EA"))
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 30f, 30f, 0f, 0f)
        return drawable
    }

    fun firstCellLastRow(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(3, Color.parseColor("#E2E3E7"))
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 30f, 30f)
        return drawable
    }

    fun getRowBackground(): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(2 , Color.parseColor("#E2E3E7"))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    fun createRoundedBackgroundForLastRow(radius: Float): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setStroke(3, Color.parseColor("#E2E3E7"))
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
        return gradientDrawable
    }

    //Mark: Function update values at run time
    fun updateTableTitle(title: String){
        titleTextView.text = title
        Table.ShareText.title = title
    }
}
