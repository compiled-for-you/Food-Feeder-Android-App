package com.example.foodfeeder.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodfeeder.R
import com.example.foodfeeder.adapters.HomeAdapter
import com.example.foodfeeder.module.Restaurants
import com.example.foodfeeder.util.ConnectionManager
import org.json.JSONException

class HomeFragment : Fragment() {
    lateinit var homerecycler : RecyclerView
    lateinit var layout : LinearLayoutManager
    lateinit var progressbarlayout : LinearLayout
    lateinit var myadapter : HomeAdapter
    var reslist = arrayListOf<Restaurants>()

    companion object {
        @SuppressLint("StaticFieldLeak")
        var clickedId: String? = ""
        var clickedName: String? = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homerecycler = view.findViewById(R.id.homerecycler)
        progressbarlayout = view.findViewById(R.id.progressbarlayout)
        progressbarlayout.visibility = View.VISIBLE
        layout = LinearLayoutManager(activity)

            if(ConnectionManager().checkConnectivity(activity as Context)){
                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            progressbarlayout.visibility = View.GONE
                            val resarray = data.getJSONArray("data")
                            for(i in 0 until resarray.length())
                            {

                                val oneresdetail = resarray.getJSONObject(i)
                                val restaurantobject = Restaurants(
                                    oneresdetail.getString("id"),
                                    oneresdetail.getString("name"),
                                    oneresdetail.getString("rating"),
                                    oneresdetail.getString("cost_for_one"),
                                    oneresdetail.getString("image_url")
                                )
                                reslist.add(restaurantobject)
                            }
                            myadapter = HomeAdapter(activity as Context, reslist)
                            homerecycler.adapter = myadapter
                            homerecycler.layoutManager = layout

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    catch (e : JSONException)
                    {
                        Toast.makeText(activity as Context, e.message , Toast.LENGTH_SHORT).show()
                    }

                },
                Response.ErrorListener {
                    Toast.makeText(activity as Context, "error listenr me ho tum", Toast.LENGTH_SHORT).show()
                })
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "743dc78972a7a2"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }
        else
            {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("No connection available !")
                dialog.setPositiveButton("Open Settings"){text, listener ->
                    val toSettings = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
                    startActivity(toSettings)
                    activity?.finish()
                }
                dialog.setNegativeButton("Exit App"){text, listener ->
                    ActivityCompat.finishAffinity(activity as Activity)
                }
                dialog.create().show()
            }

        return view
    }
    
}