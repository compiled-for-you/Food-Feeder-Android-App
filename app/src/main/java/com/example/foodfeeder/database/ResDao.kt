package com.example.foodfeeder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResDao {

    @Insert
    fun insertRestaurant (restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestaurant (restaurantEntity: RestaurantEntity)

    @Query ("SELECT * FROM FavRestaurants")
    fun getAllRestaurants () : List<RestaurantEntity>

    @Query ("SELECT * From FavRestaurants WHERE res_id = :bookId")
    fun checkRestaurant (bookId : String) : RestaurantEntity

}