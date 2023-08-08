package com.joy.joyfillandroid

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class DropDownAdapter(private val mlist: ArrayList<RecyclerModel>, private val context: Context): RecyclerView.Adapter<DropDownAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context

        val container = LinearLayout(context)
        container.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        container.orientation = LinearLayout.VERTICAL
        container.setBackgroundColor(Color.parseColor("#FFFFFF"))

        val checkBox = CheckBox(context)
        val checkBoxParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        checkBoxParams.setMargins(0, dpToPx(context, 0), 0, dpToPx(context, 0))
        checkBox.layoutParams = checkBoxParams
        checkBox.buttonDrawable = null
        checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_checkbox, 0, 0, 0)
        checkBox.compoundDrawablePadding = dpToPx(context, 20)
        checkBox.typeface = Typeface.create(null, 400, false)
        checkBox.textSize = 18f
        checkBox.setTextColor(Color.parseColor("#121417"))
        checkBox.setPadding(dpToPx(context, 24), dpToPx(context, 14), 0, dpToPx(context, 14))

        val divider = View(context)
        val dividerParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 1)
        )
        divider.layoutParams = dividerParams
        divider.setBackgroundColor(Color.parseColor("#DBDBDD"))
        container.addView(checkBox)
        container.addView(divider)

        return ViewHolder(container, checkBox)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = mlist[position]
        holder.checkBox.setText(list.checkBox)
        holder.checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                holder.checkBox.setBackgroundColor(Color.parseColor("#E3E3E3"))
            } else {
                holder.checkBox.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        })
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    class ViewHolder(container: LinearLayout, var checkBox: CheckBox) : RecyclerView.ViewHolder(container)

}