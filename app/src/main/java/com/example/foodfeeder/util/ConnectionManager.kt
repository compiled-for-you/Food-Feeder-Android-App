package com.example.foodfeeder.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun checkConnectivity (context : Context) : Boolean {
        val connectivitymanager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetwork : NetworkInfo? = connectivitymanager.activeNetworkInfo

        if(activenetwork?.isConnected != null)
        {
            return activenetwork.isConnected
        }
        else
        {
            return false
        }
    }
}