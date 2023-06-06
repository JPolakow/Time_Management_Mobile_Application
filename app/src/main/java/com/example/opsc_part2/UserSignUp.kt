package com.example.opsc_part2

import Classes.ActiveUserClass
import Classes.PasswordHandler
import Classes.ToolBox
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat

class UserSignUp : AppCompatActivity() {

    //inputs
    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText

    //press ables
    private lateinit var tvSignInClick: TextView
    private lateinit var btnSignUp: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        nameInput = findViewById(R.id.etName)
        surnameInput = findViewById(R.id.etSurname)
        usernameInput = findViewById(R.id.etUsername)
        passwordInput = findViewById(R.id.etPassword)
        confirmPasswordInput = findViewById(R.id.etConfirmPassword)

        btnSignUp = findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener() {
            if (validateForm()) RegisterUser()
        }

        tvSignInClick = findViewById(R.id.tvSignIn)
        tvSignInClick.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }
    }

    //============================================================================
    //take user inputs and create new user instance
    private fun RegisterUser() {
        val activeUserClass = ActiveUserClass(
            nameInput.text.toString().trim(),
            surnameInput.text.toString().trim(),
            usernameInput.text.toString().trim(),
            PasswordHandler.hashPassword(passwordInput.text.toString().trim())
        )

        ToolBox.UsersList.add(activeUserClass)

        val toast = Toast.makeText(this, "Account created", Toast.LENGTH_SHORT)
        toast.show()
    }

    //============================================================================
    //ensure user has inouted valid data
    private fun validateForm(): Boolean {
        var valid = true
        val name: String = nameInput.getText().toString().trim()
        val surname: String = surnameInput.getText().toString().trim()
        val username: String = usernameInput.getText().toString().trim()
        val password: String = passwordInput.getText().toString().trim()
        val confirmPassword: String = confirmPasswordInput.getText().toString().trim()

        if (TextUtils.isEmpty(name)) {
            nameInput.error = "Name is required"
            valid = false
        }
        if (TextUtils.isEmpty(surname)) {
            surnameInput.error = "Surname is required"
            valid = false
        }
        if (TextUtils.isEmpty(username)) {
            usernameInput.error = ("Password is required")
            valid = false
        }
        if (doesUsernameExist((username))) {
            usernameInput.error = ("Username already exists")
            valid = false
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.error = ("Password is required")
            valid = false
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordInput.error = ("Confirm password is required")
            valid = false
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            confirmPasswordInput.error = ("Passwords must match")
            valid = false
        }
        return valid
    }

    //============================================================================
    //try find if the existing username exists
    private fun doesUsernameExist(NameToFind: String): Boolean {
        val person = ToolBox.UsersList.find { it.UserUsername == NameToFind }
        return person != null
    }
}