package com.joy.joyfillandroid.Tablefield

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.joy.joyfillandroid.dpToPx\

class TableViewActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootLayout.layoutParams = layoutParams
        layoutParams.setMargins(dpToPx(this, 5),dpToPx(this, 10), dpToPx(this, 5), dpToPx(this, 10))
        rootLayout.orientation = LinearLayout.VERTICAL
        val table = Table(this)
        rootLayout.addView(table)
        setContentView(rootLayout)
    }
}