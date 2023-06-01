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
    private lateinit var btnSignIn: Button
    private lateinit var signUpClick: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignIn = findViewById(R.id.btnSignIn)
        signUpClick = findViewById(R.id.tvSignUp)

        UsernameInput = findViewById(R.id.etUsername)
        PasswordInput = findViewById(R.id.etPassword)

        btnSignIn.setOnClickListener {
            UserLogin()
        }

        signUpClick.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.add(R.id.relContainer, SignUp())
            transaction.commit()
        }
    }

    fun UserLogin() {

        //default login
        if (UsernameInput.text.toString().trim().equals("user") && PasswordInput.text.toString().trim().equals("pass")) {
            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            return
        }

        //if username does not exist
        val user = ToolBox.UsersList.find { it.UserUsername == UsernameInput.text.toString().trim() }
        if (user == null) {
            val errToast = Toast.makeText(
                applicationContext, "Incorrect username or password", Toast.LENGTH_LONG
            )
            errToast.setGravity(Gravity.BOTTOM, 0, 25)
            errToast.show()
            return
        }

        //check password
        if (user.UserPasswordHash == PasswordHandler.hashPassword(
                PasswordInput.text.toString().trim()
            )
        ) {
            intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        } else {
            val errToast = Toast.makeText(
                applicationContext, "Incorrect username or password", Toast.LENGTH_LONG
            )
            errToast.setGravity(Gravity.BOTTOM, 0, 25)
            errToast.show()
        }
    }
}

/*
To do
    sign n sign up
        move to sing in after signed up
 */