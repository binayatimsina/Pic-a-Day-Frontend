package com.example.pic_a_day_frontend

import android.annotation.SuppressLint
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
import com.example.pic_a_day_frontend.User.NewUserInfo

import com.example.pic_a_day_frontend.ui.theme.PicaDayFrontendTheme
import com.example.pic_a_day_frontend.utils.RetrofitInstance
import kotlinx.coroutines.launch


class CreateUser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicaDayFrontendTheme {
                CreateUserUI()
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun CreateUserUI() {
        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val createUser = remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        if (createUser.value) {
            createUser.value=false
            val body = NewUserInfo(
                username = username.value,
                email = email.value,
                password = password.value
            )
            coroutineScope.launch {
                try {
                    val res = RetrofitInstance.api.createUser(body) // res is a Response<MyResponse>
                    if (res.isSuccessful) {
                        println("Response Body: ${res.body()}") // Prints the parsed response body
                        val intent = Intent(this@CreateUser, Login::class.java)
                        intent.putExtra("message", "Welcome to Second Activity!")
                        startActivity(intent)
                    }else if (res.code() == 409) {
                        username.value = ""
                        password.value = ""
                        email.value = ""
                        Toast.makeText(this@CreateUser, "User already exists!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@CreateUser, "Error creating User!", Toast.LENGTH_LONG).show()
                        println("Error: ${res.errorBody()?.string()}") // Prints the error message
                    }
                } catch (e: Exception) {
                    println("Exception: $e") // Logs any exceptions
                }
            }

        }

        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()

        ) {
            Text(text = "Welcome to Pic-a-Day",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,)
            Spacer(Modifier.padding(10.dp))
            TextField(value = email.value, onValueChange = {email.value = it}, label = { Text("Email") }, modifier = Modifier.padding(10.dp))
            TextField(value = username.value, onValueChange = {username.value = it}, label = { Text("Username") }, modifier = Modifier.padding(10.dp))
            TextField(value = password.value, onValueChange = {password.value = it},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.padding(10.dp))

            Button(onClick = {createUser.value = true}, shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(20.dp)) {
                Text("Create User")
            }

        }
    }
}