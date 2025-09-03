package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import javax.inject.Inject

class CatchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(pokemonName: String): Result<Unit> {
        return try {
            repository.catchPokemon(pokemonName)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
