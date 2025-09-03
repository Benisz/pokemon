package com.kapmacs.pokemon.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kapmacs.pokemon.R


private val TopBarColor = Color(0xFFE53935)
private val DrawerSheetBackgroundColor = Color(0xFFE53935)
private const val POKEAPI_DOCS_URL = "https://pokeapi.co/docs/v2"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val uriHandler = LocalUriHandler.current

    val canPop = navController.previousBackStackEntry != null
    val showBackButton =
        canPop && currentRoute != Screen.PokemonList.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DrawerSheetBackgroundColor
            )  {
                NavigationDrawerItem(
                    label = { Text("PokeAPI Documentation", color = Color.White) },
                    selected = false,
                    onClick = {
                        uriHandler.openUri(POKEAPI_DOCS_URL)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .background(TopBarColor)
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (showBackButton) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    } else if (currentRoute == Screen.PokemonList.route) {
                        IconButton(
                            onClick = onMenuClick,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.pokemon_logo),
                        contentDescription = "Pokémon Logo",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
