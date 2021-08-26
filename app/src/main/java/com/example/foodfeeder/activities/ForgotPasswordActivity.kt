package com.example.foodfeeder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.foodfeeder.R
import com.example.foodfeeder.fragments.Forgot1Fragment

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var frame : FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        frame = findViewById(R.id.frame)
        supportFragmentManager.beginTransaction().replace(R.id.frame, Forgot1Fragment()).commit()
    }
}