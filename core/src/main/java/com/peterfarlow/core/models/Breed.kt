package com.peterfarlow.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Breed(
    val id: String = "",
    val name: String = "",
    val description: String = "",
)
