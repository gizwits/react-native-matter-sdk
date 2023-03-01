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
 * @param deviceId 设备ID
 * @returns 成功则返回设备的指针（长整形）的字符串表现形式
 */
export function getPairedDevicePointer(deviceId: number): Promise<string> {
  let deviceIdStr: string = String(deviceId);
  return MatterModule.getPairedDevicePointer(deviceIdStr);
}

/**
 * 通过蓝牙搜索并配对设备，并将其添加至网络
 * @param deviceId 设备的ID
 * @param discriminator 设备识别码
 * @param setupPinCode 身份校验码
 * @param wifiSSID wifi名称
 * @param wifiPassword wifi密码
 * @returns 成功则返回设备ID（长整形）的字符串表现形式
 */
export function pairDeviceWithBle(
  deviceId: number,
  discriminator: number,
  setupPinCode: number,
  wifiSSID: string,
  wifiPassword: string
): Promise<string> {
  let deviceIdStr: string = String(deviceId);
  let setupPinCodeStr: string = String(setupPinCode);
  return MatterModule.pairDeviceWithBle(
    deviceIdStr,
    discriminator,
    setupPinCodeStr,
    wifiSSID,
    wifiPassword
  );
}
