package com.example.pic_a_day_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pic_a_day_frontend.ui.theme.PicaDayFrontendTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicaDayFrontendTheme {
                LoginUI()
            }
        }
    }
}

@Composable
fun LoginUI() {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Card {
        Text(text = "Login",
            modifier = Modifier.padding(20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp)
    }
}

