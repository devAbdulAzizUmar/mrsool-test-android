package com.example.mrsooltest

import com.example.mrsooltest.interfaces.BillionairesApi
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val billionairesApi: BillionairesApi =
        Retrofit.Builder().baseUrl("https://forbes400.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BillionairesApi::class.java)


//        Retrofit.Builder().baseUrl("https://forbes400.herokuapp.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build().create(BillionairesApi::class.java)


}