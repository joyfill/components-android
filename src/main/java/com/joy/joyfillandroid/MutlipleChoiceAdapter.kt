package com.joy.joyfillandroid.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.joy.joyfillandroid.R
import com.joy.joyfillandroid.model.MultiPleChoiceModel

class MutlipleChoiceAdapter (private val mList: ArrayList<MultiPleChoiceModel>, private val context: Context, private val multiSelection: Boolean, private val multipleChoiceDspMode: String): RecyclerView.Adapter<MutlipleChoiceAdapter.ViewHolder>(){

    private var selectedItemPosition: Int = -1
    private val textColor ="#121417"
    private val white ="#FFFFFF"
    private val borderGrey="#D1D1D6"
    private val blue = "#256FFF"
    private val containerBorderColor="#DBDBDD"
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context

        val container = LinearLayout(context)
        container.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        container.orientation = LinearLayout.VERTICAL
        container.gravity = Gravity.CENTER
        container.setBackgroundColor(Color.parseColor(white))
        container.background = containerBackground()

        val checkBox = CheckBox(context)
        val checkBoxParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(45)
        )
        checkBoxParams.setMargins(dpToPx(10), 0, 0, 0)
        checkBox.layoutParams = checkBoxParams
        checkBox.buttonDrawable = null
        applyCustomSelectorToCheckBox(context, checkBox)
        checkBox.text = "Item One"
        checkBox.typeface = Typeface.create(null, 400, false)
        checkBox.textSize = 18f
        checkBox.setTextColor(Color.parseColor(textColor))
        checkBox.setPadding(dpToPx( 12), 0, 0, 0)

        val divider = View(context)
        val dividerParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx( 2)
        )
        divider.layoutParams = dividerParams
        divider.setBackgroundColor(Color.parseColor(containerBorderColor))

        container.addView(checkBox)
        container.addView(divider)

        return ViewHolder(container, checkBox, divider)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val list = mList[position]
        holder.checkBox.setText(list.checkBoxLabel)
        holder.checkBox.isChecked = list.isSelected

        if (multipleChoiceDspMode.equals("fill")){
            if (!multiSelection) {
                holder.checkBox.setOnCheckedChangeListener(null)
                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        for (i in mList.indices) {
                            if (i != position) {
                                mList[i].isSelected = false
                                notifyItemChanged(i)
                            }
                        }
                        list.isSelected = true
                        selectedItemPosition = position
                    } else {
                        list.isSelected = false
                        selectedItemPosition = RecyclerView.NO_POSITION
                    }
                }
            } else {
                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    list.isSelected = isChecked
                }
            }
        }else{
            holder.checkBox.isClickable = false
        }

        if (position == mList.size - 1) {
            holder.divider.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.divider.setBackgroundColor(Color.parseColor(containerBorderColor))
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(container: LinearLayout, var checkBox: CheckBox, val divider: View) : RecyclerView.ViewHolder(container)

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    private fun containerBackground(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 50f
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun createCustomSelector(context: Context): StateListDrawable {
        val selector = StateListDrawable()
        val stateChecked = intArrayOf(android.R.attr.state_checked)
        val drawableChecked = context.getDrawable(R.drawable.check_box)
        selector.addState(stateChecked, drawableChecked)
        val statePressed = intArrayOf(android.R.attr.state_pressed)
        val drawablePressed = context.getDrawable(R.drawable.check_box)
        selector.addState(statePressed, drawablePressed)
        val stateUnchecked = intArrayOf()
        val drawableUnchecked = context.getDrawable(R.drawable.check_box_outline)
        selector.addState(stateUnchecked, drawableUnchecked)
        return selector
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun applyCustomSelectorToCheckBox(context: Context, checkBox: CheckBox) {
        val selector = createCustomSelector(context)
        checkBox.buttonDrawable = selector
    }

    fun customUnCheckBox(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setStroke(4, Color.parseColor(color))
        drawable.setColor(Color.parseColor(white))
        drawable.cornerRadius = 15f
        return drawable
    }

    fun customCheckedBox(color: String): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor(blue))
        drawable.cornerRadius = 15f
        return drawable
    }
}