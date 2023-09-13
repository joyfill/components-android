package com.joy.joyfillandroid.Tablefield

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import com.joy.joyfillandroid.*
import com.joy.joyfillandroid.DateTimeField.DateTime
import com.joy.joyfillandroid.model.componentModel
import com.joy.joyfillandroid.signature.Signature
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.P)
class MobileSDKField(context: Context) : LinearLayout(context){
    val componentLayout = LinearLayout(context)
    var yValuesMobile = ArrayList<Int>()
    var getComponentType = ArrayList<String>()
    var sequenceWithYPosition: List<String> = emptyList()
    var dataValues = ArrayList<String>()
    var tableColumnOrderArrayID = ArrayList<String>()
    val tableColumnType = mutableListOf<String>()
    val tableColumnTitle = mutableListOf<String>()
    val tableColumnValue = mutableListOf<String>()
    var type: String? = null
    var list= ArrayList<componentModel>()
    val mainHandler = Handler(context.mainLooper)
    var options: JSONArray? = null
    private val client = OkHttpClient()
    object SharedData {
        var joyfillDocID: String = ""
        var joyfillauthorizedToken: String = ""
    }
    val textField = TextField(context)
    val dropDown = DropDown(context)
    val multipleChoice = MultipleChoice(context)


    val typePositionsMap = mutableMapOf<String, Int>()
    val idPositionsMap = mutableMapOf<String, Int>()

    init {
        orientation = VERTICAL
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        setLayoutParams(layoutParams)

        layoutParams.setMargins(dpToPx(context, 8), dpToPx(context, 8), dpToPx(context, 8), dpToPx(context, 8))
        val componentLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        componentLayout.orientation = LinearLayout.VERTICAL
        componentLayout.layoutParams = componentLayoutParams

        val view = LinearLayout(context)
        val viewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        )

        view.layoutParams = viewLayoutParams
        view.orientation = VERTICAL
        view.background = layoutBackground()

        val scrollView = NestedScrollView(context)
        val scrollViewParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        scrollViewParams.setMargins(0, dpToPx(context, 10), 0, dpToPx(context, 10))
        scrollView.layoutParams= scrollViewParams
        scrollView.setVerticalScrollBarEnabled(false)
        scrollView.overScrollMode= View.OVER_SCROLL_NEVER
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)

        scrollView.addView(componentLayout)
        view.addView(scrollView)
        addView(view)
        retrieveJoyfillData()
    }

    //Mark: Function for retrieve joy fill data from API
    fun retrieveJoyfillData() {
        val url = "https://api-joy.joyfill.io/v1/documents/"+ SharedData.joyfillDocID
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", SharedData.joyfillauthorizedToken)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                val outputJsonString =  JSONObject(responseBody)
                val fileArray = outputJsonString.getJSONArray("files")
                val fields = outputJsonString.getJSONArray("fields")

                for (i in 0 until fileArray.length()){
                    val pages = fileArray.getJSONObject(i).getJSONArray("pages")
                    val views = fileArray.getJSONObject(i).getJSONArray("views")

                    if (views.length()>0){
                        // For Mobile View
                        for (j in 0 until views.length()){
                            val viewPages = views.getJSONObject(j).getJSONArray("pages")
                            for (k in 0 until viewPages.length()){
                                val fieldPositions = viewPages.getJSONObject(k).getJSONArray("fieldPositions")
                                for (l in 0 until fieldPositions.length()){
                                    val componentType = fieldPositions.getJSONObject(l).get("type")
                                    val yPosition =  fieldPositions.getJSONObject(l).getDouble("y")
                                    yValuesMobile.add(yPosition.toInt())
                                    getComponentType.add(componentType.toString())
                                    val componentTypePairedArray = yValuesMobile.zip(getComponentType)
                                    val sortedType = componentTypePairedArray.sortedBy { it.first }
                                    val sortedTypeValue = fetchSortedComponentType(sortedType.toString())
                                    sequenceWithYPosition = sortedTypeValue
                                }
                                updateBlockFieldValues(fieldPositions)
                            }
                            joyFillComponents(sequenceWithYPosition, fields)
                        }
                    } else {
                        // For Primary View or Web View
                        for (j in 0 until pages.length()) {
                            val fieldPositions = pages.getJSONObject(j).getJSONArray("fieldPositions")
                            for (k in 0 until fieldPositions.length()) {
                                val componentType = fieldPositions.getJSONObject(k).get("type")
                                val yPosition =  fieldPositions.getJSONObject(k).getDouble("y")
                                yValuesMobile.add(yPosition.toInt())
                                getComponentType.add(componentType.toString())
                                val componentTypePairedArray = yValuesMobile.zip(getComponentType)
                                val sortedType = componentTypePairedArray.sortedBy { it.first }
                                val sortedTypeValue = fetchSortedComponentType(sortedType.toString())
                                sequenceWithYPosition = sortedTypeValue
                                dataValues.addAll(fetchSortedComponentType(sortedType.toString()))
                            }
                            updateBlockFieldValues(fieldPositions)

                        }
                        joyFillComponents(sequenceWithYPosition, fields)
                    }
                }
                updateDropDownAndMultiSelectOptions(fields)
                getTableData(fields)
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("call "+ call)
            }
        })
    }

    fun getTableData(fields: JSONArray){
        for (i in 0 until fields.length()) {
            val type = fields.getJSONObject(i).get("type")

            if (type == "table"){
                val tableColumnsArray = fields.getJSONObject(i).getJSONArray("tableColumns")
                val rowOrderArray = fields.getJSONObject(i).getJSONArray("rowOrder")
                val valueArray = fields.getJSONObject(i).getJSONArray("value")
                val tableColumnOrderArray = fields.getJSONObject(i).getJSONArray("tableColumnOrder")
                var orderID: String? = null
                for (i in 0 until tableColumnOrderArray.length()) {
                    orderID = tableColumnOrderArray.getString(i)
                    tableColumnOrderArrayID.add(orderID)
                }

                val tableColumnID =  fields.getJSONObject(i).getJSONArray("tableColumns")
                for (i in 0 until tableColumnID.length()) {
                    val getTableColumnsID = tableColumnID.getJSONObject(i).optString("_id")
                    idPositionsMap[getTableColumnsID] = i
                }

                for (j in 0 until tableColumnOrderArrayID.size){
                    val getTableColumnOrderId = tableColumnOrderArrayID[j]

                    if (idPositionsMap.containsKey(getTableColumnOrderId)) {
                        val positionInFields = idPositionsMap[getTableColumnOrderId]!!
                        val getRowData = tableColumnID.getJSONObject(positionInFields)

                        val columnType = getRowData.optString("type")
                        val columnTitle = getRowData.optString("title")
                        val columnValue = getRowData.optString("value")

                        tableColumnValue.add(columnValue)
                        tableColumnType.add(columnType)
                        tableColumnTitle.add(columnTitle)
                    }
                }

                val rowCellsArray = mutableListOf<RowsModel>()

                val filteredArray = filterDeletedRows(valueArray)
                for (i in 0 until filteredArray.length()) {
                    val jsonObject = filteredArray.getJSONObject(i)
                    val cells = jsonObject.getJSONObject("cells")
                    val cellValues = tableColumnOrderArrayID.map { key ->
                        cells.optString(key.toString(), "")
                    }

                    val rowCells = RowsModel(
                        id = jsonObject.optString("_id", ""),
                        deleted = jsonObject.optBoolean("deleted", false),
                        cells = cellValues
                    )
                    rowCellsArray.add(rowCells)
                }


                Table.globalVariable.tableColumnsArray = tableColumnsArray
                Table.globalVariable.rowArray = rowCellsArray
                TableField.variable.rowArray = rowCellsArray
                Table.globalVariable.getColumnTitle = tableColumnTitle
                Table.globalVariable.getColumnType = tableColumnType
                Table.globalVariable.tableColumnValue = tableColumnValue
                Table.globalVariable.tableColumnOrderArrayID = tableColumnOrderArrayID
            }
        }

    }

    //Mark:- Showing rows if deleted false
    fun filterDeletedRows(rows: JSONArray): JSONArray {
        val filteredArray = JSONArray()
        for (i in 0 until rows.length()) {
            val jsonObject = rows.getJSONObject(i)
            val isDeleted = jsonObject.optBoolean("deleted", false)
            if (isDeleted == false) {
                filteredArray.put(jsonObject)
            }
        }

        return filteredArray
    }

    private fun updateDropDownAndMultiSelectOptions(fields: JSONArray) {
        for (i in 0 until fields.length()){
            val type = fields.getJSONObject(i).get("type")
            val id = fields.getJSONObject(i).get("_id")
            if (type == "dropdown"){
                val title = fields.getJSONObject(i).getString("title")
                val options = fields.getJSONObject(i).get("options")
                dropDown.updateDropDownvalue(title.toString(), "Options", "", options as JSONArray)
            }

            if (type == "multiSelect"){
                val title = fields.getJSONObject(i).getString("title")
                val options = fields.getJSONObject(i).get("options")
                val multi = fields.getJSONObject(i).optBoolean("multi")
                multipleChoice.setTextValue(title.toString(), options as JSONArray, multi, "fill")
            }
        }
    }

    private fun updateBlockFieldValues(fieldPositions: JSONArray) {
        for (i in 0 until fieldPositions.length()){
            val type = fieldPositions.getJSONObject(i).get("type")
            if (type == "block"){
                val fontSize = fieldPositions.getJSONObject(i).optInt("fontSize", 18)
                val fontColor = fieldPositions.getJSONObject(i).optString("fontColor", "#000000")
                val fontWeight = fieldPositions.getJSONObject(i).optString("fontWeight","normal")
                val fontStyle = fieldPositions.getJSONObject(i).optString("fontStyle","normal")
                val textAlign = fieldPositions.getJSONObject(i).optString("textAlign","left")
                val i = fontSize as Int
                val f = i.toFloat()
                textField.setTextSize(f)
                textField.setTextColor(Color.parseColor(fontColor.toString()))
                setFontStyle(fontStyle as String, textField)
                setFontWeight(fontWeight as String, textField)
                setTextAlign(textAlign as String, textField)
            }
        }
    }


    private fun joyFillComponents(shortedDataWithYPosition: List<String>, fields: JSONArray) {
        for (i in 0 until fields.length()) {
            val type = fields.getJSONObject(i).optString("type")
            typePositionsMap[type] = i
        }

        for (j in 0 until shortedDataWithYPosition.size) {
            val getSortedYPosition = shortedDataWithYPosition[j]

            if (typePositionsMap.containsKey(getSortedYPosition)) {
                val getSortedTypeWithYPosition = typePositionsMap[getSortedYPosition]!!

                val fieldsDataArray = fields.getJSONObject(getSortedTypeWithYPosition)
                val type = fieldsDataArray.optString("type")
                var value: String? =  fieldsDataArray.optString("value")
                val title = fieldsDataArray.optString("title")

                if (fieldsDataArray.has("options")) {
                    options = fieldsDataArray.getJSONArray("options")
                }
                mainHandler.post {
                    if (shortedDataWithYPosition[j] == "block") {
                        textField.text = value.toString()
                        componentLayout.addView(textField)
                    }

                    if (shortedDataWithYPosition[j] == "multiSelect") {
                        componentLayout.addView(multipleChoice)
                    }

                    if (shortedDataWithYPosition[j] == "text") {
                        val shortField = ShortField(context)
                        shortField.updateTextFieldTitle(title.toString(), value.toString())
                        componentLayout.addView(shortField)
                    }

                    if (shortedDataWithYPosition[j] == "dropdown") {
                        componentLayout.addView(dropDown)
                    }

                    if (shortedDataWithYPosition[j] == "table") {
                        val tableField = TableField(context)
                        tableField.updateTableTitle(title.toString())
                        componentLayout.addView(tableField)
                    }

                    if (shortedDataWithYPosition[j] == "number" ) {
                        val numberField = NumberField(context)
                        var numberValue: Int = 0

                        if (fieldsDataArray.has("value")){
                            numberValue = fieldsDataArray.optInt("value", 0)
                        }
                        numberField.updateNumberFieldValue(title.toString(), numberValue)
                        componentLayout.addView(numberField)
                    }

                    if (shortedDataWithYPosition[j] == "textarea") {
                        val longTextField = LongTextField(context)
                        longTextField.updateTextFieldTitle(title.toString(), value.toString())
                        componentLayout.addView(longTextField)
                    }
                }
            }
        }
    }

    // Mark: Regex for get Sorted Value
    fun fetchSortedComponentType(input: String): List<String> {
        val regex = Regex("\\(\\d+,\\s*(\\w+)\\)")
        val matches = regex.findAll(input)
        return matches.map { it.groupValues[1] }.toList()
    }

    fun layoutBackground(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 30f
        drawable.setStroke(2, Color.parseColor("#E2E3E7"))
        drawable.setColor(Color.parseColor("#FFFFFF"))
        return drawable
    }

    private fun setTextAlign(textAlign: String, textField: TextField) {
        if (textAlign.equals("center")) {
            textField.textAlignment = TEXT_ALIGNMENT_CENTER
        } else if (textAlign.equals("right")) {
            textField.textAlignment = TEXT_ALIGNMENT_TEXT_END
        } else {
            textField.textAlignment = TEXT_ALIGNMENT_TEXT_START
        }
    }

    private fun setFontWeight(fontWeight: String, textField: TextField) {
        if(fontWeight.equals("bold")) {
            textField.setBold(true)
        } else {
            textField.setBold(false)
        }
    }

    private fun setFontStyle(fontStyle: String, textField: TextField) {
        if(fontStyle.equals("italic")) {
            textField.setItalic(true)
        } else {
            textField.setItalic(false)
        }
    }
}
//Mark: Model class
data class RowsModel(val id: String, val deleted: Boolean, val cells: List<String>)