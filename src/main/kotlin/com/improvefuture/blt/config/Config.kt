package com.improvefuture.blt.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val domain: String,
    val apiKey: String
) {
    val key
    get() = domain.split('.')[2]

    val spaceId
    get() = domain.split('.')[0]
}
