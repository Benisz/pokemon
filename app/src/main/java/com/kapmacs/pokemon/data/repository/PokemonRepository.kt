package com.kapmacs.pokemon.data.repository

import com.kapmacs.pokemon.data.model.NamedApiResource
import com.kapmacs.pokemon.data.model.PokemonDetailDto
import com.kapmacs.pokemon.data.model.PokemonTypeDetailDto
import com.kapmacs.pokemon.data.local.CaughtPokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class AllPokemonsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedApiResource>
)

interface PokemonRepository {
    suspend fun getPokemonTypes(): List<NamedApiResource>
    suspend fun getPokemonsByType(typeName: String): PokemonTypeDetailDto
    suspend fun getPokemonDetails(pokemonNameOrId: String): PokemonDetailDto
    suspend fun getAllPokemons(): AllPokemonsResponse

    fun getCaughtPokemon(): Flow<List<CaughtPokemonEntity>>
    suspend fun catchPokemon(pokemonName: String)
    suspend fun releasePokemon(pokemonName: String)
    fun isPokemonCaught(pokemonName: String): Flow<Boolean>
}
