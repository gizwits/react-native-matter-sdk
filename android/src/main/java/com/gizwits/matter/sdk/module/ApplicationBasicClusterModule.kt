package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.ApplicationBasicCluster
import chip.devicecontroller.ChipClusters.ApplicationBasicCluster.VendorIDAttributeCallback
import chip.devicecontroller.ChipClusters.CharStringAttributeCallback
import chip.devicecontroller.ChipClusters.IntegerAttributeCallback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.lang.Exception

class ApplicationBasicClusterModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    @ReactMethod
    fun readVendorID(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        ApplicationBasicCluster(devicePointer, endpointId).readVendorIDAttribute(
            object : VendorIDAttributeCallback {

                override fun onSuccess(vendorId: Int) {
                    promise.resolve(vendorId)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun readVendorName(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        ApplicationBasicCluster(devicePointer, endpointId).readVendorNameAttribute(
            object : CharStringAttributeCallback {

                override fun onSuccess(vendorName: String) {
                    promise.resolve(vendorName)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun readProductId(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        ApplicationBasicCluster(devicePointer, endpointId).readProductIDAttribute(
            object : IntegerAttributeCallback {

                override fun onSuccess(productId: Int) {
                    promise.resolve(productId)
                }

                override fun onError(error: Exception) {
                    promise.reject(error)
                }

            }
        )
    }

    @ReactMethod
    fun readProductName(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        ApplicationBasicCluster(devicePointer, endpointId).readApplicationNameAttribute(
            object : CharStringAttributeCallback {

                override fun onSuccess(productName: String) {
                    promise.resolve(productName)
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

        const val NAME = "BasicClusterModule"

    }

}