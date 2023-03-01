import { NativeModules } from 'react-native';
export * from './clusters';

const MatterModule = NativeModules.MatterModule;

/**
 * 解析Matter设备所关联的配对二维码内容，返回用于配对设备的负载信息
 * @param qrCodeContent 设备二维码内容
 * @returns 成功则返回Json形式的设备产品信息
 */
export function parseForSetupPayload(qrCodeContent: string): Promise<string> {
  return MatterModule.parseForSetupPayload(qrCodeContent);
}

/**
 * 根据设备ID获取映射到已配对设备的指针
 * @param deviceIdStr 设备ID的字符串表现形式
 * @returns 成功则返回设备的指针（长整形）的字符串表现形式
 */
export function getPairedDevicePointer(deviceIdStr: string): Promise<string> {
  return MatterModule.getPairedDevicePointer(deviceIdStr);
}

/**
 * 通过蓝牙搜索并配对设备，并将其添加至网络
 * @param deviceIdStr 设备的ID的字符串表现形式
 * @param discriminator 设备识别码
 * @param setupPinCodeStr 身份校验码的字符串表现形式
 * @param wifiSSID wifi名称
 * @param wifiPassword wifi密码
 * @returns 成功则返回设备ID（长整形）的字符串表现形式
 */
export function pairDeviceWithBle(
  deviceIdStr: number,
  discriminator: number,
  setupPinCodeStr: number,
  wifiSSID: string,
  wifiPassword: string
): Promise<string> {
  return MatterModule.pairDeviceWithBle(
    deviceIdStr,
    discriminator,
    setupPinCodeStr,
    wifiSSID,
    wifiPassword
  );
}
