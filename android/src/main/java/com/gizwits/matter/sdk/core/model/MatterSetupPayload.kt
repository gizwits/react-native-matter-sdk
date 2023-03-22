package com.gizwits.matter.sdk.core.model

import chip.setuppayload.SetupPayload

data class MatterSetupPayload(
    val version: Int,
    val vendorId: Int,
    val productId: Int,
    val commissioningFlow: Int,
    val discoveryCapabilities: List<String>,
    val discriminator: Int,
    val hasShortDiscriminator: Boolean,
    val setupPinCode: Long
)

fun SetupPayload.asMatterSetupPayload(): MatterSetupPayload =
    MatterSetupPayload(
        version = version,
        vendorId = vendorId,
        productId = productId,
        commissioningFlow = commissioningFlow,
        discoveryCapabilities = discoveryCapabilities.map {
            it.name
        },
        discriminator = discriminator,
        hasShortDiscriminator = hasShortDiscriminator,
        setupPinCode = setupPinCode
    )


