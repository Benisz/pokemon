package com.kapmacs.pokemon.domain.usecase

import com.kapmacs.pokemon.data.repository.PokemonRepository
import com.kapmacs.pokemon.domain.model.PokemonType
import java.util.Locale
import javax.inject.Inject

class GetPokemonTypesUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): Result<List<PokemonType>> {
        return try {
            val types = repository.getPokemonTypes().map { resource ->
                val typeName = resource.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
                PokemonType(name = typeName)
            }
            Result.success(types)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
