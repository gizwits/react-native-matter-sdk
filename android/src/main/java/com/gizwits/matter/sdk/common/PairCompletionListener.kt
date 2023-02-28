package com.gizwits.matter.sdk.common

import chip.devicecontroller.ChipDeviceController

abstract class PairCompletionListener : ChipDeviceController.CompletionListener {

    override fun onConnectDeviceComplete() {

    }

    override fun onStatusUpdate(status: Int) {

    }

    override fun onPairingComplete(code: Int) {

    }

    override fun onPairingDeleted(code: Int) {

    }

    override fun onCommissioningComplete(nodeId: Long, errorCode: Int) {

    }

    override fun onReadCommissioningInfo(
        vendorId: Int,
        productId: Int,
        wifiEndpointId: Int,
        threadEndpointId: Int
    ) {

    }

    override fun onCommissioningStatusUpdate(nodeId: Long, stage: String, errorCode: Int) {

    }

    override fun onNotifyChipConnectionClosed() {

    }

    override fun onCloseBleComplete() {

    }

    override fun onError(error: Throwable?) {

    }

    override fun onOpCSRGenerationComplete(csr: ByteArray) {

    }

}