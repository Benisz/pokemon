package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import com.kapmacs.pokemon.domain.model.PokemonListItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.Locale
import javax.inject.Inject

class GetAllPokemonsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): Result<List<PokemonListItem>> {
        return try {
            val initialPokemonList = repository.getAllPokemons().results
            val detailedPokemonList = coroutineScope {
                initialPokemonList.map { pokemonResource ->
                    async {
                        try {
                            val details = repository.getPokemonDetails(pokemonResource.name)
                            val typeName = details.types.firstOrNull()?.type?.name?.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                            } ?: "Unknown"
                            val pokemonName = details.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                            }
                            PokemonListItem(name = pokemonName, type = typeName)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.mapNotNull { it.await() }
            }
            Result.success(detailedPokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
