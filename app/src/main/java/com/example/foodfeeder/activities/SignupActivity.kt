package com.example.foodfeeder.activities

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodfeeder.R
import com.example.foodfeeder.fragments.HomeFragment
import com.example.foodfeeder.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    lateinit var etName : EditText
    lateinit var etMail : EditText
    lateinit var etNumber : EditText
    lateinit var etAddress : EditText
    lateinit var etPassword : EditText
    lateinit var etConfPassword : EditText
    lateinit var toolbar : Toolbar
    lateinit var btnRegister : Button
    lateinit var progressbarlayout : RelativeLayout
    lateinit var sharedP : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etName = findViewById(R.id.etName)
        etMail = findViewById(R.id.etMail)
        etAddress = findViewById(R.id.etAddress)
        etNumber = findViewById(R.id.etNumber)
        etPassword = findViewById(R.id.etPassword)
        etConfPassword = findViewById(R.id.etConfPassword)
        toolbar = findViewById(R.id.toolbar)
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setBackgroundResource(R.drawable.button_inactive_theme)
        progressbarlayout = findViewById(R.id.progressbarlayout)
        progressbarlayout.visibility = View.GONE
        sharedP = getSharedPreferences(getString(R.string.MySharedP), Context.MODE_PRIVATE)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        btnRegister.setOnClickListener {
            progressbarlayout.visibility = View.VISIBLE
            val name = etName.text.toString()
            val mail = etMail.text.toString()
            val number = etNumber.text.toString()
            val address = etAddress.text.toString()
            val pass = etPassword.text.toString()
            val confpass = etConfPassword.text.toString()

            if(name.isEmpty() || mail.isEmpty() || number.isEmpty() || address.isEmpty() ||
                    pass.isEmpty() || confpass.isEmpty())
            {
                progressbarlayout.visibility = View.GONE
                Toast.makeText(this@SignupActivity, "Fill out all the fields !",
                Toast.LENGTH_SHORT).show()
            }
            else
            {
                btnRegister.setBackgroundResource(R.drawable.button_theme)
                if(pass.equals(confpass))
                {
                    val queue = Volley.newRequestQueue(this@SignupActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"
                    val jsonparams = JSONObject()
                    jsonparams.put("name", name)
                    jsonparams.put("email", mail)
                    jsonparams.put("mobile_number", number)
                    jsonparams.put("address", address)
                    jsonparams.put("password", pass)

                    if(ConnectionManager().checkConnectivity(this@SignupActivity))
                    {
                        val jsonObjectRequest = object : JsonObjectRequest (Request.Method.POST,
                        url, jsonparams,

                        Response.Listener {
                            progressbarlayout.visibility = View.GONE
                            try {
                                val datareceived = it.getJSONObject("data")
                                val success = datareceived.getBoolean("success")
                                if (success) {
                                    Toast.makeText(
                                        this@SignupActivity,
                                        "You are registered, start exploring !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val personDetail = datareceived.getJSONObject("data")
                                    val personName = personDetail.getString("name")
                                    val personNumber = personDetail.getString("mobile_number")
                                    sharedP.edit().putString("personName", personName).apply()
                                    sharedP.edit().putString("personNumber", personNumber).apply()
                                    val toMainActivity = Intent(this@SignupActivity, MainActivity::class.java)
                                    startActivity(toMainActivity)
                                    finish()
                                }
                                else {
                                    progressbarlayout.visibility = View.GONE
                                    val errormsg = datareceived.getString("errorMessage")
                                    Toast.makeText(this@SignupActivity, errormsg, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                            catch (e : JSONException)
                            {
                                Toast.makeText(this@SignupActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        },

                        Response.ErrorListener {
                                Toast.makeText(this@SignupActivity, it.message, Toast.LENGTH_SHORT).show()
                        })
                        {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers ["Content-Type"] = "application/json"
                                headers ["token"] = "743dc78972a7a2"
                                return headers
                            }
                        }
                        queue.add(jsonObjectRequest)
                    }
                    else
                    {
                        val dialog = AlertDialog.Builder(this@SignupActivity)
                        dialog.setTitle("No Connection !")
                        dialog.setMessage("Connect to a network ?")
                        dialog.setPositiveButton("Open Settings"){text, listener ->
                            val toSettings = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(toSettings)
                        }
                        dialog.setNegativeButton("Close the App"){text, listener ->
                            ActivityCompat.finishAffinity(this@SignupActivity)
                        }
                        dialog.create().show()
                    }
                }
                else
                {
                    progressbarlayout.visibility = View.GONE
                    Toast.makeText(this@SignupActivity, "Passwords don't match !",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}