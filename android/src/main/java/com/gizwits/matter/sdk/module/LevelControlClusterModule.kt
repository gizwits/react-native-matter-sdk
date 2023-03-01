package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.DefaultClusterCallback
import chip.devicecontroller.ChipClusters.LevelControlCluster
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class LevelControlClusterModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    @ReactMethod
    fun moveToLevel(
        devicePointerStr: String,
        endpointId: Int,
        alpha: Int,
        red: Int,
        green: Int,
        blue: Int,
        promise: Promise
    ) {
        val devicePointer: Long = devicePointerStr.toLong()
        LevelControlCluster(devicePointer, endpointId).moveToLevel(
            object : DefaultClusterCallback {

                override fun onSuccess() {
                    promise.resolve(null)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }, alpha, red, green, blue
        )
    }

    override fun getName(): String {
        return NAME
    }

    companion object {

        const val NAME: String = "LevelControlClusterModule"

    }

}