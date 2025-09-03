package com.kapmacs.pokemon.data.repository

import com.kapmacs.pokemon.data.local.CaughtPokemonDao
import com.kapmacs.pokemon.data.local.CaughtPokemonEntity
import com.kapmacs.pokemon.data.model.NamedApiResource
import com.kapmacs.pokemon.data.model.PokemonDetailDto
import com.kapmacs.pokemon.data.model.PokemonTypeDetailDto
import com.kapmacs.pokemon.data.remote.PokeApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokeApiService: PokeApiService,
    private val caughtPokemonDao: CaughtPokemonDao
) : PokemonRepository {

    override suspend fun getPokemonTypes(): List<NamedApiResource> {
        return pokeApiService.getPokemonTypes().results
    }

    override suspend fun getPokemonsByType(typeName: String): PokemonTypeDetailDto {
        return pokeApiService.getPokemonsByType(typeName)
    }

    override suspend fun getPokemonDetails(pokemonNameOrId: String): PokemonDetailDto {
        return pokeApiService.getPokemonDetails(pokemonNameOrId)
    }

    override suspend fun getAllPokemons(): AllPokemonsResponse {
        return pokeApiService.getAllPokemons()
    }

    override fun getCaughtPokemon(): Flow<List<CaughtPokemonEntity>> {
        return caughtPokemonDao.getAllCaughtPokemon()
    }

    override suspend fun catchPokemon(pokemonName: String) {
        caughtPokemonDao.catchPokemon(CaughtPokemonEntity(name = pokemonName))
    }

    override suspend fun releasePokemon(pokemonName: String) {
        caughtPokemonDao.releasePokemon(CaughtPokemonEntity(name = pokemonName))
    }

    override fun isPokemonCaught(pokemonName: String): Flow<Boolean> {
        return caughtPokemonDao.isPokemonCaught(pokemonName)
    }
}
