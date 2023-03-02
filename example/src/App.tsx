import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { pairDeviceWithBle, getPairedDevicePointer, OnOffCluster } from 'react-native-matter-sdk';

export default function App() {
  const [message, setMessage] = React.useState<string | undefined>();

  React.useEffect(() => {
    // 配对Matter设备
    let deviceIdStr: string = '1';
    let discriminator: number = 3840;
    let setupPinCodeStr: string = '20202021';
    let wifiSSID: string = 'MERCURY_23A4';
    let wifiPassword: string = '#web123456';
    console.log("pairDeviceWithBle");
    pairDeviceWithBle(deviceIdStr, discriminator, setupPinCodeStr, wifiSSID, wifiPassword).then(
      (value) => {
        console.log("pairDeviceWithBle success");
        setMessage("success");
        console.log("getPairedDevicePointer");
        getPairedDevicePointer(value).then((devicePointerStr) => {
            new OnOffCluster(devicePointerStr, 1).readOnOff().then((isOn) => {
                console.log('isOn: ' + isOn);
            })

        })
      }
    ).catch(
      () => {
        console.log("pairDeviceWithBle failure");
        setMessage("failure");
      }
    );
  }, []);

  return (
    <View style={styles.container}>
      <Text>pairDeviceWithBle: {message}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
