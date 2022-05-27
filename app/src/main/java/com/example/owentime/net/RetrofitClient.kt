package com.example.owentime.net

import com.example.owentime.base.BaseRetrofitClient
import okhttp3.OkHttpClient

object RetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(ApiService::class.java, ApiService.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) = Unit

}