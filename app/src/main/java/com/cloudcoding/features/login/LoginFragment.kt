package com.cloudcoding.features.login

import android.content.Context
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
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.LoginRequest
import com.cloudcoding.features.conversations.ConversationAdapter
import kotlinx.android.synthetic.main.conversations_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.projects_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            val username = username.text.toString()
            val password = password.text.toString()
            GlobalScope.launch(Dispatchers.Default) {
                val token = CloudCodingNetworkManager.login(LoginRequest(username, password)).accessToken
                val sharedPref = context?.getSharedPreferences(
                    getString(R.string.token),
                    Context.MODE_PRIVATE)!!
                with (sharedPref.edit()) {
                    putString(getString(R.string.token), token)
                    commit()
                }
                withContext(Dispatchers.Main) {
                    findNavController()
                        .navigate(R.id.action_loginFragment_to_mainMenuFragment)
                }
            }
        }
    }
}