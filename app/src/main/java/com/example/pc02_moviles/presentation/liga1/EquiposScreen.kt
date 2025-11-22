package com.example.pc02_moviles.presentation.liga1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Team(val name: String, val year: String, val titles: String, val imageUrl: String)

class EquiposViewModel : ViewModel() {
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    init {
        fetchTeams()
    }

    private fun fetchTeams() {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("equipos")
                .get()
                .addOnSuccessListener { result ->
                    val teamList = result.map { document ->
                        Team(
                            name = document.getString("name") ?: "",
                            year = document.getString("year") ?: "",
                            titles = document.getString("titles") ?: "",
                            imageUrl = document.getString("imageUrl") ?: ""
                        )
                    }
                    _teams.value = teamList
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquiposScreen(navController: NavController, viewModel: EquiposViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val teams by viewModel.teams.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos de la Liga 1") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("liga1_registration") }) {
                Icon(Icons.Default.Add, contentDescription = "Registrar Equipo")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (teams.isEmpty()) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(teams) { team ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AsyncImage(
                                    model = team.imageUrl,
                                    contentDescription = team.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text("Nombre: ${team.name}", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge)
                                Text("Año de fundación: ${team.year}", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                                Text("Títulos: ${team.titles}", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
