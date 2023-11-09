package fun.tszsy.flutter_rpa_utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPAManager {
    private final static String TAG = RPAManager.class.getSimpleName();
    private Handler mainThreadHandler;
    private Handler mHandler;

    public static AccessibilityNodeInfo rootNode;
    public static AccessibilityService accessibilityService;

    public RPAManager() {
        HandlerThread mHandlerThread = new HandlerThread("RPAThread");
        mHandlerThread.start();
        mainThreadHandler = new Handler(Looper.getMainLooper());
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public List<String> findText(String content) {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList;
        if (rootNode != null) {
            accessibilityNodeInfoList = rootNode.findAccessibilityNodeInfosByText(content);
        } else {
            accessibilityNodeInfoList = new ArrayList<>();
        }
        List<String> resultList = new ArrayList<>();
        for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfoList) {
            if (accessibilityNodeInfo == null || accessibilityNodeInfo.getText() == null) {
                continue;
            }
            String tempString = accessibilityNodeInfo.getText().toString();
            if (TextUtils.isEmpty(tempString)) {
                continue;
            }
            resultList.add(tempString);
        }

        return resultList;
    }

    /**
     * 根据类名查找节点
     */
    public AccessibilityNodeInfo findNodeByClassName(AccessibilityNodeInfo parentNode, String className) {
        if (parentNode == null) {
            return null;
        }

        int childCount = parentNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo childNode = parentNode.getChild(i);
            if (childNode != null) {
                if (className.contentEquals(childNode.getClassName())) {
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
