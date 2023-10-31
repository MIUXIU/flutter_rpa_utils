

import 'flutter_rpa_utils_method_channel.dart';

class FlutterRpaUtils {
  Future<String?> getPlatformVersion() {
    return MethodChannelFlutterRpaUtils.instance.getPlatformVersion();
  }
}
