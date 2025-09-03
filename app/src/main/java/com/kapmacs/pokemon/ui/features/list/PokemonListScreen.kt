package com.kapmacs.pokemon.ui.features.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kapmacs.pokemon.ui.features.list.components.*
import com.kapmacs.pokemon.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonTypes by viewModel.pokemonTypes.collectAsState()
    val selectedType by viewModel.selectedPokemonType.collectAsState()
    val filteredPokemons by viewModel.filteredPokemons.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showOnlyCaught by viewModel.showOnlyCaught.collectAsState()
    val isLoadingTypes by viewModel.isLoadingTypes.collectAsState()
    val isLoadingPokemons by viewModel.isLoadingPokemons.collectAsState()
    val error by viewModel.error.collectAsState()
    val caughtPokemonNames by viewModel.caughtPokemonNames.collectAsState()

    var typeDropdownExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackgroundColor)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchTextField(searchQuery = searchQuery, onSearchQueryChanged = viewModel::onSearchQueryChanged)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Pokemon Types", style = MaterialTheme.typography.titleMedium, color = DarkGrey)
        Spacer(modifier = Modifier.height(4.dp))

        if (isLoadingTypes) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = TopBarColor
            )
        }

        PokemonTypeDropdown(
            pokemonTypes = pokemonTypes,
            selectedType = selectedType,
            expanded = typeDropdownExpanded,
            onExpandedChange = { typeDropdownExpanded = it },
            onTypeSelected = {
                viewModel.selectPokemonType(it)
                typeDropdownExpanded = false
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = showOnlyCaught,
                onCheckedChange = viewModel::onShowOnlyCaughtChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = TopBarColor,
                    checkmarkColor = Color.White
                )
            )
            Text("Only show caught Pokemon", color = DarkGrey)
        }
        Spacer(modifier = Modifier.height(16.dp))

        PokemonListHeader()
        HorizontalDivider(color = LightGreyBorder)

        PokemonListContent(
            isLoadingPokemons = isLoadingPokemons,
            filteredPokemons = filteredPokemons,
            error = error,
            caughtPokemonNames = caughtPokemonNames,
            navController = navController,
            onRetry = {
                viewModel.clearError()
                viewModel.selectPokemonType(selectedType)
            },
            onToggleCatchRelease = viewModel::toggleCatchRelease
        )
    }
}
