package com.gizwits.matter.sdk.core.model

import chip.setuppayload.SetupPayload

data class MatterSetupPayload(
    val version: Int,
    val vendorId: Int,
    val productId: Int,
    val discoveryCapabilities: List<String>,
    val discriminator: Int,
    val setupPinCode: Long
)

fun SetupPayload.asMatterSetupPayload(): MatterSetupPayload =
    MatterSetupPayload(
        version = version,
        vendorId = vendorId,
        productId = productId,
        discoveryCapabilities = discoveryCapabilities.map {
            it.name
        },
        discriminator = discriminator,
        setupPinCode = setupPinCode
    )


