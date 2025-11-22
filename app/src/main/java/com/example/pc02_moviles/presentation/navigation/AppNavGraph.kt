
package com.example.pc02_moviles.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pc02_moviles.presentation.liga1.EquiposScreen
import com.example.pc02_moviles.presentation.liga1.Liga1RegistrationScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "liga1_registration") {
        composable("liga1_registration") {
            Liga1RegistrationScreen(navController = navController)
        }
        composable("equipos") {
            EquiposScreen(navController = navController)
        }
    }
}
