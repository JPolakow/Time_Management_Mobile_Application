package com.example.opsc_part2

import Classes.ActiveUserClass
import Classes.PasswordHandler
import Classes.ToolBox
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserSignUp : AppCompatActivity() {

    //Inputs
    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText

    //Press ables
    private lateinit var tvSignInClick: TextView
    private lateinit var btnSignUp: Button

    //============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)


        try {
            nameInput = findViewById(R.id.etName)
            surnameInput = findViewById(R.id.etSurname)
            usernameInput = findViewById(R.id.etUsername)
            passwordInput = findViewById(R.id.etPassword)
            confirmPasswordInput = findViewById(R.id.etConfirmPassword)

            btnSignUp = findViewById(R.id.btnSignUp)
            btnSignUp.setOnClickListener() {
                if (validateForm()) {
                    RegisterUser()
                    intentToSignIn()
                }
            }

            tvSignInClick = findViewById(R.id.tvSignIn)
            tvSignInClick.setOnClickListener() {
                intentToSignIn()
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //take user inputs and create new user instance
    // atraverso2001 Password1!
    private fun RegisterUser() {
       // val db = Firebase.firestore
        try {
            val activeUserClass = ActiveUserClass(
                nameInput.text.toString().trim(),
                surnameInput.text.toString().trim(),
                usernameInput.text.toString().trim(),
                PasswordHandler.hashPassword(passwordInput.text.toString().trim())
            )

//            createAccount()

            /*val user = hashMapOf(
                "name" to nameInput.text.toString().trim(),
                "surname" to surnameInput.text.toString().trim(),
                "username" to usernameInput.text.toString().trim(),
                "password" to PasswordHandler.hashPassword(passwordInput.text.toString().trim())
            )
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
*/
            ToolBox.UsersList.add(activeUserClass)

            val toast = Toast.makeText(this, "Account created", Toast.LENGTH_SHORT)
            toast.show()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Method to start intent activity to sign in
    private fun intentToSignIn() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
            ActivityCompat.startActivity(this, intent, options.toBundle())
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Ensure user has inputted valid data
    private fun validateForm(): Boolean {
        var valid = true
        try {
            val name: String = nameInput.text.toString().trim()
            val surname: String = surnameInput.text.toString().trim()
            val username: String = usernameInput.text.toString().trim()
            val password: String = passwordInput.text.toString().trim()
            val confirmPassword: String = confirmPasswordInput.text.toString().trim()

            val minLength = 8
            val maxLength = 50
            val hasUpperCase = "[A-Z]".toRegex().containsMatchIn(password)
            val hasLowerCase = "[a-z]".toRegex().containsMatchIn(password)
            val hasDigit = "\\d".toRegex().containsMatchIn(password)
            val hasSpecialChar = "[^A-Za-z0-9]".toRegex().containsMatchIn(password)

            if (TextUtils.isEmpty(name)) {
                nameInput.error = "Name is required"
                valid = false
            }
            if (TextUtils.isEmpty(surname)) {
                surnameInput.error = "Surname is required"
                valid = false
            }
            if (TextUtils.isEmpty(username)) {
                usernameInput.error = ("Username is required")
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
            if (!(password.length in minLength..maxLength &&
                        hasUpperCase &&
                        hasLowerCase &&
                        hasDigit &&
                        hasSpecialChar)
            ) {
                passwordInput.error = ("Password is not strong enough.")
                valid = false
            }


            return valid
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return false
        }
    }

    //============================================================================
    // Try find if the existing username exists
    private fun doesUsernameExist(NameToFind: String): Boolean {
        try {
            val person = ToolBox.UsersList.find { it.UserUsername == NameToFind }
            return person != null
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return true
        }
    }

    private fun createAccount(email: String, password:String)
    {
        MainActivity.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = MainActivity.auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //updateUI(null)
                }
            }


    }
}