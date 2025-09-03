package com.kapmacs.pokemon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NamedApiResource(
    val name: String,
    val url: String
)
