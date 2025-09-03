package com.kapmacs.pokemon.data.remote

import com.kapmacs.pokemon.data.model.PaginatedResponse
import com.kapmacs.pokemon.data.model.PokemonDetailDto
import com.kapmacs.pokemon.data.model.PokemonTypeDetailDto
import com.kapmacs.pokemon.data.repository.AllPokemonsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class PokeApiServiceImpl @Inject constructor(
    private val client: HttpClient
) : PokeApiService {

    private val baseUrl = "https://pokeapi.co/api/v2"

    override suspend fun getPokemonTypes(): PaginatedResponse {
        return client.get("$baseUrl/type").body()
    }

    override suspend fun getPokemonsByType(typeName: String): PokemonTypeDetailDto {
        return client.get("$baseUrl/type/$typeName").body()
    }

    override suspend fun getPokemonDetails(pokemonNameOrId: String): PokemonDetailDto {
        return client.get("$baseUrl/pokemon/$pokemonNameOrId").body()
    }

    override suspend fun getAllPokemons(): AllPokemonsResponse {
        return client.get("$baseUrl/pokemon").body()
    }
}