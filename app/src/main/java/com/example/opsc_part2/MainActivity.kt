package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGoToDash = findViewById<Button>(R.id.btnSignIn)


        btnGoToDash.setOnClickListener{

            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)

        }

    }
}