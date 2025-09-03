package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import com.kapmacs.pokemon.domain.model.PokemonListItem
import java.util.Locale
import javax.inject.Inject

class GetPokemonsByTypeUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(typeName: String): Result<List<PokemonListItem>> {
        return try {
            val capitalizedTypeName = typeName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
            val pokemons = repository.getPokemonsByType(typeName)
                .pokemonSlots.map { slot ->
                    val pokemonName = slot.pokemon.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                    PokemonListItem(name = pokemonName, type = capitalizedTypeName)
                }
            Result.success(pokemons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
