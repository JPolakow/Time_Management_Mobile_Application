/*
package com.example.opsc_part2

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.core.motion.CustomAttribute

class custom_dashboard_cards(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {
    init {
        inflate(context, R.layout.example, this)

        val customAttributesStyle =
            context.obtainStyledAttributes(attrs, R., 0, 0)


        // ------------------------------------- Bind TextViews ------------------------------------- //
        val activityName = findViewById<TextView>(R.id.tvActivityName)
        val dateCreated = findViewById<TextView>(R.id.tvDateCreated)
        val minGoal = findViewById<TextView>(R.id.tvMinGoal)
        val maxGoal = findViewById<TextView>(R.id.tvMaxGoal)
        val timerTick = findViewById<TextView>(R.id.txtTimerTick)


        try {

            activityName.text = customAttributesStyle.getString(R.styleable.CustomAttribute)
            button1.text =
                customAttributesStyle.getString(R.styleable.CustomButtonLayout_button1Text)
            button2.text =
                customAttributesStyle.getString(R.styleable.CustomButtonLayout_button2Text)

        } finally {
            customAttributesStyle.recycle()
        }

        button1.setOnClickListener {
            // Handle button1 click event...
        }

        button2.setOnClickListener {
            // Handle button2 click event...
        }
    }
}*/
