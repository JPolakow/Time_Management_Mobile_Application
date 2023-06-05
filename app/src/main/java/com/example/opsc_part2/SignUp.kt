package com.example.opsc_part2

import Classes.ActiveUserClass
import Classes.PasswordHandler
import Classes.ToolBox
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.opsc_part2.databinding.FragmentSignUpBinding
import java.util.jar.Attributes.Name

class SignUp : Fragment(R.layout.fragment_sign_up) {

    //bind the front end, making it accessible
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    //inputs
    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText

    //press ables
    private lateinit var tvSignInClick: TextView
    private lateinit var btnSignUp: Button

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        nameInput = binding.etName
        surnameInput = binding.etSurname
        usernameInput = binding.etUsername
        passwordInput = binding.etPassword
        confirmPasswordInput = binding.etConfirmPassword

        btnSignUp = binding.btnSignUp
        btnSignUp.setOnClickListener() {
            if (validateForm()) RegisterUser()
        }

        tvSignInClick = binding.tvSignIn
        tvSignInClick.setOnClickListener() {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), 0, 0)
            ActivityCompat.startActivity(requireActivity(), intent, options.toBundle())
        }

        return binding.root
    }

    //============================================================================
    //needed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        val toast = Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT)
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