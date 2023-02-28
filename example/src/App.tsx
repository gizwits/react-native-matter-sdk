import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
// import { DescriptorCluster } from 'react-native-matter-sdk';

export default function App() {
  // const [result, setResult] = React.useState<string | undefined>();

  React.useEffect(() => {
    // new DescriptorCluster(0, 0);
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
