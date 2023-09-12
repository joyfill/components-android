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
    var yValues_mobile = ArrayList<Int>()
    var component_type = ArrayList<String>()
    var Sequence_With_Y_Position: List<String> = emptyList()
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
        var authorizedToken: String = ""
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
        makeApiCall()
    }

    //Mark: Function for API Call
    fun makeApiCall() {
        val url = "https://api-joy.joyfill.io/v1/documents/"+ SharedData.joyfillDocID
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", SharedData.authorizedToken)
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
                            val view_pages = views.getJSONObject(j).getJSONArray("pages")
                            for (k in 0 until view_pages.length()){
                                val fieldPositions =view_pages.getJSONObject(k).getJSONArray("fieldPositions")
                                for (l in 0 until fieldPositions.length()){
                                    val component_Type = fieldPositions.getJSONObject(l).get("type")
                                    val Y_Position =  fieldPositions.getJSONObject(l).getDouble("y")
                                    yValues_mobile.add(Y_Position.toInt())
                                    component_type.add(component_Type.toString())
                                    val componentTypePairedArray = yValues_mobile.zip(component_type)
                                    val sorted_type = componentTypePairedArray.sortedBy { it.first }
                                    val sortedType_value = valueForSortedType(sorted_type.toString())
                                    Sequence_With_Y_Position = sortedType_value
                                }
                                formatBlockData(fieldPositions)
                            }
                            ShowComponents(Sequence_With_Y_Position, fields)
                        }
                    }else {
                        // For Primary View or Web View
                        for (j in 0 until pages.length()) {
                            val fieldPositions = pages.getJSONObject(j).getJSONArray("fieldPositions")
                            for (k in 0 until fieldPositions.length()) {
                                val component_Type = fieldPositions.getJSONObject(k).get("type")
                                val Y_Position =  fieldPositions.getJSONObject(k).getDouble("y")
                                yValues_mobile.add(Y_Position.toInt())
                                component_type.add(component_Type.toString())
                                val componentTypePairedArray = yValues_mobile.zip(component_type)
                                val sorted_type = componentTypePairedArray.sortedBy { it.first }
                                val sortedType_value = valueForSortedType(sorted_type.toString())
                                Sequence_With_Y_Position = sortedType_value
                                dataValues.addAll(valueForSortedType(sorted_type.toString()))
                            }
                            formatBlockData(fieldPositions)

                        }
                        ShowComponents(Sequence_With_Y_Position, fields)
                    }
                }
                updateVaue(fields)
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
                    val columnId = tableColumnID.getJSONObject(i).optString("_id")
                    idPositionsMap[columnId] = i
                }

                for (j in 0 until tableColumnOrderArrayID.size){
                    val idFromShortedData = tableColumnOrderArrayID[j]
                    if (idPositionsMap.containsKey(idFromShortedData)) {
                        val positionInFields = idPositionsMap[idFromShortedData]!!
                        val haveID = tableColumnID.getJSONObject(positionInFields)
                        val columnType = haveID.optString("type")
                        val columnTitle = haveID.optString("title")
                        val columnValue = haveID.optString("value")
                        tableColumnValue.add(columnValue)
                        tableColumnType.add(columnType)
                        tableColumnTitle.add(columnTitle)
                    }
                }

                // for number of rows
                var dataArray = mutableListOf<String>()
                for (m in 0 until rowOrderArray.length()){
                    val row_id = rowOrderArray.get(m)
                    dataArray.add(row_id.toString())
                }

                val dataObjects = mutableListOf<MyDataList>()

                val filteredArray = filterDeletedObjects(valueArray)
                for (i in 0 until filteredArray.length()) {
                    val jsonObject = filteredArray.getJSONObject(i)
                    val cellsObject = jsonObject.getJSONObject("cells")
                    val cellValues = tableColumnOrderArrayID.map { key ->
                        cellsObject.optString(key.toString(), "")
                    }
                    val myData = MyDataList(
                        id = jsonObject.optString("_id", ""),
                        deleted = jsonObject.optBoolean("deleted", false),
                        cells = cellValues
                    )
                    dataObjects.add(myData)
                }

                Table.ShareText.tableColumns_Array = tableColumnsArray
                Table.ShareText.dataObjects = dataObjects
                TableField.TableFieldObj.RowArray = dataObjects
                Table.ShareText.getRowTitle = tableColumnTitle
                Table.ShareText.getRowType = tableColumnType
                Table.ShareText.tableColumnValue = tableColumnValue
                Table.ShareText.tableColumnOrderArrayID = tableColumnOrderArrayID
            }
        }

    }

    //Mark:- Showing rows if deleted false
    fun filterDeletedObjects(dataArray: JSONArray): JSONArray {
        val filteredArray = JSONArray()
        for (i in 0 until dataArray.length()) {
            val jsonObject = dataArray.getJSONObject(i)
            val isDeleted = jsonObject.optBoolean("deleted", false)
            if (isDeleted == false) {
                filteredArray.put(jsonObject)
            }
        }

        return filteredArray
    }

    private fun updateVaue(fields: JSONArray) {
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

    private fun formatBlockData(fieldPositions: JSONArray) {
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


    private fun ShowComponents(shorteddatawithYPosition: List<String>, fields: JSONArray) {
        for (i in 0 until fields.length()) {
            val type = fields.getJSONObject(i).optString("type")
            typePositionsMap[type] = i
        }

        for (j in 0 until shorteddatawithYPosition.size) {
            val typeFromShortedData = shorteddatawithYPosition[j]

            if (typePositionsMap.containsKey(typeFromShortedData)) {
                val positionInFields = typePositionsMap[typeFromShortedData]!!

                val haveValues = fields.getJSONObject(positionInFields)
                val type = haveValues.optString("type")
                var value: String? =  haveValues.optString("value")
                val title_1 = haveValues.optString("title")

                if (haveValues.has("options")) {
                    options = haveValues.getJSONArray("options")
                }
                mainHandler.post {
                    if (shorteddatawithYPosition[j] == "block") {
                        textField.text = value.toString()
                        componentLayout.addView(textField)
                    }

                    if (shorteddatawithYPosition[j] == "multiSelect") {
                        componentLayout.addView(multipleChoice)
                    }

                    if (shorteddatawithYPosition[j] == "text") {
                        val shortField = ShortField(context)
                        shortField.updateTextFieldTitle(title_1.toString(), value.toString())
                        componentLayout.addView(shortField)
                    }

                    if (shorteddatawithYPosition[j] == "dropdown") {
                        componentLayout.addView(dropDown)
                    }

                    if (shorteddatawithYPosition[j] == "table") {
                        val tableField = TableField(context)
                        tableField.updateTableTitle(title_1.toString())
                        componentLayout.addView(tableField)
                    }

                    if (shorteddatawithYPosition[j] == "number" ) {
                        val numberField = NumberField(context)
                        var numberValue: Int = 0

                        if (haveValues.has("value")){
                            numberValue = haveValues.optInt("value", 0)
                        }
                        numberField.updateNumberFieldValue(title_1.toString(), numberValue)
                        componentLayout.addView(numberField)
                    }

                    if (shorteddatawithYPosition[j] == "textarea") {
                        val longTextField = LongTextField(context)
                        longTextField.updateTextFieldTitle(title_1.toString(), value.toString())
                        componentLayout.addView(longTextField)
                    }
                }
            }
        }
    }

    // Mark: Regex for get Sorted Value
    fun valueForSortedType(input: String): List<String> {
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
        if (textAlign.equals("center")){
            textField.textAlignment = TEXT_ALIGNMENT_CENTER
        }else if (textAlign.equals("right")){
            textField.textAlignment = TEXT_ALIGNMENT_TEXT_END
        }else{
            textField.textAlignment = TEXT_ALIGNMENT_TEXT_START
        }
    }

    private fun setFontWeight(fontWeight: String, textField: TextField) {
        if(fontWeight.equals("bold")){
            textField.setBold(true)
        }else{
            textField.setBold(false)
        }
    }

    private fun setFontStyle(fontStyle: String, textField: TextField) {
        if(fontStyle.equals("italic")){
            textField.setItalic(true)
        }else{
            textField.setItalic(false)
        }
    }
}

//Mark: Model class
data class MyDataList(val id: String, val deleted: Boolean, val cells: List<String>)