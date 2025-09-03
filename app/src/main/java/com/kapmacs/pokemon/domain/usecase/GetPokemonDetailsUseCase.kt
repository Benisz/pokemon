package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import com.kapmacs.pokemon.domain.model.PokemonAbility
import com.kapmacs.pokemon.domain.model.PokemonDetails
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(pokemonNameOrId: String): Result<PokemonDetails> {
        return try {
            val dto = repository.getPokemonDetails(pokemonNameOrId)
            val details = PokemonDetails(
                id = dto.id,
                name = dto.name,
                imageUrl = dto.sprites.frontDefault,
                height = dto.height,
                weight = dto.weight,
                abilities = dto.abilities.filter { !it.isHidden }.map { PokemonAbility(name = it.ability.name) }
            )
            Result.success(details)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
