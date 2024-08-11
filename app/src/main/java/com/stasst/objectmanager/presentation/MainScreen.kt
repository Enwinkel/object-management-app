package com.stasst.objectmanager.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stasst.objectmanager.presentation.add_object.AddObjectScreen
import com.stasst.objectmanager.presentation.add_relations.AddRelationsScreen
import com.stasst.objectmanager.presentation.all_objects.AllObjectsScreen
import com.stasst.objectmanager.presentation.edit_object.EditObjectScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "objectList") {
        composable("objectList") {
            AllObjectsScreen(navController = navController)
        }
        composable("objectDetail/{objectId}") { backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId")?.toLong() ?: 0
            EditObjectScreen(navController = navController, id = objectId)
        }
        composable("addObject") {
            AddObjectScreen(navController = navController)
        }
        composable("addRelations/{objectId}"){ backStackEntry ->
            val objectId = backStackEntry.arguments?.getString("objectId")?.toLong() ?: 0
            AddRelationsScreen(navController = navController, id = objectId)
        }
    }
}