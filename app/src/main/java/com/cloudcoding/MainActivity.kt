package com.cloudcoding

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
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
fun TextView.setTextBold(text: String, separator: String = " ") {
    val builder = SpannableStringBuilder(text)
    builder.setSpan(StyleSpan(Typeface.BOLD), 0, text.indexOf(separator) + 1, 0)
    setText(builder)
}