package com.example.foodfeeder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodfeeder.R
import com.example.foodfeeder.activities.ForgotPasswordActivity
import com.example.foodfeeder.activities.LoginActivity
import org.json.JSONException
import org.json.JSONObject



/**
 * A simple [Fragment] subclass.
 * Use the [Forgot2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Forgot2Fragment : Fragment() {
    lateinit var etOtp : EditText
    lateinit var etPassword : EditText
    lateinit var etConfPassword : EditText
    lateinit var btnSubmit : Button
    lateinit var progressBarLayout : RelativeLayout
    lateinit var tvOtp : TextView

    companion object {
        @SuppressLint("StaticFieldLeak")
        var number: String? = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot2, container, false)
        etOtp = view.findViewById(R.id.etOtp)
        etPassword = view.findViewById(R.id.etPassword)
        etConfPassword = view.findViewById(R.id.etConfPassword)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        progressBarLayout = view.findViewById(R.id.progressbarlayout)
        tvOtp = view.findViewById(R.id.tvOtp)

        btnSubmit.setOnClickListener {
            progressBarLayout.visibility = View.VISIBLE
            val otp = etOtp.text.toString()
            val password = etPassword.text.toString()
            val confpassword = etConfPassword.text.toString()
            if(otp.isEmpty() || password.isEmpty() || confpassword.isEmpty())
            {
                progressBarLayout.visibility = View.GONE
                Toast.makeText(activity as Context, "Fill out all the fields!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if( !password.equals(confpassword))
                {
                    progressBarLayout.visibility = View.GONE
                    Toast.makeText(activity as Context, "Password do not match", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    number = arguments?.getString("number")
                    val queue = Volley.newRequestQueue(activity)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", number)
                    jsonParams.put("password", password)
                    jsonParams.put("otp", otp)

                    val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, jsonParams,
                    Response.Listener {
                        progressBarLayout.visibility = View.GONE
                        try
                            {
                                val data = it.getJSONObject("data")
                                val message = data.getString("successMessage")
                                Toast.makeText(activity as Context, "$message" , Toast.LENGTH_SHORT).show()
                                Handler().postDelayed(
                                    {
                                        tvOtp.text = "Getting things ready..."
                                        progressBarLayout.visibility = View.VISIBLE
                                        Toast.makeText(activity as Context, "$message" , Toast.LENGTH_SHORT).show()
                                        val intent = Intent(activity, LoginActivity::class.java )
                                        startActivity(intent)
                                    },
                                    1500)
                            }
                            catch (e : JSONException)
                            {
                                Toast.makeText(activity as Context, e.message, Toast.LENGTH_SHORT).show()
                            }

                    },
                    Response.ErrorListener {
                        progressBarLayout.visibility = View.GONE
                        Toast.makeText(activity as Context, it.message, Toast.LENGTH_SHORT).show()
                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "743dc78972a7a2"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                }
            }
        }
        return view
    }

}