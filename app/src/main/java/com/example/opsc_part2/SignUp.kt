package com.example.opsc_part2

//this line is reqired
//FragmentSignUpBinding is auto generated
import Classes.ActiveUserClass
import Classes.PasswordHandler
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
import androidx.fragment.app.Fragment
import com.example.opsc_part2.databinding.FragmentSignUpBinding
import java.util.jar.Attributes.Name


class SignUp : Fragment(R.layout.fragment_sign_up) {

    //bind things to things
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    //inputs
    private lateinit var NameInput: EditText
    private lateinit var SurnameInput: EditText
    private lateinit var UsernameInput: EditText
    private lateinit var PasswordInput: EditText
    private lateinit var ConfirmPasswordInput: EditText

    //pressables
    private lateinit var tvSignInClick: TextView
    private lateinit var btnSignUp: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        NameInput = binding.etName
        SurnameInput = binding.etSurname
        UsernameInput = binding.etUsername
        PasswordInput = binding.etPassword
        ConfirmPasswordInput = binding.etConfirmPassword

        btnSignUp = binding.btnSignUp
        btnSignUp.setOnClickListener()
        {
            if (validateForm())
                RegisterUser()
        }

        tvSignInClick = binding.tvSignIn
        tvSignInClick.setOnClickListener()
        {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    //needed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //custom method
    private fun RegisterUser() {

        val toast = Toast.makeText(requireContext(), "message", Toast.LENGTH_SHORT)
        toast.show()

        //save data
        ActiveUserClass.UserName = NameInput.text.toString().trim()
        ActiveUserClass.UserSurname = SurnameInput.text.toString().trim()
        ActiveUserClass.UserUsername = UsernameInput.text.toString().trim()
        ActiveUserClass.UserPasswordHash =
            PasswordHandler.hashPassword(PasswordInput.text.toString().trim())

        var a = 0;
    }

    private fun validateForm(): Boolean {
        var valid = true
        val name: String = NameInput.getText().toString().trim()
        val surname: String = SurnameInput.getText().toString().trim()
        val username: String = UsernameInput.getText().toString().trim()
        val password: String = PasswordInput.getText().toString().trim()
        val confirmPassword: String = ConfirmPasswordInput.getText().toString().trim()

        if (TextUtils.isEmpty(password)) {
            PasswordInput.setError("Password is required")
            valid = false
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            ConfirmPasswordInput.setError("Confirm password is required")
            valid = false
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            ConfirmPasswordInput.setError("Passwords must match")
            valid = false
        }
        return valid
    }

}