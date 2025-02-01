package com.example.pic_a_day_frontend

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pic_a_day_frontend.User.LoginRequest
import com.example.pic_a_day_frontend.User.NewUserInfo
import com.example.pic_a_day_frontend.ui.theme.PicaDayFrontendTheme
import com.example.pic_a_day_frontend.utils.RetrofitInstance
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicaDayFrontendTheme {
                var message = intent.getStringExtra("message")
                if (message == null) message = ""
                FirebaseApp.initializeApp(this)
                LoginUI(message)
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition", "CommitPrefEdits")
    @Composable
    fun LoginUI(message: Any) {
        if (message != "") {
            Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
        }
        val username = remember { mutableStateOf("binaya") }
        val password = remember { mutableStateOf("binaya") }
        val login = remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        if (login.value) {
            login.value=false
            val body = LoginRequest(
                username = username.value,
                password = password.value
            )
            coroutineScope.launch {
                try {
                    val res = RetrofitInstance.api.loginUser(body) // res is a Response<MyResponse>
                    if (res.isSuccessful) {
//                        this@Login.getPreferences(Context.MODE_PRIVATE).edit().putString("username", username.value.toString())
                        getSharedPreferences("Pic-a-Day", Context.MODE_PRIVATE).edit().putString("username", username.value).apply()
                        getSharedPreferences("Pic-a-Day", Context.MODE_PRIVATE).edit().putString("token", res.body()!!.token).apply()
                        println("Response Body: ${res.body()}") // Prints the parsed response body

                        val intent = Intent(this@Login, HomePage::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@Login, "Invalid Username or password!", Toast.LENGTH_LONG).show()
                        println("Error: ${res.errorBody()?.string()}") // Prints the error message
                    }
                } catch (e: Exception) {
                    println("Exception: $e") // Logs any exceptions
                }
            }

        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login to Pic-a-Day",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,)
            Spacer(Modifier.padding(10.dp))
            TextField(value = username.value, onValueChange = {username.value = it}, label = { Text("Username") },
                modifier = Modifier.padding(10.dp))
            TextField(value = password.value, onValueChange = {password.value = it},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.padding(10.dp))

            Button(onClick = {login.value = true}, shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(20.dp)) {
                Text("Login")
            }

            OutlinedButton(onClick = {
                startActivity(Intent(this@Login, CreateUser::class.java))
            }) {
                Text("Create User")
            }
        }
    }


}