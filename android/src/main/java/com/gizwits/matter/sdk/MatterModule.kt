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
     * 通过蓝牙搜索并配对设备，并将其添加至网络
     * @param deviceId 设备的ID
     * @param discriminator 设备识别码
     * @param setupPinCode 身份校验码
     * @param wifiSSID wifi名称
     * @param wifiPassword wifi密码
     * @param promise 成功则返回设备的ID，否则返回异常
     */
    @ReactMethod
    fun pairDeviceWithBle(
        deviceId: Int,
        discriminator: Int,
        setupPinCode: Int,
        wifiSSID: String,
        wifiPassword: String,
        promise: Promise
    ) {
        moduleScope.launch {
            Matter.pairDeviceWithBle(
                deviceId = deviceId.toLong(),
                discriminator = discriminator,
                setupPinCode = setupPinCode.toLong(),
                wifiSSID = wifiSSID,
                wifiPassword = wifiPassword
            ).onSuccess {
                promise.resolve(it.toInt())
            }.onFailure {
                promise.reject(it)
            }
        }
    }

    override fun getName(): String {
        return NAME
    }

    companion object {

        const val NAME = "Matter"

        const val VENDOR_ID = 0xFFF4

        const val CHIP_UUID = "0000FFF6-0000-1000-8000-00805F9B34FB"

    }

}
