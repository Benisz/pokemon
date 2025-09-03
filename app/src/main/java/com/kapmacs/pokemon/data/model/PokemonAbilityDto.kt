package com.kapmacs.pokemon.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonAbilityDto(
    val ability: NamedApiResource,
    @SerialName("is_hidden")
    val isHidden: Boolean,
    val slot: Int
)
