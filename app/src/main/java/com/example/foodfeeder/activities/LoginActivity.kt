package com.example.foodfeeder.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodfeeder.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etnumber : EditText
    lateinit var etpassword : EditText
    lateinit var tvforgot : TextView
    lateinit var btnlogin : Button
    lateinit var btnsignup : Button
    lateinit var sharedP : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etnumber = findViewById(R.id.etnumber)
        etpassword = findViewById(R.id.etpassword)
        tvforgot = findViewById(R.id.tvforgot)
        btnlogin = findViewById(R.id.btnlogin)
        btnsignup = findViewById(R.id.btnsignup)
        sharedP = getSharedPreferences(getString(R.string.MySharedP), Context.MODE_PRIVATE)

        val checkLoggedIn = sharedP.getBoolean("loggedin", false)
        if(checkLoggedIn)
        {
            val toMainActivity = Intent (this@LoginActivity, MainActivity::class.java)
            startActivity(toMainActivity)
            finish()
        }

        tvforgot.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnlogin.setOnClickListener {
            val number = etnumber.text.toString()
            val password = etpassword.text.toString()
            if (number.isEmpty() || password.isEmpty())
            {
                Toast.makeText(this@LoginActivity, "Fill out all the fields", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", number)
                jsonParams.put("password", password)
                    val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        Response.Listener {
                            val datarecieved = it.getJSONObject("data")
                            val success = datarecieved.getBoolean("success")
                            if (success) {
                                sharedP.edit().putBoolean("loggedin", true).apply()
                                val personData = datarecieved.getJSONObject("data")
                                val personName = personData.getString("name")
                                val personNumber = personData.getString("mobile_number")
                                sharedP.edit().putString("personName", personName).apply()
                                sharedP.edit().putString("personNumber", personNumber).apply()
                                val toMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(toMainActivity)
                                finish()
                            }
                            else
                            {
                               val errorMessage =  datarecieved.getString("errorMessage")
                                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                   Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "743dc78972a7a2"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }
        }

        btnsignup.setOnClickListener {
            val tosignup = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(tosignup)
            etnumber.text = null
            etpassword.text = null
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this@LoginActivity)
    }

}
