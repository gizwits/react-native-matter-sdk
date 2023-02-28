import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-matter-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Matter = NativeModules.Matter
  ? NativeModules.Matter
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * 解析Matter设备所关联的配对二维码内容，返回用于配对设备的负载信息
 * @param qrCodeContent 设备二维码内容
 * @returns 成功则返回Json形式的设备产品信息，否则返回异常
 */
export function parseForSetupPayload(qrCodeContent: string): Promise<string> {
  return Matter.parseForSetupPayload(qrCodeContent);
}

/**
 * 通过蓝牙搜索并配对设备，并将其添加至网络
 * @param deviceId 设备的ID
 * @param discriminator 设备识别码
 * @param setupPinCode 身份校验码
 * @param wifiSSID wifi名称
 * @param wifiPassword wifi密码
 * @returns
 */
export function pairDeviceWithBle(
  deviceId: number,
  discriminator: number,
  setupPinCode: number,
  wifiSSID: string,
  wifiPassword: string
): Promise<number> {
  return Matter.pairDeviceWithBle(
    deviceId,
    discriminator,
    setupPinCode,
    wifiSSID,
    wifiPassword
  );
}
