package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsPokemonCaughtUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(pokemonName: String): Flow<Boolean> {
        return repository.isPokemonCaught(pokemonName)
    }
}
