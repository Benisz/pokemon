package com.kapmacs.pokemon.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kapmacs.pokemon.ui.features.detail.PokemonDetailScreen
import com.kapmacs.pokemon.ui.features.list.PokemonListScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppScaffold(
        navController = navController,
        drawerState = drawerState,
        onMenuClick = {
            scope.launch {
                if (drawerState.isClosed) drawerState.open() else drawerState.close()
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues).background(Color.White),
            navController = navController,
            startDestination = Screen.PokemonList.route
        ) {
            composable(Screen.PokemonList.route) {
                PokemonListScreen(navController = navController)
            }
            composable(
                route = Screen.PokemonDetail.route,
                arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
            ) {
                PokemonDetailScreen(navController = navController)
            }
        }
    }
}
