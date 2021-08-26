package com.example.foodfeeder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfeeder.R
import com.example.foodfeeder.module.FoodItem
import kotlinx.android.synthetic.main.one_food_row.view.*

class OneRestaurantAdapter (val context: Context, val menuList: List<FoodItem>) : RecyclerView.Adapter<OneRestaurantAdapter.OneRestaurantViewHolder>() {
    class OneRestaurantViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val tvSerial : TextView = view.findViewById(R.id.tvSerial)
        val tvFoodName : TextView = view.findViewById(R.id.tvFoodName)
        val tvFoodPrice : TextView = view.findViewById(R.id.tvFoodPrice)
        val btnFood : Button = view.findViewById(R.id.btnFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneRestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_food_row, parent, false)
        return OneRestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: OneRestaurantViewHolder, position: Int) {
        val item = menuList[position]
        holder.tvSerial.text = (position+1).toString()
        holder.tvFoodName.text = item.food_name
        holder.tvFoodPrice.text = item.food_price
    }

}