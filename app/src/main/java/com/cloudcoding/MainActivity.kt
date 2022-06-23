package com.cloudcoding

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cloudcoding.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mInstance = this
    }

    companion object {
        lateinit var mInstance: MainActivity
        fun getContext(): Context {
            return mInstance.applicationContext
        }
    }
}