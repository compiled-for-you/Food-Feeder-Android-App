package com.example.foodfeeder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName ="FavRestaurants")

data class RestaurantEntity (
    @PrimaryKey                            var res_id : String,
    @ColumnInfo (name = "res_name")        var res_name : String,
    @ColumnInfo (name = "res_rating")      var res_rating : String,
    @ColumnInfo (name = "res_cost")        var res_cost : String,
    @ColumnInfo (name = "res_image")       var res_image : String

)