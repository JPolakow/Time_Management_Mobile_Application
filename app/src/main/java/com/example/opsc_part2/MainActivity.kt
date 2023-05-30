package com.example.opsc_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGoToDash = findViewById<Button>(R.id.btnSignIn)
        val signUpClick = findViewById<TextView>(R.id.tvSignUp)


        btnGoToDash.setOnClickListener{

            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)

        }

        signUpClick.setOnClickListener{

            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.add(R.id.relContainer, SignUp())
            transaction.commit()

        }

    }
}