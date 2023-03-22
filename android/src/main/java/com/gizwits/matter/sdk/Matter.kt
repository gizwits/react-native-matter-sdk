package com.gizwits.matter.sdk

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import chip.devicecontroller.*
import chip.platform.*
import chip.setuppayload.DiscoveryCapability
import chip.setuppayload.SetupPayload
import chip.setuppayload.SetupPayloadParser
import com.gizwits.matter.sdk.common.PairCompletionListener
import com.gizwits.matter.sdk.core.model.MatterSetupPayload
import com.gizwits.matter.sdk.core.model.asMatterSetupPayload
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import java.util.*

@SuppressLint("MissingPermission")
object Matter {

    private const val CHIP_UUID: String = "0000FFF6-0000-1000-8000-00805F9B34FB"

    private const val VENDOR_ID: Int = 0xFFF4

    private val gson: Gson = GsonBuilder().create()

    private lateinit var application: Application

    private lateinit var bluetoothManager: BluetoothManager

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var androidChipPlatform: AndroidChipPlatform

    private lateinit var chipDeviceController: ChipDeviceController

    private val setupPayloadParser: SetupPayloadParser = SetupPayloadParser()

    fun init(application: Application) {
        this.application = application
        // 初始化系统相关依赖
        bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        // 初始化Matter相关依赖
        ChipDeviceController.loadJni()
        androidChipPlatform = AndroidChipPlatform(
            AndroidBleManager(),
            PreferencesKeyValueStoreManager(application),
            PreferencesConfigurationManager(application),
            NsdManagerServiceResolver(application),
            NsdManagerServiceBrowser(application),
            ChipMdnsCallbackImpl(),
            DiagnosticDataProviderImpl(application)
        )
        chipDeviceController = ChipDeviceController(
            ControllerParams.newBuilder().setControllerVendorId(VENDOR_ID).build()
        )
    }

    /**
     * 解析Matter设备所关联的配对二维码内容，返回用于配对设备的负载信息
     * @param qrCodeContent 设备二维码内容
     * @return 配对设备的负载信息
     */
    fun parseForSetupPayload(qrCodeContent: String): Result<String> {
        return runCatching {
            gson.toJson(
                setupPayloadParser
                    .parseQrCode(qrCodeContent)
                    .asMatterSetupPayload()
            )
        }
    }

    /**
     * 解析Matter设备的手动配对码，返回用于配对设备的负载信息
     * @param manualCode 设备的手动配对码
     * @return 配对设备的负载信息
     */
    fun parseManualCodeForSetupPayload(manualCode: String): Result<String> {
        return runCatching {
            gson.toJson(
                setupPayloadParser
                    .parseManualEntryCode(manualCode)
                    .asMatterSetupPayload()
            )
        }
    }

    /**
     * 获取Matter设备手动配对码
     * @param payload 设备配对信息的json字符串形式
     * @return 设备手动配对码
     */
    fun getManualEntryCodeFromPayload(payload: String): Result<String> {
        val matterSetupPayload: MatterSetupPayload = gson
            .fromJson(payload, MatterSetupPayload::class.java)
        val discoveryCapabilities: List<DiscoveryCapability> =
            matterSetupPayload.discoveryCapabilities.map {
                DiscoveryCapability.valueOf(it)
            }
        return runCatching {
            setupPayloadParser.getManualEntryCodeFromPayload(
                SetupPayload(
                    matterSetupPayload.version,
                    matterSetupPayload.vendorId,
                    matterSetupPayload.productId,
                    matterSetupPayload.commissioningFlow,
                    discoveryCapabilities.toSet(),
                    matterSetupPayload.discriminator,
                    matterSetupPayload.hasShortDiscriminator,
                    matterSetupPayload.setupPinCode
                )
            )
        }
    }

    /**
     * 获取Matter设备手动配对码
     * @param version 版本号
     * @param vendorId 厂商ID
     * @param productId 产品ID
     * @param commissioningFlow 当前委托模式？
     * @param discoveryCapabilities 发现设备的方式
     * @param discriminator 设备识别码
     * @param hasShortDiscriminator 是否为短的识别码
     * @param setupPinCode 设备配对码
     */
    fun getManualEntryCodeFromPayload(
        version: Int,
        vendorId: Int,
        productId: Int,
        commissioningFlow: Int,
        discoveryCapabilities: List<DiscoveryCapability>,
        discriminator: Int,
        hasShortDiscriminator: Boolean,
        setupPinCode: Long
    ): Result<String> {
        return runCatching {
            setupPayloadParser.getManualEntryCodeFromPayload(
                SetupPayload(
                    version,
                    vendorId,
                    productId,
                    commissioningFlow,
                    discoveryCapabilities.toSet(),
                    discriminator,
                    hasShortDiscriminator,
                    setupPinCode
                )
            )
        }
    }

    /**
     * 根据设备ID获取映射到已配对设备的指针
     * @param deviceId 设备ID
     * @return 映射到对应设备的指针
     */
    suspend fun getPairedDevicePointer(deviceId: Long): Result<Long> {
        return runCatching<Matter, Long> {
            suspendCancellableCoroutine {
                chipDeviceController.getConnectedDevicePointer(
                    deviceId,
                    object : GetConnectedDeviceCallbackJni.GetConnectedDeviceCallback {

                        override fun onDeviceConnected(devicePointer: Long) {
                            it.resumeWith(Result.success(devicePointer))
                        }

                        override fun onConnectionFailure(deviceId: Long, exception: Exception) {
                            it.resumeWith(Result.failure(exception))
                        }

                    }
                )
            }
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    /**
     * 开启指定设备的配对窗口
     * @param devicePointer 设备的指针
     * @param duration 持续时间，单位：秒
     */
    suspend fun openPairingWindowCallback(
        devicePointer: Long,
        duration: Int
    ): Result<Unit> {
        return runCatching<Matter, Unit>{
            suspendCancellableCoroutine {
                chipDeviceController.openPairingWindowCallback(
                    devicePointer,
                    duration,
                    object : OpenCommissioningCallback {

                        override fun onSuccess(deviceId: Long, manualPairingCode: String?, qrCode: String?) {
                            it.resumeWith(Result.success(Unit))
                        }

                        override fun onError(status: Int, deviceId: Long) {
                            it.resumeWith(
                                Result.failure(Exception("OpenBasicCommissioning Fail! \n" +
                                        "Device ID : $deviceId\n" +
                                        "ErrorCode : $status"))
                            )
                        }

                    }
                )
            }
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    /**
     * 通过局域网搜索并配对设备
     * @param deviceId 设备的ID
     * @param discriminator 设备识别码
     * @param setupPinCode 身份校验码
     */
    suspend fun pairDeviceWithAddress(
        deviceId: Long,
        discriminator: Int,
        setupPinCode: Long
    ): Result<Long> {
        // Step 1、通过SDK接口发现搜索局域网设备，通过设备识别码作为过滤器，过滤出所配对的设备
        val discoveredDevice: DiscoveredDevice = withTimeoutOrNull(10000) {
            try {
                while (isActive) {
                    chipDeviceController.discoverCommissionableNodes()
                    delay(3500)
                    // 检索已发现的设备
                    for (index in 0..Int.MAX_VALUE) {
                        val device: DiscoveredDevice? = chipDeviceController.getDiscoveredDevice(index)
                        if (device == null) {
                            // 未搜索到任何设备，等待重试
                            delay(500)
                            break
                        }
                        if (device.discriminator == discriminator.toLong()) {
                            // 识别码一致，返回设备对象
                            return@withTimeoutOrNull device
                        }
                    }
                }
            } finally {
                // 获取一次已发现的设备，停止Matter SDK内部发现设备的业务
                chipDeviceController.getDiscoveredDevice(0)
            }
            return@withTimeoutOrNull null
        } ?: return Result.failure(Exception("Search for devices timed out"))
        // Step 4、使用IP地址开始配对设备
        return withContext(NonCancellable) {
            callbackFlow {
                chipDeviceController.setCompletionListener(object : PairCompletionListener() {
                    override fun onCommissioningComplete(nodeId: Long, errorCode: Int) {
                        super.onCommissioningComplete(nodeId, errorCode)
                        // TODO 释放蓝牙设备所持有的资源
                        chipDeviceController.close()
                        if (errorCode == 0) {
                            trySend(Result.success(deviceId))
                        } else {
                            trySend(Result.failure(Exception("${errorCode} - Pairing device failed")))
                        }
                    }
                })
                chipDeviceController.pairDeviceWithAddress(
                    deviceId, discoveredDevice.ipAddress, 5540, discriminator, setupPinCode, null
                )
                awaitClose()
            }.first()
        }
    }

    /**
     * 通过蓝牙搜索并配对设备，并将其添加至网络
     * @param deviceId 设备的ID
     * @param discriminator 设备识别码
     * @param setupPinCode 身份校验码
     * @param wifiSSID wifi名称
     * @param wifiPassword wifi密码
     * @return 如果成功，则为已配对设备的ID
     */
    suspend fun pairDeviceWithBle(
        deviceId: Long,
        discriminator: Int,
        setupPinCode: Long,
        wifiSSID: String,
        wifiPassword: String
    ): Result<Long> {
        // Step 1、通过蓝牙搜索和过滤出对应的蓝牙设备，通过设备识别码作为蓝牙广播扫描过滤器，过滤出所配对的设备
        val bluetoothLeScanner: BluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        val bluetoothDevice: BluetoothDevice = withTimeoutOrNull(10000) {
            callbackFlow<BluetoothDevice> {
                val scanCallback: ScanCallback = object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        trySend(result.device)
                    }
                }
                val serviceData: ByteArray = getServiceData(discriminator)
                val scanFilter: ScanFilter = ScanFilter.Builder()
                    .setServiceData(ParcelUuid(UUID.fromString(CHIP_UUID)), serviceData)
                    .build()
                val scanSettings: ScanSettings = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build()
                bluetoothLeScanner.startScan(listOf(scanFilter), scanSettings, scanCallback)
                awaitClose {
                    bluetoothLeScanner.stopScan(scanCallback)
                }
            }.first()
        } ?: return Result.failure(Exception("Search for bluetooth devices timed out"))
        // Step 2、连接蓝牙设备并等待连接成功
        var connectionId: Int? = null
        val bluetoothGatt: BluetoothGatt? = withTimeoutOrNull(10000) {
            var gatt: BluetoothGatt? = null
            try {
                callbackFlow {
                    // 监听蓝牙服务连接成功回调，成功则抛出BluetoothGatt对象
                    val bluetoothGattCallback: BluetoothGattCallback = getWrappedCallback {
                        trySend(it)
                    }
                    gatt = bluetoothDevice.connectGatt(
                        application, false, bluetoothGattCallback
                    )
                    connectionId = androidChipPlatform.bleManager.addConnection(gatt)
                    androidChipPlatform.bleManager.setBleCallback(object : BleCallback {

                        override fun onCloseBleComplete(connId: Int) {
                            // TODO 将来通过此接口处理蓝牙资源释放相关逻辑
                        }

                        override fun onNotifyChipConnectionClosed(connId: Int) {
                            // TODO 将来通过此接口处理蓝牙资源释放相关逻辑
                            gatt?.disconnect()
                            gatt?.close()
                        }

                    })
                    awaitClose()
                }.first()
            } catch (cancellationException: CancellationException) {
                // 在连接蓝牙过程中任务被取消了，断开蓝牙连接
                gatt?.disconnect()
                gatt?.close()
                throw cancellationException
            }
        }
        if (connectionId == null || bluetoothGatt == null) {
            // 服务连接ID为空或者蓝牙GATT服务未空，则代表连接蓝牙失败
            return Result.failure(Exception("Failed to connect to bluetooth device"))
        }
        // Step 3、开始配对设备
        return withContext(NonCancellable) {
            callbackFlow {
                chipDeviceController.setCompletionListener(object : PairCompletionListener() {
                    override fun onCommissioningComplete(nodeId: Long, errorCode: Int) {
                        super.onCommissioningComplete(nodeId, errorCode)
                        // TODO 释放蓝牙设备所持有的资源
                        chipDeviceController.close()
                        if (errorCode == 0) {
                            trySend(Result.success(deviceId))
                        } else {
                            trySend(Result.failure(Exception("${errorCode} - Pairing device failed")))
                        }
                    }
                })
                val networkCredentials: NetworkCredentials = NetworkCredentials.forWiFi(
                    NetworkCredentials.WiFiCredentials(
                        wifiSSID,
                        wifiPassword
                    )
                )
                chipDeviceController.pairDevice(
                    bluetoothGatt, connectionId!!, deviceId, setupPinCode, networkCredentials
                )
                awaitClose()
            }.first()
        }
    }

    private fun getServiceData(discriminator: Int): ByteArray {
        val opcode = 0
        val version = 0
        val versionDiscriminator = ((version and 0xf) shl 12) or (discriminator and 0xfff)
        return intArrayOf(opcode, versionDiscriminator, versionDiscriminator shr 8)
            .map { it.toByte() }
            .toByteArray()
    }

    private fun getWrappedCallback(onCompleted: (BluetoothGatt) -> Unit): BluetoothGattCallback {
        return object : BluetoothGattCallback() {

            private val wrappedCallback: BluetoothGattCallback =
                androidChipPlatform.bleManager.callback

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                wrappedCallback.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS) {
                    gatt.discoverServices()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                super.onServicesDiscovered(gatt, status)
                wrappedCallback.onServicesDiscovered(gatt, status)
                gatt.requestMtu(247)
            }

            override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
                super.onMtuChanged(gatt, mtu, status)
                wrappedCallback.onMtuChanged(gatt, mtu, status)
                onCompleted(gatt)
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                super.onCharacteristicChanged(gatt, characteristic)
                wrappedCallback.onCharacteristicChanged(gatt, characteristic)
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
                wrappedCallback.onCharacteristicRead(gatt, characteristic, status)
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                super.onCharacteristicWrite(gatt, characteristic, status)
                wrappedCallback.onCharacteristicWrite(gatt, characteristic, status)
            }

            override fun onDescriptorRead(
                gatt: BluetoothGatt,
                descriptor: BluetoothGattDescriptor,
                status: Int
            ) {
                super.onDescriptorRead(gatt, descriptor, status)
                wrappedCallback.onDescriptorRead(gatt, descriptor, status)
            }

            override fun onDescriptorWrite(
                gatt: BluetoothGatt,
                descriptor: BluetoothGattDescriptor,
                status: Int
            ) {
                super.onDescriptorWrite(gatt, descriptor, status)
                wrappedCallback.onDescriptorWrite(gatt, descriptor, status)
            }

            override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
                super.onReadRemoteRssi(gatt, rssi, status)
                wrappedCallback.onReadRemoteRssi(gatt, rssi, status)
            }

            override fun onReliableWriteCompleted(gatt: BluetoothGatt, status: Int) {
                super.onReliableWriteCompleted(gatt, status)
                wrappedCallback.onReliableWriteCompleted(gatt, status)
            }

        }
    }

}