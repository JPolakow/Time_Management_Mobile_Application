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





    //ui vars
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var btnSignIn: Button
    private lateinit var signUpClick: TextView

    //============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignIn = findViewById(R.id.btnSignIn)
        signUpClick = findViewById(R.id.tvSignUp)

        usernameInput = findViewById(R.id.etUsername)
        passwordInput = findViewById(R.id.etPassword)

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

    //============================================================================
    fun UserLogin() {

        //if username does not exist
        val user = ToolBox.UsersList.find { it.UserUsername == usernameInput.text.toString().trim() }
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
                passwordInput.text.toString().trim()
            )
        ) {
            ToolBox.ActiveUserID = ToolBox.UsersList.indexOfFirst { it.UserUsername == user.UserUsername }

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
        signup is borked
    add activity
        goal must populate textview
    profile
        make not suck
 */