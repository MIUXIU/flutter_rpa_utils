import 'flutter_rpa_utils_method_channel.dart';
import 'service/constants.dart';
import 'service/utils.dart';

class AccessibilityNodeInfo {
  String _source = "";

  int? childCount;

  String? packageName;

  String? className;

  String? capturedText;

  WindowType? windowType;

  bool? isActive;

  bool? isFocused;

  bool? isPip;

  Rect? rect;

  List<String>? nodesText;

  AccessibilityNodeInfo({
    required String source,
    this.childCount,
    this.packageName,
    this.className,
    this.capturedText,
    this.windowType,
    this.isActive,
    this.isFocused,
    this.isPip,
    this.rect,
    this.nodesText,
  }) : _source = source;

  AccessibilityNodeInfo.fromMap(Map<dynamic, dynamic> map) {
    _source = map['source'];
    childCount = map['childCount'];
    packageName = map['packageName'];
    className = map['className'];
    capturedText = map['capturedText'];
    windowType = map['windowType'] == null ? null : Utils.windowType[map['windowType']];
    isActive = map['isActive'];
    isFocused = map['isFocused'];
    isPip = map['isPip'];
    rect = map['screenBounds'] != null ? Rect.fromMap(map['screenBounds']) : null;
    nodesText = map['nodesText'] == null
        ? []
        : [
            ...{...map['nodesText']}
          ];
  }

  @override
  String toString() {
    return '''AccessibilityEvent: (
       source : $_source 
       childCount: $childCount 
       Package Name: $packageName 
       class Name: $className 
       Captured Text: $capturedText 
       Is Active: $isActive
       is focused: $isFocused
       in Pip: $isPip
       window Type: $windowType
       Screen bounds: $rect
       Nodes Text: $nodesText
       )''';
  }

  Future<String?> getText() {
    return MethodChannelFlutterRpaUtils.instance.getText(source: _source);
  }

  Future<AccessibilityNodeInfo?> getParent() {
    return MethodChannelFlutterRpaUtils.instance.getParent(source: _source);
  }

  Future<AccessibilityNodeInfo?> getChild({required int index}) {
    return MethodChannelFlutterRpaUtils.instance.getChild(source: _source, index: index);
  }

  Future<int> getChildCount() {
    return MethodChannelFlutterRpaUtils.instance.getChildCount(source: _source);
  }

  Future<AccessibilityNodeInfo?> getChildMore({required List<int> indexList}) {
    return MethodChannelFlutterRpaUtils.instance.getChildMore(source: _source, indexList: indexList);
  }

  Future<bool> scrollToPosition({required int index, required int rowCount}) {
    return MethodChannelFlutterRpaUtils.instance.scrollToPosition(source: _source, index: index, rowCount: rowCount);
  }

  Future<bool> click() {
    return MethodChannelFlutterRpaUtils.instance.click(source: _source);
  }
  Future<bool> inputText(String text) {
    return MethodChannelFlutterRpaUtils.instance.inputText(source: _source,text: text);
  }
}

class Rect {
  int? right;
  int? top;
  int? left;
  int? bottom;
  int? width;
  int? height;
  int? centerY;
  int? centerX;

  Rect({
    this.centerX,
    this.centerY,
    this.right,
    this.top,
    this.left,
    this.bottom,
    this.width,
    this.height,
  });

  Rect.fromMap(Map<dynamic, dynamic> json) {
    centerY = json['centerY'];
    centerX = json['centerX'];
    right = json['right'];
    top = json['top'];
    left = json['left'];
    bottom = json['bottom'];
    width = json['width'];
    height = json['height'];
  }

  @override
  String toString() {
    return "left: $left - right: $right - top: $top - bottom: $bottom - width: $width - height: $height";
  }
}
