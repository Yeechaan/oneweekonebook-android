package com.lee.oneweekonebook.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


fun isNetworkConnected(context: Context?) = run {
    val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    networkCapabilities?.let {
        when {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } ?: false
}