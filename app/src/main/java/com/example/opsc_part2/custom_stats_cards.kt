package com.example.opsc_part2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

class custom_stats_cards @JvmOverloads constructor(
context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_stats_cards, this, true)
    }

    //============================================================================
    // Used to set category Name
    fun setCategoryName(name: String) {
        val categoryName = findViewById<TextView>(R.id.txtName)
        categoryName.text = name;
    }

    //============================================================================
    // Used to set amount
    fun setCategoryAmount(amount: String) {
        val categoryAmount = findViewById<TextView>(R.id.txtAmount)
        categoryAmount.text = amount;
    }

    //============================================================================
    // Used to set category Name
    fun setCategoryDuration(name: String) {
        val categoryDuration = findViewById<TextView>(R.id.txtDuration)
        categoryDuration.text = name;
    }
}