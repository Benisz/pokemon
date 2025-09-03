package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCaughtPokemonsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getCaughtPokemon().map { entities ->
            entities.map { it.name }
        }
    }
}
