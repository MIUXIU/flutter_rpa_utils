import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_rpa_utils/accessibility_node_info.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

/// An implementation of [FlutterRpaUtilsPlatform] that uses method channels.
class MethodChannelFlutterRpaUtils extends PlatformInterface {
  /// The method channel used to interact with the native platform.
  ///
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_rpa_utils');

  static final Object _token = Object();

  static final MethodChannelFlutterRpaUtils _instance = MethodChannelFlutterRpaUtils();

  MethodChannelFlutterRpaUtils() : super(token: _token);

  static MethodChannelFlutterRpaUtils get instance => _instance;

  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  Future<bool?> isAccessibilityPermissionEnabled() async {
    return await methodChannel.invokeMethod<bool>('isAccessibilityPermissionEnabled');
  }

  Future<bool?> requestAccessibilityPermission() async {
    return await methodChannel.invokeMethod<bool>('requestAccessibilityPermission');
  }

  Future<bool?> clickButtonByText({required String text, int index = 0}) async {
    final result = await methodChannel.invokeMethod<bool>('clickButtonByText', {"text": text, "index": index});
    return result;
  }

  Future<List<String>> findText({required String text}) async {
    List<String> result = await methodChannel.invokeListMethod<String>('findText', {"text": text}) ?? [];
    return result;
  }

  Future<AccessibilityNodeInfo?> getRootNode() async {
    Map? map = await methodChannel.invokeMethod<Map<dynamic, dynamic>>("getRootNode");
    if (map == null) {
      return null;
    }
    return AccessibilityNodeInfo.fromMap(map);
  }

  Future<AccessibilityNodeInfo?> getChild({required String source, required int index}) async {
    Map? map = await methodChannel.invokeMethod<Map<dynamic, dynamic>>("getChild", {"source": source, "index": index});

    if (map == null) {
      return null;
    }
    return AccessibilityNodeInfo.fromMap(map);
  }
  Future<int> getChildCount({required String source}) async {
    int count = await methodChannel.invokeMethod<int>("getChildCount", {"source": source}) ?? 0;
    return count;
  }

  Future<AccessibilityNodeInfo?> getParent({required String source}) async {
    Map? map = await methodChannel.invokeMethod<Map<dynamic, dynamic>>("getParent", {"source": source});

    if (map == null) {
      return null;
    }
    return AccessibilityNodeInfo.fromMap(map);
  }

  Future<AccessibilityNodeInfo?> getChildMore({required String source, required List<int> indexList}) async {
    Map? map = await methodChannel
        .invokeMethod<Map<dynamic, dynamic>>("getChildMore", {"source": source, "indexList": indexList});
    if (map == null) {
      return null;
    }
    return AccessibilityNodeInfo.fromMap(map);
  }

  Future<List<AccessibilityNodeInfo>> findAccessibilityNodeInfosByText({required String text}) async {
    List<Map<dynamic, dynamic>?> result = await methodChannel
            .invokeListMethod<Map<dynamic, dynamic>>('findAccessibilityNodeInfosByText', {"text": text}) ??
        [];

    List<AccessibilityNodeInfo> accessibilityNodeInfoList = [];
    for (Map? d in result) {
      if (d == null) {
        continue;
      }
      accessibilityNodeInfoList.add(AccessibilityNodeInfo.fromMap(d));
    }
    return accessibilityNodeInfoList;
  }

  Future<bool> globalBack() async {
    return await methodChannel.invokeMethod<bool>('globalBack') ?? false;
  }

  Future<String?> getText({required String source}) async {
    String? result = await methodChannel.invokeMethod<String>('getText', {"source": source});
    return result;
  }

  Future<bool> scrollToPosition({required String source, required int index, required int rowCount}) async {
    bool result = await methodChannel
            .invokeMethod<bool>('scrollToPosition', {"source": source, "rowCount": rowCount, "index": index}) ??
        false;
    return result;
  }

  Future<bool> click({required String source}) async {
    bool result = await methodChannel.invokeMethod<bool>('click', {"source": source}) ?? false;
    return result;
  }

  Future<bool> inputText({required String source, required String text}) async {
    bool result = await methodChannel.invokeMethod<bool>('inputText', {"source": source, "text": text}) ?? false;
    return result;
  }

  Future<AccessibilityNodeInfo?> findNodeByClassName({required String className}) async {
    Map? map = await methodChannel.invokeMethod<Map<dynamic, dynamic>>("findNodeByClassName", {"className": className});
    if (map == null) {
      return null;
    }
    return AccessibilityNodeInfo.fromMap(map);
  }
}
