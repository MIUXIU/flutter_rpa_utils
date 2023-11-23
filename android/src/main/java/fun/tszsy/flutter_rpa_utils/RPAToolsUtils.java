package fun.tszsy.flutter_rpa_utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class RPAToolsUtils {

    private final static String TAG = RPAToolsUtils.class.getSimpleName();

    /**
     * 点击某个按钮
     *
     * @return 是否成功点击
     */
    public static boolean clickButton(AccessibilityService accessibilityService, AccessibilityNodeInfo nodeInfo) {

        boolean result = false;

        result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        if (result) {
            return true;
        }

        Rect boundsInScreen = new Rect();
        nodeInfo.getBoundsInScreen(boundsInScreen);
        int x = boundsInScreen.centerX();
        int y = boundsInScreen.centerY();
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100));
        GestureDescription gesture = builder.build();
        result = accessibilityService.dispatchGesture(gesture, null, null);
        return result;
    }

    /**
     * Gesture手势实现滚动(Android7+)
     * 解决滚动距离不可控制问题
     *
     * @param distanceX 向右滚动为负值 向左滚动为正值
     * @param distanceY 向下滚动为负值 向上滚动为正值
     */
    public static Boolean scrollByNode(AccessibilityService service, AccessibilityNodeInfo nodeInfo, int distanceX,
                                       int distanceY) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        Point point = new Point((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(point.x, point.y);
        path.lineTo(point.x + distanceX, point.y + distanceY);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0L, 300L));
        GestureDescription gesture = builder.build();
        return service.dispatchGesture(gesture, null, null);
    }

    /**
     * 滚动到指定行列 personIndex,rowCunt
     */
    public static Boolean scrollToPosition(AccessibilityNodeInfo nodeInfo, int personIndex, int rowCunt) {
        Log.d(TAG, "滚动到指定位置 scrollToPosition: personIndex" + personIndex);
        int columnIndex = personIndex % rowCunt;
        int rowIndex = personIndex / rowCunt;

        Log.d(TAG, "scrollToPosition: rowIndex" + rowIndex);
        Log.d(TAG, "scrollToPosition: columnIndex" + columnIndex);


        Bundle bundle = new Bundle();
        bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_ROW_INT, rowIndex);
        bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_COLUMN_INT, columnIndex);
        return nodeInfo.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION.getId(),
                bundle);
    }

    public static Boolean scrollToPositionByRowAndColumn(AccessibilityNodeInfo nodeInfo, int row, int column) {
        Bundle bundle = new Bundle();
        bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_ROW_INT, row);
        bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_COLUMN_INT, column);
        return nodeInfo.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION.getId(),
                bundle);
    }

    public  static  boolean inputText(AccessibilityNodeInfo nodeInfo,String text){
        Bundle requireArguments = new Bundle();
        requireArguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        boolean result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, requireArguments);
        return result;
    }

    /**
     * 获取节点大小位置
     */
    public static Rect getAccessibilityNodeInfoRect(AccessibilityNodeInfo nodeInfo) {
        Rect boundsInScreen = new Rect();
        nodeInfo.getBoundsInScreen(boundsInScreen);
        return boundsInScreen;
    }

    /**
     * 返回上一界面
     */
    public static void globalBack(AccessibilityService service) {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public static AccessibilityNodeInfo findGridView(AccessibilityNodeInfo rootNode) {
        AccessibilityNodeInfo accessibilityNodeInfoGridView = rootNode.getChild(0).getChild(3);
        if (accessibilityNodeInfoGridView == null || !accessibilityNodeInfoGridView.getClassName().equals("android.widget.GridView")) {
            accessibilityNodeInfoGridView = findNodeByClassName(rootNode, "android.widget.GridView");
        }

        return findNodeByClassName(rootNode, "android.widget.GridView");
    }

    public static AccessibilityNodeInfo findNodeByClassName(AccessibilityNodeInfo parentNode, String className) {
        if (parentNode == null) {
            return null;
        }
        int childCount = parentNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo childNode = parentNode.getChild(i);
            if (childNode != null) {
                if (className.equals(childNode.getClassName())) {
                    return childNode;
                } else {
                    AccessibilityNodeInfo result = findNodeByClassName(childNode, className);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

}
