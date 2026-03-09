package com.example.avniproject.backendandroidconnection

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject


@HiltViewModel
class SaleViewModel @Inject constructor() : ViewModel() {
    var stockCount by mutableStateOf(0)
    var statusMessage by mutableStateOf("Ready to Buy")

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    init {
        fetchInitialStock()
        connectWebSocket()
    }

    private fun fetchInitialStock() {
        viewModelScope.launch {
            try {
                val response = OrderApi.RetrofitClient.api.getStock(1L)
                val body = response.body()
                Log.d("SALE_DEBUG", "Received from Backend: $body")
                stockCount = response.body() ?: 0
                Log.d("SaleViewModel", "Initial Stock from DB/Redis: $stockCount")
            } catch (e: Exception) {
                Log.e("SaleViewModel", "Initial fetch failed", e)
            }
        }
    }

    private fun connectWebSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url("ws://10.0.2.2:8080/ws-flash-sale")
                .build()

            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d("SALE_SOCKET", "WebSocket Received: $text")
                    viewModelScope.launch(Dispatchers.Main) {
                        try {
                            val numericValue = text.filter { it.isDigit() }.toIntOrNull()
                            if (numericValue != null) {
                                stockCount = numericValue
                                Log.d(
                                    "SALE_SOCKET",
                                    "WebSocket Received: Stock Updated to: $stockCount"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("SALE_SOCKET", "WebSocket Received: Parse Error: ${e.message}")
                        }
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("SALE_SOCKET", "Connection Failed: ${t.message}")
                }
            })
        }
    }

    fun incrementStock(productId: Long, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = OrderApi.RetrofitClient.api.addStock(productId, quantity)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        statusMessage = "Stock Added Successfully! 📦"
                    } else {
                        statusMessage = "Failed to add stock: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                statusMessage = "Admin Connection Error ❌"
            }
        }
    }

    fun buy(productId: Long) {
        viewModelScope.launch {
            try {
                val response = OrderApi.RetrofitClient.api.placeOrder(productId)
                if (response.isSuccessful) {
                    val result = response.body() ?: ""
                    statusMessage =
                        if (result.contains("Success")) "Order Placed! ✅" else "Sold Out1! ❌"
                } else {
                    statusMessage = "Sold Out2! ❌"
                }
            } catch (e: Exception) {
                statusMessage = "Connection Error ❌"
            }
        }
    }

}
