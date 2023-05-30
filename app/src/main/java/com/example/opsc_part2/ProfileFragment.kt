package com.example.opsc_part2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

// Here ":" symbol is indicate that LoginFragment
// is child class of Fragment Class
class ProfileFragment
    : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_profile_settings, container, false
        )
    }
    // Here "layout_login" is a name of layout file
    // created for LoginFragment
}
