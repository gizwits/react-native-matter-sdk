package com.gizwits.matter.sdk

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.gizwits.matter.sdk.module.BasicClusterModule
import com.gizwits.matter.sdk.module.DescriptorClusterModule
import com.gizwits.matter.sdk.module.LevelControlClusterModule
import com.gizwits.matter.sdk.module.OnOffClusterModule


class MatterPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
            MatterModule(reactContext),
            BasicClusterModule(reactContext),
            DescriptorClusterModule(reactContext),
            OnOffClusterModule(reactContext),
            LevelControlClusterModule(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

}
