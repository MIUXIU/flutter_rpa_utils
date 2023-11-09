import 'accessibility_node_info.dart';
import 'flutter_rpa_utils_method_channel.dart';

class FlutterRpaUtils {
  Future<AccessibilityNodeInfo?> getRootNode() {
    return MethodChannelFlutterRpaUtils.instance.getRootNode();
  }

  Future<String?> getPlatformVersion() {
    return MethodChannelFlutterRpaUtils.instance.getPlatformVersion();
  }

  Future<bool?> isAccessibilityPermissionEnabled() {
    return MethodChannelFlutterRpaUtils.instance.isAccessibilityPermissionEnabled();
  }

  Future<bool?> requestAccessibilityPermission() {
    return MethodChannelFlutterRpaUtils.instance.requestAccessibilityPermission();
  }

  Future<bool?> clickButtonByText({required String text, int index = 0}) {
    return MethodChannelFlutterRpaUtils.instance.clickButtonByText(text: text, index: index);
  }

  Future<List<String>> findText({required String text}) {
    return MethodChannelFlutterRpaUtils.instance.findText(text: text);
  }

  Future<List<AccessibilityNodeInfo>> findAccessibilityNodeInfosByText({required String text}) {
    return MethodChannelFlutterRpaUtils.instance.findAccessibilityNodeInfosByText(text: text);
  }


  Future<AccessibilityNodeInfo?> findNodeByClassName({required String className}) {
    return MethodChannelFlutterRpaUtils.instance.findNodeByClassName(className: className);
  }

// Future<String?> findText({required String content}) {
//   return MethodChannelFlutterRpaUtils.instance.findTextList(content: content);
// }
}
