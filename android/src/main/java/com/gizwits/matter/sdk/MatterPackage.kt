package com.gizwits.matter.sdk

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.gizwits.matter.sdk.module.*


class MatterPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
            MatterModule(reactContext),
            BasicClusterModule(reactContext),
            DescriptorClusterModule(reactContext),
            GeneralDiagnosticsClusterModule(reactContext),
            OnOffClusterModule(reactContext),
            LevelControlClusterModule(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

}
