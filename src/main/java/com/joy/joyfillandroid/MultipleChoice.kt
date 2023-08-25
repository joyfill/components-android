package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.joy.joyfillandroid.adapter.MutlipleChoiceAdapter
import com.joy.joyfillandroid.model.MultiPleChoiceModel
import org.json.JSONArray

@RequiresApi(Build.VERSION_CODES.P)
class MultipleChoice @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int =0
) : LinearLayout(context, attrs, defStyleAttr){

    private val textColor ="#121417"
    private val borderGrey="#D1D1D6"

    val label = TextView(context)
    val checkBoxList = RecyclerView(context)

    var list= ArrayList<MultiPleChoiceModel>()
    private lateinit var adapter: MutlipleChoiceAdapter

    init {
        orientation = VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 170)
        )

        layoutParams.setMargins(dpToPx(context,10),dpToPx(context,12),dpToPx(context,10),dpToPx(context,0))
        setLayoutParams(layoutParams)
        val labelLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        labelLayoutParams.setMargins(0, 0,0,dpToPx(context,8))
        label.layoutParams = labelLayoutParams
        label.setTextColor(Color.parseColor(textColor))
        label.textSize = 16f
        label.typeface = Typeface.create(null, 500, false)
        addView(label)

        val multiChoiceContainer = LinearLayout(context)
        val containerLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(context,110))
        multiChoiceContainer.layoutParams = containerLayoutParams
        multiChoiceContainer.background = containerBackground(borderGrey)

        checkBoxList.background = recyclerViewBackground(borderGrey)
        val checkBoxListLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(context,124))
        containerLayoutParams.setMargins(dpToPx(context,1), dpToPx(context,2),dpToPx(context,1),dpToPx(context,1))
        checkBoxList.layoutParams = checkBoxListLayoutParams
        checkBoxList.setPadding(dpToPx(context,1),dpToPx(context,5),dpToPx(context,1),dpToPx(context,5))
        checkBoxList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        checkBoxList.overScrollMode= View.OVER_SCROLL_NEVER
        addView(checkBoxList)
    }

    private fun containerBackground(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadius = 30f
        drawable.setStroke(3, Color.parseColor(color))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    private fun recyclerViewBackground(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadius = 30f
        drawable.setStroke(3, Color.parseColor(color))
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    //Mark: Function update values at run time
    fun setTextValue(newText: String, listData: JSONArray, multiSelection: Boolean, multipleChoiceDspMode: String) {
        label.text = newText
        list = ArrayList()
        for (i in 0 until listData.length()) {
            val item = listData.getJSONObject(i)
            val checkBoxValue = item.optString("value", "")
            list.add(MultiPleChoiceModel(checkBoxValue, false))
        }
        adapter = MutlipleChoiceAdapter(list, context, multiSelection, multipleChoiceDspMode)
        checkBoxList.adapter = adapter
    }
}