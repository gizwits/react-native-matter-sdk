package com.gizwits.matter.sdk.core.model

import chip.setuppayload.SetupPayload
import com.google.gson.annotations.SerializedName

data class MatterSetupPayload(
    @SerializedName("version")
    val version: Int,
    @SerializedName("vendorId")
    val vendorId: Int,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("commissioningFlow")
    val commissioningFlow: Int,
    @SerializedName("discoveryCapabilities")
    val discoveryCapabilities: List<String>,
    @SerializedName("discriminator")
    val discriminator: Int,
    @SerializedName("hasShortDiscriminator")
    val hasShortDiscriminator: Boolean,
    @SerializedName("setupPinCode")
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


