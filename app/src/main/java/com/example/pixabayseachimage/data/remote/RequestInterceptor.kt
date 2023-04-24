package com.example.pixabayseachimage.data.remote

import android.content.Context
import com.example.pixabayseachimage.BuildConfig
import com.example.pixabayseachimage.utils.isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY).build()
        val requestBuilder = request()
            .newBuilder()
        requestBuilder.method(request().method, request().body)
            .url(url)

        if (isInternetAvailable(context)) {
            requestBuilder.header("Cache-Control", "public, max-age=" + 60)
        } else {
            requestBuilder.header(
                "Cache-Control",
                "public, only-if-cached, max-stale=${(60 * 60 * 24 * 7)}"
            )
        }
        proceed(requestBuilder.build())
    }
}