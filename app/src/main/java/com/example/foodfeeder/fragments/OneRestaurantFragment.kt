package com.example.foodfeeder.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodfeeder.R
import com.example.foodfeeder.adapters.OneRestaurantAdapter
import com.example.foodfeeder.module.FoodItem
import org.json.JSONException

class OneRestaurantFragment : Fragment() {

    lateinit var onerestaurantrecycler : RecyclerView
    lateinit var linearLayoutManager : LinearLayoutManager
    lateinit var progressBarLayout : LinearLayout
    lateinit var myadapter : OneRestaurantAdapter
    var menuList = arrayListOf<FoodItem>()

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
        val view =  inflater.inflate(R.layout.fragment_one_restaurant, container, false)
        onerestaurantrecycler = view.findViewById(R.id.onerestaurantrecycler)
        linearLayoutManager = LinearLayoutManager(activity)
        progressBarLayout = view.findViewById(R.id.progressbarlayout)
        progressBarLayout.visibility = View.VISIBLE

        clickedId = arguments?.getString("clickedId", "1")
        clickedName = arguments?.getString("clickedName", "Restaurant")

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$clickedId"
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
        Response.Listener{
            try {
                val dataReceived = it.getJSONObject("data")
                val success = dataReceived.getBoolean("success")
                if (success)
                {
                    progressBarLayout.visibility = View.GONE
                    val menu = dataReceived.getJSONArray("data")
                    for(i in 0 until menu.length())
                    {
                        val oneFood = menu.getJSONObject(i)
                        val foodItem = FoodItem(
                            oneFood.getString("id"),
                            oneFood.getString("name"),
                            oneFood.getString("cost_for_one"),
                            oneFood.getString("restaurant_id")
                        )
                        menuList.add(foodItem)
                    }
                    myadapter = OneRestaurantAdapter(activity as Context, menuList)
                    onerestaurantrecycler.adapter = myadapter
                    onerestaurantrecycler.layoutManager = linearLayoutManager
                }
            }
            catch (e : JSONException)
            {
                progressBarLayout.visibility = View.GONE
                Toast.makeText(activity as Context, "Some error occurred, try later", Toast.LENGTH_SHORT).show()
            }
        },
        Response.ErrorListener {
            progressBarLayout.visibility = View.GONE
            Toast.makeText(activity as Context, "Some error occurred", Toast.LENGTH_SHORT).show()
        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "743dc78972a7a2"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        return view
    }

}