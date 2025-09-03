package com.kapmacs.pokemon.ui.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetail : Screen("pokemon_detail/{pokemonName}") {
        fun createRoute(pokemonName: String) = "pokemon_detail/$pokemonName"
    }
}
