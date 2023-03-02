package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.*
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class OnOffClusterModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    @ReactMethod
    fun off(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        OnOffCluster(devicePointer, endpointId).off(
            object : DefaultClusterCallback {

                override fun onSuccess() {
                    promise.resolve(null)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun on(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        OnOffCluster(devicePointer, endpointId).on(
            object : DefaultClusterCallback {

                override fun onSuccess() {
                    promise.resolve(null)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun toggle(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        OnOffCluster(devicePointer, endpointId).toggle(
            object : DefaultClusterCallback {

                override fun onSuccess() {
                    promise.resolve(null)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun readOnOff(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        OnOffCluster(devicePointer, endpointId).readOnOffAttribute(
            object : BooleanAttributeCallback {

                override fun onSuccess(value: Boolean) {
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

        const val NAME = "OnOffClusterModule"

    }

}