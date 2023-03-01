package com.gizwits.matter.sdk.core.model

data class DeviceNetworkInterface(
    val name: String,
    val iPv6Addresses: List<String>
)
