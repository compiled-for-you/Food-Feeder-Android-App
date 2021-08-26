package com.example.foodfeeder.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.foodfeeder.R

class foodfeedersplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodfeedersplash)

        Handler().postDelayed(
            {
                val startLogin = Intent (this@foodfeedersplash, LoginActivity::class.java)
                startActivity(startLogin)
                finish()
            },
        2000)
    }
}