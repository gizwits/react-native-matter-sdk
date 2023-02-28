import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
// import { pairDeviceWithBle } from 'react-native-matter-sdk';

export default function App() {
  // const [result, setResult] = React.useState<string | undefined>();

  React.useEffect(() => {
    // MT:Y.K9042C00KA0648G00
  }, []);

  return (
    <View style={styles.container}>
      <Text>Text</Text>
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
