import 'flutter_rpa_utils_method_channel.dart';
import 'service/constants.dart';
import 'service/utils.dart';

class AccessibilityNodeInfo {
  String _source = "";

  /// the performed action that triggered this event
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getAction()
  int? actionType;

  /// the time in which this event was sent.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED
  DateTime? eventTime;

  /// the package name of the source
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getPackageName()
  String? packageName;

  String? className;

  /// the event type.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getEventTime()
  EventType? eventType;

  /// Gets the text of this node.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo#getText()
  String? capturedText;

  /// the bit mask of change types signaled by a `TYPE_WINDOW_CONTENT_CHANGED` event or `TYPE_WINDOW_STATE_CHANGED`. A single event may represent multiple change types
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getContentChangeTypes()
  ContentChangeTypes? contentChangeTypes;

  /// the movement granularity that was traversed
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent#getMovementGranularity()
  int? movementGranularity;

  /// the type of the window
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#getType()
  WindowType? windowType;

  /// check if this window is active. An active window is the one the user is currently touching or the window has input focus and the user is not touching any window.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#getType()
  bool? isActive;

  /// check if this window has input focus.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#isFocused()
  bool? isFocused;

  /// Check if the window is in picture-in-picture mode.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityWindowInfo#isInPictureInPictureMode()
  bool? isPip;

  /// Gets the node bounds in screen coordinates.
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo#getBoundsInScreen(android.graphics.Rect)
  Rect? rect;

  /// Get the node childrens and sub childrens text
  /// https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo#getChild(int)
  List<String>? nodesText;

  AccessibilityNodeInfo({
    required String source,
    this.actionType,
    this.eventTime,
    this.packageName,
    this.className,
    this.eventType,
    this.capturedText,
    this.contentChangeTypes,
    this.movementGranularity,
    this.windowType,
    this.isActive,
    this.isFocused,
    this.isPip,
    this.rect,
    this.nodesText,
  }) : _source = source;

  AccessibilityNodeInfo.fromMap(Map<dynamic, dynamic> map) {
    _source = map['source'];
    actionType = map['actionType'];
    eventTime = DateTime.now();
    packageName = map['packageName'];
    className = map['className'];
    eventType = map['eventType'] == null ? null : Utils.eventType[map['eventType']];
    capturedText = map['capturedText'];
    contentChangeTypes = map['contentChangeTypes'] == null
        ? null
        : (Utils.changeType[map['contentChangeTypes']] ?? ContentChangeTypes.others);
    movementGranularity = map['movementGranularity'];
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
       Action Type: $actionType 
       Event Time: $eventTime 
       Package Name: $packageName 
       class Name: $className 
       Event Type: $eventType 
       Captured Text: $capturedText 
       content Change Types: $contentChangeTypes 
       Movement Granularity: $movementGranularity
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

  Future<AccessibilityNodeInfo?> getChild({required int index}) {
    return MethodChannelFlutterRpaUtils.instance.getChild(source: _source, index: index);
  }
  Future<AccessibilityNodeInfo?> getChildMore({required List<int> indexList}) {
    return MethodChannelFlutterRpaUtils.instance.getChildMore(source: _source, indexList: indexList);
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
