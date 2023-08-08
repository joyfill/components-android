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
import com.joy.joyfillandroid.adapter.MutlipleChoiceAdapter
import com.joy.joyfillandroid.model.MultiPleChoiceModel

@RequiresApi(Build.VERSION_CODES.P)
class MultipleChoice @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int =0
) : LinearLayout(context, attrs, defStyleAttr){

    private val textColor ="#121417"
    private val white ="#FFFFFF"
    private val borderGrey="#D1D1D6"
    private val blue = "#256FFF"

    val label = TextView(context)
    val checkBoxList = RecyclerView(context)

    var list= ArrayList<MultiPleChoiceModel>()
    private lateinit var adapter: MutlipleChoiceAdapter

    init {
        orientation = VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 190)
        )
        layoutParams.setMargins(dpToPx(context,10),dpToPx(context,10),dpToPx(context,10),dpToPx(context,10))
        setLayoutParams(layoutParams)

        val labelLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        labelLayoutParams.setMargins(0, 0,0,dpToPx(context,8))
        label.layoutParams = labelLayoutParams
        label.setTextColor(Color.parseColor(textColor))
        label.textSize = 16f
        label.typeface = Typeface.create(null, 500, false)
        addView(label)

        val multiChoiceContainer = LinearLayout(context)
        val containerLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(context,138))
        multiChoiceContainer.layoutParams = containerLayoutParams
        multiChoiceContainer.background = containerBackground(borderGrey)

        checkBoxList.background = recyclerViewBackground(borderGrey)
        val checkBoxListLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        checkBoxList.layoutParams = checkBoxListLayoutParams
        checkBoxList.setPadding(dpToPx(context,1),dpToPx(context,1),dpToPx(context,1),dpToPx(context,3))
        checkBoxList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        checkBoxList.isVerticalScrollBarEnabled = true
        checkBoxList.overScrollMode= View.OVER_SCROLL_NEVER

        multiChoiceContainer.addView(checkBoxList)
        addView(multiChoiceContainer)
    }

    private fun containerBackground(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadius = 30f
        drawable.setStroke(2, Color.parseColor(color))
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(Color.parseColor("#FFFFFF"))
        return drawable
    }

    private fun recyclerViewBackground(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadius = 50f
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    //Mark: update dynamic values
    fun setLabelText(newText: String, itemTexts: Array<String>, multiSelection: Boolean, multipleChoiceDspMode: String, selected: Boolean) {
        label.text = newText
        list = ArrayList()
        for (text in itemTexts) {
            list.add(MultiPleChoiceModel(text, selected))
        }
        adapter = MutlipleChoiceAdapter(list, context, multiSelection, multipleChoiceDspMode)
        checkBoxList.adapter = adapter
    }
}