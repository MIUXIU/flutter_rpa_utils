import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_overlay_window/flutter_overlay_window.dart';
import 'package:flutter_rpa_utils/flutter_rpa_utils.dart';
import 'package:flutter_rpa_utils/service/flutter_accessibility_service.dart';

import 'messanger_chat_head.dart';

void main() {
  runApp(const MyApp());
}

// @pragma("vm:entry-point")
// void overlayMain() {
//   // String a = "1";
//   MaterialApp(
//       home: Scaffold(
//         body: TextButton(
//           onPressed: () async {
//             await FlutterOverlayWindow.shareData("dismiss");
//           },
//           child: Text("Dismiss"),
//         ),
//       ));
// }


// overlay entry point
@pragma("vm:entry-point")
void overlayMain() {
  runApp(const MessangerChatHead());
}



class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _flutterRpaUtilsPlugin = FlutterRpaUtils();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // bool status = await FlutterAccessibilityService.isAccessibilityPermissionEnabled();

    // bool status = await FlutterAccessibilityService.requestAccessibilityPermission();

    /// stream the incoming Accessibility events
    FlutterAccessibilityService.accessStream.listen((event) {
      setState(() {
        _platformVersion = event.toString();
      });
      /*
  Current Event: AccessibilityEvent: (
     Action Type: 0
     Event Time: 2022-04-11 14:19:56.556834
     Package Name: com.facebook.katana
     Event Type: EventType.typeWindowContentChanged
     Captured Text: events you may like
     content Change Types: ContentChangeTypes.contentChangeTypeSubtree
     Movement Granularity: 0
     Is Active: true
     is focused: true
     in Pip: false
     window Type: WindowType.typeApplication
     Screen bounds: left: 0 - right: 720 - top: 0 - bottom: 1544 - width: 720 - height: 1544
)
  */
    });

    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              MaterialButton(
                color: Colors.red,
                onPressed: () async {
                  await FlutterOverlayWindow.showOverlay(width: 300, height: 300, overlayContent: "123333");
                },
              ),
              MaterialButton(
                color: Colors.green,
                onPressed: () async {
                  await FlutterOverlayWindow.shareData("123444");
                },
              ),
              Text('Running on: $_platformVersion\n'),
            ],
          ),
        ),
      ),
    );
  }
}
