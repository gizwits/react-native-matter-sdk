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
 * 解析Matter设备的手动配对码，返回用于配对设备的负载信息
 * @param manualCode 设备的手动配对码
 * @returns 成功则返回Json形式的设备产品信息
 */
export function parseManualCodeForSetupPayload(
  manualCode: string
): Promise<string> {
  return MatterModule.parseManualCodeForSetupPayload(manualCode);
}

/**
 * 获取Matter设备的手动配对码
 * @param payload 设备原始配对信息的Json字符串（由parseForSetupPayload接口返回的内容）
 * @returns 成功则返回设备的手动配对码
 */
export function getManualEntryCodeFromPayload(
  payload: string
): Promise<string> {
  return MatterModule.getManualEntryCodeFromPayload(payload)
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
 * 开启设备的配对窗口
 * @param devicePointerStr 设备指针的字符串表现形式
 * @param duration 开启的持续时间，单位：秒
 * @returns
 */
export function openPairingWindowCallback(
  devicePointerStr: string,
  duration: number
): Promise<undefined> {
  return MatterModule.openPairingWindowCallback(devicePointerStr, duration);
}

/**
 * 通过局域网搜索并配对设备
 * @param deviceIdStr 设备的ID的字符串表现形式
 * @param discriminator 设备识别码
 * @param setupPinCodeStr 身份校验码的字符串表现形式
 * @returns 成功则返回设备ID（长整形）的字符串表现形式
 */
export function pairDeviceWithAddress(
  deviceIdStr: string,
  discriminator: number,
  setupPinCodeStr: string
): Promise<string> {
  return MatterModule.pairDeviceWithAddress(
    deviceIdStr,
    discriminator,
    setupPinCodeStr
  );
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
  deviceIdStr: string,
  discriminator: number,
  setupPinCodeStr: string,
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
