import { NativeModules } from 'react-native';

const BasicClusterModule = NativeModules.BasicClusterModule

const DescriptorClusterModule = NativeModules.DescriptorClusterModule

const GeneralDiagnosticsClusterModule = NativeModules.GeneralDiagnosticsClusterModule

const OnOffClusterModule = NativeModules.OnOffClusterModule

const LevelControlClusterModule = NativeModules.LevelControlClusterModule

const ColorControlClusterModule = NativeModules.ColorControlClusterModule

class BaseCluster {
  devicePointerStr: string;

  endpointId: number;

  constructor(devicePointerStr: string, endpointId: number) {
    this.devicePointerStr = devicePointerStr
    this.endpointId = endpointId;
  }
}

export class DescriptorCluster extends BaseCluster {

  /**
   * 读取指定设备的指点端点的设备类型列表
   * @returns 成功则返回设备端点的设备类型（字符串表现形式）列表
   */
  readDeviceTypeList(): Promise<Array<string>> {
    return DescriptorClusterModule.readDeviceTypeList(
      this.devicePointerStr,
      this.endpointId
    )
  }

}

export class BasicCluster extends BaseCluster {

  /**
   * 读取设备的厂商ID
   * @returns 成功则返回设备的厂商ID
   */
  readVendorID(): Promise<number> {
    return BasicClusterModule.readVendorID(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 读取设备的厂商名称
   * @returns 成功则返回设备的厂商名称
   */
  readVendorName(): Promise<string> {
    return BasicClusterModule.readVendorName(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 读取设备的产品ID
   * @returns 成功则返回设备的产品ID
   */
  readProductId(): Promise<number> {
    return BasicClusterModule.readProductId(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 读取设备的产品名称
   * @returns 成功则返回设备的产品名称
   */
  readProductName(): Promise<string> {
    return BasicClusterModule.readProductName(
      this.devicePointerStr,
      this.endpointId
    )
  }

}

export class OnOffCluster extends BaseCluster {

  /**
   * 关闭开关
   * @returns 
   */
  off(): Promise<undefined> {
    return OnOffClusterModule.off(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 开启开关
   * @returns 
   */
  on(): Promise<undefined> {
    return OnOffClusterModule.on(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 切换开关状态
   * @returns 
   */
  toggle(): Promise<undefined> {
    return OnOffClusterModule.toggle(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 读取开关状态
   * @returns 成功则返回开关状态
   */
  readOnOff(): Promise<boolean> {
    return OnOffClusterModule.readOnOff(
      this.devicePointerStr,
      this.endpointId
    )
  }

}

export class LevelControlCluster extends BaseCluster {

  /**
   * 修改亮度等级
   * @param level 亮度等级
   * @param transitionTime 
   * @param optionsMask 
   * @param optionsOverride 
   * @returns
   */
  moveToLevel(level: number, transitionTime: number, optionsMask: number, optionsOverride: number): Promise<undefined> {
    return LevelControlClusterModule.moveToLevel(
      this.devicePointerStr,
      this.endpointId,
      level,
      transitionTime,
      optionsMask,
      optionsOverride
    )
  }

  /**
   * 读取当前等级值
   * @returns 成功则返回当前等级值
   */
  readCurrentLevel(): Promise<number> {
    return LevelControlClusterModule.readCurrentLevel(
      this.devicePointerStr,
      this.endpointId
    )
  }

}

export class GeneralDiagnosticsCluster extends BaseCluster {

  /**
   * 读取设备的网络接口信息
   * @returns 成功则返回网络接口信息
   */
  readNetworkInterfaces(): Promise<string> {
    return GeneralDiagnosticsClusterModule.readNetworkInterfaces(
      this.devicePointerStr,
      this.endpointId,
    )
  }

}

export class ColorControlCluster extends BaseCluster {

  /**
   * 颜色控制，修改色调和饱和度
   * @param hue 
   * @param saturation 
   * @param transitionTime 
   * @param optionsMask 
   * @param optionsOverride 
   * @returns 
   */
  moveToHueAndSaturation(
    hue: number, 
    saturation: number, 
    transitionTime: number, 
    optionsMask: number, 
    optionsOverride: number
  ): Promise<undefined> {
    return ColorControlClusterModule.moveToHueAndSaturation(
      this.devicePointerStr,
      this.endpointId,
      hue,
      saturation, 
      transitionTime,
      optionsMask,
      optionsOverride
    )
  }

  /**
   * 读取当前色调值
   * @returns 
   */
  readCurrentHue(): Promise<number> {
    return ColorControlClusterModule.readCurrentHue(
      this.devicePointerStr,
      this.endpointId
    )
  }

  /**
   * 读取当前饱和度
   * @returns 
   */
  readCurrentSaturation(): Promise<number> {
    return ColorControlClusterModule.readCurrentSaturation(
      this.devicePointerStr,
      this.endpointId
    )
  }

}
