package com.gizwits.matter.sdk.module

import chip.devicecontroller.ChipClusters.DescriptorCluster
import chip.devicecontroller.ChipClusters.DescriptorCluster.DeviceTypeListAttributeCallback
import chip.devicecontroller.ChipStructs
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.lang.Exception

class DescriptorClusterModule(
    reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    /**
     * 读取指定设备的指定端点的设备类型列表
     * @param devicePointerStr 映射至设备的指针的字符串表现形式
     * @param endpointId 设备的端点ID
     * @param promise 成功则返回设备端点的设备类型列表
     */
    @ReactMethod
    fun readDeviceTypeList(devicePointerStr: String, endpointId: Int, promise: Promise) {
        val devicePointer: Long = devicePointerStr.toLong()
        DescriptorCluster(devicePointer, endpointId).readDeviceTypeListAttribute(
            object : DeviceTypeListAttributeCallback {

                override fun onSuccess(deviceTypeList: MutableList<ChipStructs.DescriptorClusterDeviceTypeStruct>) {
                    promise.resolve(
                        Arguments.makeNativeArray(
                            deviceTypeList.map { it.type.toString() }
                        )
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

        const val NAME = "DescriptorClusterModule"

    }

}