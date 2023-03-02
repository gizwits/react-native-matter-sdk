package com.gizwits.matter.sdk

import android.app.Application
import android.util.Log
import chip.devicecontroller.ChipDeviceController
import chip.devicecontroller.ControllerParams
import chip.platform.*
import chip.setuppayload.SetupPayload
import chip.setuppayload.SetupPayloadParser
import chip.setuppayload.SetupPayloadParser.SetupPayloadException
import chip.setuppayload.SetupPayloadParser.UnrecognizedQrCodeException
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.gizwits.matter.sdk.core.model.MatterSetupPayload
import com.gizwits.matter.sdk.core.model.asMatterSetupPayload
import com.google.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MatterModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private val moduleScope: CoroutineScope = CoroutineScope(SupervisorJob())

    init {
        Matter.init(reactContext.applicationContext as Application)
    }

    /**
     * 解析Matter设备所关联的配对二维码内容，返回用于配对设备的负载信息
     * @param qrCodeContent 设备二维码内容
     * @param promise 成功则返回Json形式的设备产品信息，否则返回异常
     */
    @ReactMethod
    fun parseForSetupPayload(qrCodeContent: String, promise: Promise) {
        Matter.parseForSetupPayload(qrCodeContent)
            .onSuccess {
                promise.resolve(it)
            }.onFailure {
                promise.reject(it)
            }
    }

    /**
     * 根据设备ID获取映射到已配对设备的指针
     * @param deviceIdStr 设备ID的字符串表示形式
     * @param promise 成功则返回设备的指针
     */
    @ReactMethod
    fun getPairedDevicePointer(deviceIdStr: String, promise: Promise) {
        val deviceId: Long = deviceIdStr.toLong()
        moduleScope.launch {
            Matter.getPairedDevicePointer(deviceId)
                .onSuccess {
                    promise.resolve(it.toString())
                }.onFailure {
                    promise.reject(it)
                }
        }
    }

    /**
     * 开启指定设备的配对窗口
     * @param devicePointer 设备的指针
     * @param duration 持续时间，单位：秒
     */
    @ReactMethod
    fun openPairingWindowCallback(
        devicePointerStr: String,
        duration: Int,
        promise: Promise
    ) {
        val devicePointer: Long = devicePointerStr.toLong()
        moduleScope.launch {
            Matter.openPairingWindowCallback(
                devicePointer = devicePointer,
                duration = duration
            ).onFailure {
                promise.resolve(null)
            }.onFailure {
                promise.reject(it)
            }
        }
    }

    /**
     * 通过蓝牙搜索并配对设备，并将其添加至网络
     * @param deviceIdStr 设备的ID的字符串表现形式
     * @param discriminator 设备识别码
     * @param setupPinCodeStr 身份校验码的字符串表现形式
     * @param wifiSSID wifi名称
     * @param wifiPassword wifi密码
     * @param promise 成功则返回设备的ID，否则返回异常
     */
    @ReactMethod
    fun pairDeviceWithBle(
        deviceIdStr: String,
        discriminator: Int,
        setupPinCodeStr: String,
        wifiSSID: String,
        wifiPassword: String,
        promise: Promise
    ) {
        val deviceId: Long = deviceIdStr.toLong()
        val setupPinCode: Long = setupPinCodeStr.toLong()
        moduleScope.launch {
            Matter.pairDeviceWithBle(
                deviceId = deviceId,
                discriminator = discriminator,
                setupPinCode = setupPinCode,
                wifiSSID = wifiSSID,
                wifiPassword = wifiPassword
            ).onSuccess {
                promise.resolve(it.toString())
            }.onFailure {
                promise.reject(it)
            }
        }
    }

    override fun getName(): String {
        return NAME
    }

    companion object {

        const val NAME = "MatterModule"

        const val VENDOR_ID = 0xFFF4

        const val CHIP_UUID = "0000FFF6-0000-1000-8000-00805F9B34FB"

    }

}
