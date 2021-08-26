package com.example.foodfeeder.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database (entities = [RestaurantEntity::class], version = 1)
abstract class RestaurantsDatabase : RoomDatabase() {

    abstract fun resDao () : ResDao

}