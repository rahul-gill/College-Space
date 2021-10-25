package com.artisticent.collegespace.data.contestsApi

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response


class NetworkInterceptor(val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailable()){
            throw NoInternetException("No internet connection")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean{
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: Network? = connectivityManager.activeNetwork
        val caps: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)
        return caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }
}

class NoInternetException(message: String): Exception(message)