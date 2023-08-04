package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
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

    private val multiSelection = false
    private val multipleChoiceDspMode ="readonly"

    private val textColor ="#121417"
    private val white ="#FFFFFF"
    private val borderGrey="#D1D1D6"
    private val blue = "#256FFF"
    private val containerBorderColor="#D1D1D6"

    var list= ArrayList<MultiPleChoiceModel>()
    private lateinit var adapter: MutlipleChoiceAdapter

    init {
        orientation = VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(190)
        )
        layoutParams.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10))
        setLayoutParams(layoutParams)

        val label = TextView(context)
        label.text = "MultiChoice Field"
        val labelLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        labelLayoutParams.setMargins(0, 0,0,dpToPx(8))
        label.layoutParams = labelLayoutParams
        label.setTextColor(Color.parseColor(textColor))
        label.textSize = 16f
        label.typeface = Typeface.create(null, 500, false)
        addView(label)

        val multiChoiceContainer = LinearLayout(context)
        val containerLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(140))
        multiChoiceContainer.layoutParams = containerLayoutParams
        multiChoiceContainer.background = containerBackground(containerBorderColor)

        val checkBoxList = RecyclerView(context)
        checkBoxList.background = recyclerViewBackground(containerBorderColor)
        val checkBoxListLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        checkBoxList.layoutParams = checkBoxListLayoutParams
        checkBoxListLayoutParams.setMargins(5,5,5,5)
        checkBoxList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        list = ArrayList()
        list.add(MultiPleChoiceModel("Item One", false))
        list.add(MultiPleChoiceModel("Item Two",false))
        list.add(MultiPleChoiceModel("Item Three", false))
        list.add(MultiPleChoiceModel("Item Four", false))

        adapter = MutlipleChoiceAdapter(list, getContext(),multiSelection, multipleChoiceDspMode)
        checkBoxList.adapter = adapter
        multiChoiceContainer.addView(checkBoxList)
        addView(multiChoiceContainer)
    }

    private fun containerBackground(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.cornerRadius = 50f
        drawable.setStroke(4, Color.parseColor(color))
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

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}