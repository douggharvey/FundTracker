package com.douglasharvey.fundtracker3.utilities

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    // Reference: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
