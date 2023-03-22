import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { parseForSetupPayload } from 'react-native-matter-sdk';

export default function App() {
  const [message, setMessage] = React.useState<string | undefined>();

  React.useEffect(() => {
    
  }, []);

  return (
    <View style={styles.container}>
      <Text>设备ID: {message}</Text>
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
