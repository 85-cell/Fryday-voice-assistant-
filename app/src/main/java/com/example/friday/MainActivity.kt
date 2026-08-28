package com.example.friday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.friday.theme.FridayTheme
import com.example.friday.ui.FridayApp
import com.example.friday.viewmodel.FridayViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: FridayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridayTheme {
                FridayApp(viewModel = viewModel)
            }
        }
    }
}
