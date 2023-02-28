// import { NativeModules } from 'react-native';

// const DescriptorClusterModule = NativeModules.DescriptorClusterModule

class BaseCluster {
  devicePointer: number;

  endpointId: number;

  constructor(devicePointer: number, endpointId: number) {
    this.devicePointer = devicePointer;
    this.endpointId = endpointId;
  }
}

export class DescriptorCluster extends BaseCluster {}
