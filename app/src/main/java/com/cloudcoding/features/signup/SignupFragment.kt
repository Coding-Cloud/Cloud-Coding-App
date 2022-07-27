package com.cloudcoding.features.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cloudcoding.R
import kotlinx.android.synthetic.main.signup_fragment.*

class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signup_button.setOnClickListener {
            findNavController()
                .navigate(R.id.action_signupFragment_to_loginFragment)
        }
        login_button.setOnClickListener {
            findNavController()
                .navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}