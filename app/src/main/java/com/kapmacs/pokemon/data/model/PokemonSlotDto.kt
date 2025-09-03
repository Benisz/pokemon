package com.kapmacs.pokemon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonSlotDto(
    val pokemon: NamedApiResource,
    val slot: Int
)
