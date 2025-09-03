package com.kapmacs.pokemon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int,
    val type: NamedApiResource
)

@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<PokemonAbilityDto>,
    val sprites: PokemonSpritesDto,
    val types: List<PokemonTypeSlotDto>
)
