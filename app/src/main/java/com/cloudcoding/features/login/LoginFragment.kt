package com.cloudcoding.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudcoding.R
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.projects_fragment.*

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signup_button.setOnClickListener {
                findNavController()
                .navigate(R.id.action_loginFragment_to_signupFragment)
        }
        login_button.setOnClickListener {
            findNavController()
                .navigate(R.id.action_loginFragment_to_mainMenuFragment)
        }
    }
}