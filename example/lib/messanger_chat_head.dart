import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter_overlay_window/flutter_overlay_window.dart';

class MessangerChatHead extends StatefulWidget {
  const MessangerChatHead({Key? key}) : super(key: key);

  @override
  State<MessangerChatHead> createState() => _MessangerChatHeadState();
}

class _MessangerChatHeadState extends State<MessangerChatHead> {
  Color color = const Color(0xFFFF0C0C);
  BoxShape shape = BoxShape.rectangle;
  String data = '123';

  @override
  void initState() {
    super.initState();
    FlutterOverlayWindow.overlayListener.listen((event) {
      setState(() {
        data = event.toString();
        log('Received Data : ${event.toString()}');
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Material(
        color: Colors.transparent,
        elevation: 0.0,
        child: GestureDetector(
          onTap: () async {
            if (shape == BoxShape.circle) {
              await FlutterOverlayWindow.resizeOverlay(300, 300);
              setState(() {
                shape = BoxShape.rectangle;
              });
            } else {
              await FlutterOverlayWindow.resizeOverlay(150, 150);
              setState(() {
                shape = BoxShape.circle;
              });
            }
          },
          child: Container(
            height: MediaQuery.of(context).size.height,
            decoration: BoxDecoration(color: color, shape: shape),
            child:  Center(
              child: Text(data, style: const TextStyle(color: Colors.black, fontSize: 20),),
            ),
          ),
        ),
      ),
    );
  }
}