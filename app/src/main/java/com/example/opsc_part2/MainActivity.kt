package com.example.opsc_part2

import Classes.PasswordHandler
import Classes.RetreiveData
import Classes.ToolBox
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
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
                // Calling method to authenticate user credentials with firebase - returning user document ID
                authenticateUserWithFirebase(
                    usernameInput.text.toString().trim(),
                    passwordInput.text.toString().trim()
                )


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

    //============================================================================
    private fun authenticateUserWithFirebase(username: String, password: String) {
        val db = Firebase.firestore

        // Query the users collection for the provided username
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val userDocument = result.documents[0]
                    val storedPassword = userDocument.getString("password")

                    // Compare the stored hashed password with the provided password
                    if (storedPassword != null && verifyPassword(password, storedPassword)) {
                        // Authentication successful
                        val userId = userDocument.id

                        RetreiveData.LoadUserCategories(userId) { categoryCallback ->
                            RetreiveData.LoadActivities(userId) { activityCallback ->
                                RetreiveData.LoadWorkEntries(userId) { workEntriesCallBack ->
                                    if (categoryCallback == "success" && activityCallback == "success" && workEntriesCallBack == "success") {
                                        ToolBox.ActiveUserID = userId

                                        // Perform any necessary actions for a successful login
                                        Log.d(TAG, "Authentication successful. User ID: $userId")

                                        intent = Intent(this, Dashboard::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    } else {
                        // Authentication failed
                        Log.d(TAG, "Authentication failed. Invalid username or password.")
                        val errToast = Toast.makeText(
                            applicationContext, "Incorrect username or password", Toast.LENGTH_LONG
                        )
                        errToast.setGravity(Gravity.BOTTOM, 0, 25)
                        errToast.show()
                    }
                } else {
                    // Authentication failed
                    Log.d(TAG, "Authentication failed. Invalid username or password.")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    // function to verify a users' password using their stored hash password
    private fun verifyPassword(password: String, storedPassword: String): Boolean {
        return PasswordHandler.hashPassword(password.toString().trim()) == storedPassword
    }
}