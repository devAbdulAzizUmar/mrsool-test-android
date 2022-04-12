package com.example.mrsooltest.interfaces

import com.example.mrsooltest.models.Billionaire
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BillionairesApi {
    @GET("/api/forbes400")
     fun getBillionaires(@Query("limit") limit: Int = 100) : Call<List<Billionaire>>
}