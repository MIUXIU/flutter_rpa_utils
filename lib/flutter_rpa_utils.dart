import 'flutter_rpa_utils_method_channel.dart';

class FlutterRpaUtils {
  Future<String?> getPlatformVersion() {
    return MethodChannelFlutterRpaUtils.instance.getPlatformVersion();
  }

  Future<bool?> clickButtonByText({required String text}) {
    return MethodChannelFlutterRpaUtils.instance.clickButtonByText(text: text);
  }

// Future<String?> findText({required String content}) {
//   return MethodChannelFlutterRpaUtils.instance.findTextList(content: content);
// }
}
