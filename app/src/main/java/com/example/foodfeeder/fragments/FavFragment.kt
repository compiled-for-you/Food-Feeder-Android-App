package com.example.foodfeeder.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodfeeder.R
import com.example.foodfeeder.adapters.FavAdapter
import com.example.foodfeeder.database.RestaurantEntity
import com.example.foodfeeder.database.RestaurantsDatabase
import com.example.foodfeeder.module.Restaurants

class FavFragment : Fragment() {

    lateinit var favrecycler : RecyclerView
    lateinit var progressbarlayout : LinearLayout
    lateinit var layoutmanager : LinearLayoutManager
    var favList = listOf<RestaurantEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
        favrecycler = view.findViewById(R.id.favrecycler)
        progressbarlayout = view.findViewById(R.id.progressbarlayout)
        progressbarlayout.visibility = View.VISIBLE
        layoutmanager = LinearLayoutManager(activity as Context)
        favList = RetrieveFavRestaurants(activity as Context).execute().get()
        if(activity!=null)
        {
            progressbarlayout.visibility = View.GONE
            val myadapter = FavAdapter(activity as Context, favList)
            favrecycler.adapter = myadapter
            favrecycler.layoutManager = layoutmanager
        }
        return view
    }

    class RetrieveFavRestaurants (val context : Context) : AsyncTask<Void, Void, List<RestaurantEntity>>()
    {
        val db = Room.databaseBuilder(context, RestaurantsDatabase::class.java, "FavRestaurants").build()
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            return db.resDao().getAllRestaurants()
        }
    }
}