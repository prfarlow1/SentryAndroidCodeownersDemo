package com.peterfarlow.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    val breeds: List<Breed> = emptyList(),
    val id: String = "",
    val url: String = "",
)
