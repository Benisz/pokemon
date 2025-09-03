package com.kapmacs.pokemon.data.remote

import com.kapmacs.pokemon.data.model.PaginatedResponse
import com.kapmacs.pokemon.data.model.PokemonDetailDto
import com.kapmacs.pokemon.data.model.PokemonTypeDetailDto
import com.kapmacs.pokemon.data.repository.AllPokemonsResponse

interface PokeApiService {
    suspend fun getPokemonTypes(): PaginatedResponse
    suspend fun getPokemonsByType(typeName: String): PokemonTypeDetailDto
    suspend fun getPokemonDetails(pokemonNameOrId: String): PokemonDetailDto
    suspend fun getAllPokemons(): AllPokemonsResponse
}
