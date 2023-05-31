package com.example.opsc_part2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

//import com.example.opsc_part2.databinding.SignInFragmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


class SignUp : Fragment(R.layout.fragment_sign_up) {

    private var _binding: SignUp? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var NameInput: EditText
    private lateinit var SurnameInput: EditText
    private lateinit var UsernameInput: EditText
    private lateinit var PasswordInput: EditText
    private lateinit var ConfirmPasswordInput: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvSignInClick: TextView

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val view = inflater.inflate(R.layout.fragment_quick_action_popup, container, false)


        //  _binding = SignUp.inflate(inflater, container, false)
        // val view = binding.root


        // Find the buttons in the inflated view
        //  tvSignInClick = view.findViewById(R.id.btnCreateActivity)

//        NameInput = findViewById(R.id.etName)
//        SurnameInput = findViewById(R.id.etSurname)
//        UsernameInput = findViewById(R.id.etUsername)
//        PasswordInput = findViewById(R.id.etPassword)
//        ConfirmPasswordInput = findViewById(R.id.etConfirmPassword)

        binding.tvSignInClick.setOnClickListener {
//            //test code to show the create goal page
//            var intent = Intent(activity, Create_Goal::class.java)
//            startActivity(intent)
        }

        return view
    }
}

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SignUp.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SignUp().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
