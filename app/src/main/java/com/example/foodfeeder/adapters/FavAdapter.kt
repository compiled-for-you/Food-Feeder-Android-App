package com.example.foodfeeder.adapters

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodfeeder.R
import com.example.foodfeeder.database.RestaurantEntity
import com.example.foodfeeder.database.RestaurantsDatabase
import com.example.foodfeeder.fragments.FavFragment
import com.example.foodfeeder.fragments.OneRestaurantFragment
import com.squareup.picasso.Picasso

class FavAdapter (val context : Context, val favList: List<RestaurantEntity> ) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    class FavViewHolder (view : View): RecyclerView.ViewHolder(view)
    {
        val imgres : ImageView = view.findViewById(R.id.imgrestaurant)
        val imgsetfav : ImageView = view.findViewById(R.id.imgsetfav)
        val tvresname : TextView = view.findViewById(R.id.tvresname)
        val tvresprice : TextView = view.findViewById(R.id.tvresprice)
        val tvrating : TextView = view.findViewById(R.id.tvrating)
        val cardview : CardView = view.findViewById(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_row, parent, false )
        return FavViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val restaurant = favList[position]
        holder.tvresname.text = restaurant.res_name
        holder.tvresprice.text = "${restaurant.res_cost}/person"
        holder.tvrating.text = restaurant.res_rating
        Picasso.get().load(restaurant.res_image).error(R.drawable.ic_launcher_background).into(holder.imgres)
        holder.imgsetfav.setImageResource(R.drawable.ic_heart_fav)

        holder.imgsetfav.setOnClickListener {
            val clickedRestaurant = RestaurantEntity(
                restaurant.res_id,
                restaurant.res_name,
                restaurant.res_rating,
                restaurant.res_cost,
                restaurant.res_image
            )

            val checkFav = FavAsyncTask(context, clickedRestaurant, 1)
            val isFav = checkFav.execute().get()

            if(isFav)
            {
                FavAsyncTask(context, clickedRestaurant, 3).execute().get()
                holder.imgsetfav.setImageResource(R.drawable.ic_heart_set_fav)
            }
            else
            {
                FavAsyncTask(context, clickedRestaurant, 2).execute().get()
                holder.imgsetfav.setImageResource(R.drawable.ic_heart_fav)
            }
        }
        holder.cardview.setOnClickListener {
            val fragment = OneRestaurantFragment()
            val args = Bundle()
            args.putString("clickedId", restaurant.res_id)
            args.putString("clickedName", restaurant.res_name)
            fragment.arguments = args
            (context as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
            (context as AppCompatActivity).supportActionBar?.title = holder.tvresname.text.toString()
        }
    }
}
class FavAsyncTask (val context : Context, val restaurantEntity: RestaurantEntity, val mode : Int ) : AsyncTask<Void, Void, Boolean>()
{
    val db = Room.databaseBuilder(context, RestaurantsDatabase::class.java,  "FavRestaurants").build()
    /**
     * Mode 1-> check db if the restaurant is fav or not
     * Mode 2-> add restaurant to database
     * Mode 3-> delete restaurant from database*/
    override fun doInBackground(vararg params: Void?): Boolean {
        when (mode)
        {
            1 ->
            {
                val restaurant : RestaurantEntity?  = db.resDao().checkRestaurant(restaurantEntity.res_id)
                db.close()
                return restaurant!= null
            }
            2 ->
            {
                db.resDao().insertRestaurant(restaurantEntity)
                db.close()
                return true
            }
            3 ->
            {
                db.resDao().deleteRestaurant(restaurantEntity)
                db.close()
                return true
            }
        }
        return false
    }

}