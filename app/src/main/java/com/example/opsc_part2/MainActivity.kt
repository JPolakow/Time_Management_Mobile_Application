package com.example.opsc_part2

import Classes.ActiveUserClass
import Classes.PasswordHandler
import Classes.ToolBox
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //UI vars
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var btnSignIn: Button
    private lateinit var signUpClick: TextView

    //============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_main)

            btnSignIn = findViewById(R.id.btnSignIn)
            signUpClick = findViewById(R.id.tvSignUp)

            usernameInput = findViewById(R.id.etUsername)
            passwordInput = findViewById(R.id.etPassword)

            btnSignIn.setOnClickListener {
                userLogin()
            }

            signUpClick.setOnClickListener {
                             val intent = Intent(this, UserSignUp::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
                ActivityCompat.startActivity(this, intent, options.toBundle())
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    companion object {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
    }

    /*fun verifyUserCredentials(email: String, password: String) {
        auth.signInWith(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User credentials are correct
                    println("User credentials are correct.")
                    val user = auth.currentUser
                    println("Logged in user: ${user?.email}")
                } else {
                    // An error occurred or credentials are incorrect
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            // User does not exist
                            println("User does not exist.")
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Invalid password
                            println("Invalid password.")
                        }
                        else -> {
                            // Other error occurred
                            println("Error: ${task.exception?.message}")
                        }
                    }
                }
            }
    }*/
    //============================================================================
    private fun writeToDB()
    {
        val db = Firebase.firestore
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

    //============================================================================
    private fun readFromDB()
    {
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
    //============================================================================
    private fun userLogin() {
        try {
            //If username does not exist
            val user =
                ToolBox.UsersList.find { it.UserUsername == usernameInput.text.toString().trim() }
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
                ToolBox.ActiveUserID =
                    ToolBox.UsersList.indexOfFirst { it.UserUsername == user.UserUsername }

                intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
            } else {
                val errToast = Toast.makeText(
                    applicationContext, "Incorrect username or password", Toast.LENGTH_LONG
                )
                errToast.setGravity(Gravity.BOTTOM, 0, 25)
                errToast.show()
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }


}