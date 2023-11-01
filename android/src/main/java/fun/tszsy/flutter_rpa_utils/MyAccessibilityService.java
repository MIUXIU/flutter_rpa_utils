package fun.tszsy.flutter_rpa_utils;

import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import fun.tszsy.flutter_rpa_utils.service.AccessibilityListener;

public class MyAccessibilityService extends AccessibilityListener {
    private final static String TAG = MyAccessibilityService.class.getSimpleName();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        super.onAccessibilityEvent(event);

        // 获取当前界面的根节点
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) {
            return;
        }
        RPAManager.rootNode = rootNode;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onInterrupt() {
        // 中断事件
    }




}
