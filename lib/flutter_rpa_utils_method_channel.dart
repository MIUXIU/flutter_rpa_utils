import 'dart:ffi';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
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

  Future<Bool?> findText({required String content}) async {
    final result = await methodChannel.invokeMethod<Bool>(
        'findText', {"content": content});
    return result;
  }


  Future<Bool?> clickButton({required Object accessibilityService, required Object accessibilityNodeInfo}) async {
    final result = await methodChannel.invokeMethod<Bool>(
        'clickButton', {"accessibilityService": accessibilityService, "accessibilityNodeInfo": accessibilityNodeInfo});
    return result;
  }
}
