package com.example.foodfeeder.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodfeeder.R
import com.example.foodfeeder.activities.ForgotPasswordActivity
import org.json.JSONException
import org.json.JSONObject

class Forgot1Fragment : Fragment() {
    lateinit var etNumber : EditText
    lateinit var etMail : EditText
    lateinit var btnNext : Button
    lateinit var progressBarLayout : RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot1, container, false)
        etNumber = view.findViewById(R.id.etNumber)
        etMail = view.findViewById(R.id.etMail)
        btnNext = view.findViewById(R.id.btnNext)
        progressBarLayout = view.findViewById(R.id.progressbarlayout)

        btnNext.setOnClickListener {
            progressBarLayout.visibility = View.VISIBLE
            val number = etNumber.text.toString()
            val mail = etMail.text.toString()
            if(mail.isEmpty() || number.isEmpty())
            {
                progressBarLayout.visibility = View.GONE
                Toast.makeText(activity as Context, "Fill out both the fields !", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", number)
                jsonParams.put("email", mail)
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                Response.Listener {
                    try{
                        progressBarLayout.visibility = View.GONE
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success)
                            {
                        val fragment = Forgot2Fragment()
                        val args = Bundle()
                        args.putString("number", number)
                        fragment.arguments = args
                        Toast.makeText(activity as Context, "OTP has been sent to your registered email", Toast.LENGTH_LONG).show()
                        (context as ForgotPasswordActivity).supportFragmentManager.beginTransaction().replace(R.id.frame, fragment )
                            .commit()
                            }
                       }
                    catch(e : JSONException)
                    {
                        progressBarLayout.visibility = View.GONE
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

        return view
    }
}
