package com.example.opsc_part2

import Classes.ActiveUserClass
import Classes.PasswordHandler
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

//this line is reqired
//FragmentSignUpBinding is auto generated
import com.example.opsc_part2.databinding.FragmentSignUpBinding

class SignUp : Fragment(R.layout.fragment_sign_up) {

    //bind things to things
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    //    private lateinit var NameInput: EditText
//    private lateinit var SurnameInput: EditText
//    private lateinit var UsernameInput: EditText
//    private lateinit var PasswordInput: EditText
//    private lateinit var ConfirmPasswordInput: EditText
    private lateinit var tvSignInClick: TextView

    private lateinit var btnSignUp: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        btnSignUp = binding.btnSignUp
        btnSignUp.setOnClickListener()
        {
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
        ActiveUserClass.UserName = binding.etName.text.toString()
        ActiveUserClass.UserSurname = binding.etSurname.text.toString()
        ActiveUserClass.UserUsername = binding.etUsername.text.toString()
        ActiveUserClass.UserPasswordHash = PasswordHandler.hashPassword(binding.etPassword.text.toString())

        var a = 0;
    }
}