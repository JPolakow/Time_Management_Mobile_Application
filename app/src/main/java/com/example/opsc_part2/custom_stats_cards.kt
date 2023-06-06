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
    // Used to set catagory Name
    fun setCatagoryName(name: String) {
        val catagoryName = findViewById<TextView>(R.id.txtName)
        catagoryName.text = name;
    }

    //============================================================================
    // Used to set amount
    fun setCatagoryAmount(amount: String) {
        val catagoryAmount = findViewById<TextView>(R.id.txtAmount)
        catagoryAmount.text = amount;
    }

    //============================================================================
    // Used to set catagory Name
    fun setCatagoryDuration(name: String) {
        val catagoryDuration = findViewById<TextView>(R.id.txtDuration)
        catagoryDuration.text = name;
    }
}