package com.kapmacs.pokemon.domain.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val height: Int,
    val weight: Int,
    val abilities: List<PokemonAbility>
)
