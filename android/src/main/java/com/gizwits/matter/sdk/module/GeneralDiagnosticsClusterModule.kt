package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.GeneralDiagnosticsCluster
import chip.devicecontroller.ChipClusters.GeneralDiagnosticsCluster.NetworkInterfacesAttributeCallback
import chip.devicecontroller.ChipStructs
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.gizwits.matter.sdk.core.model.DeviceNetworkInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.net.Inet6Address

class GeneralDiagnosticsClusterModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    private val gson: Gson = GsonBuilder().create()

    @ReactMethod
    fun readNetworkInterfaces(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        GeneralDiagnosticsCluster(devicePointer, endpointId).readNetworkInterfacesAttribute(
            object : NetworkInterfacesAttributeCallback {

                override fun onSuccess(networkInterfaces: MutableList<ChipStructs.GeneralDiagnosticsClusterNetworkInterfaceType>) {
                    val deviceNetworkInterface: List<DeviceNetworkInterface> = networkInterfaces.map {
                        DeviceNetworkInterface(
                            name = it.name,
                            iPv6Addresses = it.IPv6Addresses.mapNotNull {
                                Inet6Address.getByAddress(it).hostAddress
                            }
                        )
                    }
                    promise.resolve(
                        gson.toJson(deviceNetworkInterface)
                    )
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    override fun getName(): String {
        return NAME
    }

    companion object {

        const val NAME: String = "GeneralDiagnosticsClusterModule"

    }

}