package com.example.avniproject.backendandroidconnection

import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApi {
    @POST("api/orders/{productId}")
    suspend fun placeOrder(@Path("productId") productId: Long): Response<String>

    @PUT("api/orders/add-stock/{productId}/{quantity}")
    suspend fun addStock(
        @Path("productId") productId: Long,
        @Path("quantity") quantity: Int
    ): Response<String>

    @GET("api/orders/stock/{productId}")
    suspend fun getStock(@Path("productId") productId: Long): Response<Int>

    object RetrofitClient {
        private const val BASE_URL = "http://10.0.2.2:8080/"

        val api: OrderApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrderApi::class.java)
        }
    }
}
