package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.DefaultClusterCallback
import chip.devicecontroller.ChipClusters.LevelControlCluster
import chip.devicecontroller.ChipClusters.LevelControlCluster.CurrentLevelAttributeCallback
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
        level: Int,
        transitionTime: Int,
        optionsMask: Int,
        optionsOverride: Int,
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

            }, level, transitionTime, optionsMask, optionsOverride
        )
    }

    @ReactMethod
    fun readCurrentLevel(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        LevelControlCluster(devicePointer, endpointId).readCurrentLevelAttribute(
            object : CurrentLevelAttributeCallback {

                override fun onSuccess(value: Int?) {
                    if (value != null) {
                        promise.resolve(value)
                    } else {
                        promise.reject(Exception("Unknown error"))
                    }
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

        const val NAME: String = "LevelControlClusterModule"

    }

}