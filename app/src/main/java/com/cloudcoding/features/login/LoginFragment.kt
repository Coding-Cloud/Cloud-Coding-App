package com.cloudcoding.features.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cloudcoding.R
import com.cloudcoding.api.CloudCodingNetworkManager
import com.cloudcoding.api.request.LoginRequest
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.*
import retrofit2.HttpException

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, parent, false)
    }

    private var jobs: MutableList<Job> = mutableListOf()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { job ->
            job.cancel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = context?.getSharedPreferences(
            getString(R.string.token),
            Context.MODE_PRIVATE
        )!!
        val savedToken = sharedPref.getString(getString(R.string.token), "")
        if (savedToken != null && savedToken != "") {
            findNavController()
                .navigate(R.id.action_loginFragment_to_mainMenuFragment)
        }
        signup_button.setOnClickListener {
            findNavController()
                .navigate(R.id.action_loginFragment_to_signupFragment)
        }
        login_button.setOnClickListener {
            login_button.isEnabled = false
            val username = username.text.toString()
            val password = password.text.toString()
            jobs.add(GlobalScope.launch(Dispatchers.Default) {
                var loginFailed = false
                try {
                    val token =
                        CloudCodingNetworkManager.login(
                            LoginRequest(
                                username,
                                password
                            )
                        ).accessToken
                    with(sharedPref.edit()) {
                        putString(getString(R.string.token), token)
                        commit()
                    }
                } catch (e: HttpException) {
                    e.printStackTrace()
                    loginFailed = true
                }

                withContext(Dispatchers.Main) {
                    if (!loginFailed) {
                        val sharedPrefMe = context?.getSharedPreferences(
                            getString(R.string.me),
                            Context.MODE_PRIVATE
                        )!!
                        val userId = CloudCodingNetworkManager.getMe().id
                        with(sharedPrefMe.edit()) {
                            putString(getString(R.string.me), userId)
                            commit()
                        }
                        findNavController()
                            .navigate(R.id.action_loginFragment_to_mainMenuFragment)
                    }
                    login_button.isEnabled = true
                    error.visibility = View.VISIBLE
                    return@withContext
                }
            })
        }
    }
}