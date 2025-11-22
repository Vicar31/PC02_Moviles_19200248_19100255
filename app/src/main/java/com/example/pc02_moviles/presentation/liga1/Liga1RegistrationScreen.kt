package com.example.pc02_moviles.presentation.liga1

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Liga1RegistrationScreen(navController: NavController) {
    var teamName by remember { mutableStateOf("") }
    var foundationYear by remember { mutableStateOf("") }
    var titlesWon by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Liga 1") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Nombre de equipo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = foundationYear,
                onValueChange = { foundationYear = it },
                label = { Text("Año de fundación") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = titlesWon,
                onValueChange = { titlesWon = it },
                label = { Text("Número de títulos ganados") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Url de la imagen del equipo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    val db = Firebase.firestore
                    val team = hashMapOf(
                        "name" to teamName,
                        "year" to foundationYear,
                        "titles" to titlesWon,
                        "imageUrl" to imageUrl
                    )

                    db.collection("equipos")
                        .add(team)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Liga1RegistrationScreen", "DocumentSnapshot added with ID: ${documentReference.id}")
                            navController.navigate("equipos")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Liga1-RegistrationScreen", "Error adding document", e)
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Equipo")
            }
        }
    }
}
