package com.example.opsc_part2

import Classes.ActiveUserClass
import Classes.PasswordHandler
import Classes.ToolBox
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var UsernameInput: EditText
    private lateinit var PasswordInput: EditText
    private lateinit var btnGoToDash: Button
    private lateinit var signUpClick: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoToDash = findViewById(R.id.btnSignIn)
        signUpClick = findViewById(R.id.tvSignUp)

        UsernameInput = findViewById(R.id.etUsername)
        PasswordInput = findViewById(R.id.etPassword)

        btnGoToDash.setOnClickListener {
           // UserLogin()
            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        signUpClick.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.add(R.id.relContainer, SignUp())
            transaction.commit()
        }
    }

    fun UserLogin() {

        val person =
            ToolBox.UsersList.find { it.UserUsername == UsernameInput.text.toString().trim() }
        if (person == null) {
            val errToast = Toast.makeText(
                applicationContext,
                "Incorrect username or password",
                Toast.LENGTH_LONG
            )
            errToast.setGravity(Gravity.BOTTOM, 0, 25)
            errToast.show()
            return
        }

        //default login
        if (UsernameInput.text.toString() == "user1" && PasswordInput.text.toString() == "password1") {
            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        } else if (person.UserPasswordHash == PasswordHandler.hashPassword(
                PasswordInput.text.toString().trim()
            )
        ) {
            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        } else {
            val errToast = Toast.makeText(
                applicationContext,
                "Incorrect username or password",
                Toast.LENGTH_LONG
            )
            errToast.setGravity(Gravity.BOTTOM, 0, 25)
            errToast.show()
        }
    }
}