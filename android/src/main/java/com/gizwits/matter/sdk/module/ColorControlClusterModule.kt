package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.ColorControlCluster
import chip.devicecontroller.ChipClusters.DefaultClusterCallback
import chip.devicecontroller.ChipClusters.IntegerAttributeCallback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.lang.Exception

class ColorControlClusterModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    @ReactMethod
    fun moveToHueAndSaturation(
        devicePointerStr: String,
        endpointId: Int,
        hue: Int,
        saturation: Int,
        transitionTime: Int,
        optionsMask: Int,
        optionsOverride: Int,
        promise: Promise
    ) {
        val devicePointer: Long = devicePointerStr.toLong()
        ColorControlCluster(devicePointer, endpointId).moveToHueAndSaturation(
            object : DefaultClusterCallback {

                override fun onSuccess() {
                    promise.resolve(null)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            },
            hue, saturation, transitionTime, optionsMask, optionsOverride
        )
    }

    @ReactMethod
    fun readCurrentHue(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        ColorControlCluster(devicePointer, endpointId).readCurrentHueAttribute(
            object : IntegerAttributeCallback {

                override fun onSuccess(value: Int) {
                    promise.resolve(value)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun readCurrentSaturation(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        ColorControlCluster(devicePointer, endpointId).readCurrentSaturationAttribute(
            object : IntegerAttributeCallback {

                override fun onSuccess(value: Int) {
                    promise.resolve(value)
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

        const val NAME: String = "ColorControlClusterModule"

    }

}