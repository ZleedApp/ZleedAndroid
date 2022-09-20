package com.zleed.app.classes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar

class NetworkUtils {
    /*
     * isOnline(context: Context): Boolean
     *
     * https://stackoverflow.com/a/57237708
     */
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("NetworkUtils", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("NetworkUtils", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("NetworkUtils", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    fun checkInternetWithSnackbar(context: Context, view: View, message: String) {
        if(!this.isOnline(context))
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Retry") {
                    checkInternetWithSnackbar(context, view, message)
                }.show()
    }
}