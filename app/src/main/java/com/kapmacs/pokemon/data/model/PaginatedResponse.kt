package com.kapmacs.pokemon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedApiResource>
)
