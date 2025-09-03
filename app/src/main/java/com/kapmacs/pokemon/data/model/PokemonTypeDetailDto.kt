package com.kapmacs.pokemon.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeDetailDto(
    val id: Int,
    val name: String,
    @SerialName("pokemon")
    val pokemonSlots: List<PokemonSlotDto>
)
