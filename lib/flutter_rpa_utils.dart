

import 'flutter_rpa_utils_method_channel.dart';

class FlutterRpaUtils {
  Future<String?> getPlatformVersion() {
    return MethodChannelFlutterRpaUtils.instance.getPlatformVersion();
  }

  Future<String?> findText({required String content}) {
    return MethodChannelFlutterRpaUtils.instance.findTextList(content: content);
  }
}
