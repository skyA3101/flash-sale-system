package com.example.avniproject


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.avniproject.backendandroidconnection.FlashSaleScreen
import com.example.avniproject.backendandroidconnection.SaleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: SaleViewModel = hiltViewModel()
            FlashSaleScreen(viewModel)
        }
    }
}
