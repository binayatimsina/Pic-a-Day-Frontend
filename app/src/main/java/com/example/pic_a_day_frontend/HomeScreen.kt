package com.example.pic_a_day_frontend

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pic_a_day_frontend.User.UserDataBody
import com.example.pic_a_day_frontend.utils.RetrofitInstance
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import java.util.UUID

//import com.google.firebase.storage.storage

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(username: String, token: String) {
    val coroutineScope = rememberCoroutineScope()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var savedImageURL by remember { mutableStateOf("") }
    var imageUploaded by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }
    var dataSaved by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Icon(Icons.Filled.Home, "", modifier = Modifier.size(50.dp))
                    Text("Pic-a-Day", fontSize = 30.sp, modifier = Modifier.padding(40.dp))
                }
            })}
    ) { innerPadding ->
        if (imageUploaded) {
            imageUploaded = false
            val body = UserDataBody(imageURL = savedImageURL, note = note, username = username)
            val token = "Bearer $token"
            coroutineScope.launch {
                try {
                    val res = RetrofitInstance.api.uploadData(body = body, token = token) // res is a Response<MyResponse>
                    if (res.isSuccessful) {
                        println("SuccessFull ${res.body()!!.message}")
                        dataSaved = true
                    } else {
                        println(res.errorBody().toString())
                    }

                } catch (e: Exception) {
                    println("Exception: $e") // Logs any exceptions
                }
            }
        }
        Column(modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            if (selectedImageUri == null) {
                Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Person, "", Modifier.size(400.dp))
                    Button(onClick = { launcher.launch("image/*") }) {
                        Text("Pick Image")
                    }
                }
            } else {
                Column (verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri), "",
                        Modifier.size(400.dp)
                    )

                    if (!dataSaved) {
                        OutlinedTextField(
                            value = note,
                            onValueChange = {
                                note = it
                            },
                            label = { Text("Add Note") },
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            maxLines = 5,
                        )

                        Button(onClick = {
                            uploadImageToFirebase(
                                selectedImageUri!!,
                                {
                                    imageUploaded = true
                                    //TODO Make a call to store the image link and Note to the database
                                    println(it)
                                    savedImageURL = it
                                },
                                { println(it) }
                            )
                        }) {
                            Text("Upload")
                        }
                    } else {
                        Text(note, modifier = Modifier.padding(20.dp))
                    }

                }
            }
        }
    }
}

fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    // Get Firebase Storage instance
    val storageReference = FirebaseStorage.getInstance().reference

    // Create a unique file name for the image
    val fileName = "images/${UUID.randomUUID()}.jpg"
    val imageReference = storageReference.child(fileName)

    // Start the upload
    imageReference.putFile(imageUri)
        .addOnSuccessListener { taskSnapshot ->
            // Get the image download URL
            imageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
                onSuccess(downloadUrl.toString()) // Return the image URL
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}
